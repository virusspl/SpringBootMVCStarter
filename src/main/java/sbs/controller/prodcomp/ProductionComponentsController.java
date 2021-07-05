package sbs.controller.prodcomp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.config.error.OutOfHeapMemoryException;
import sbs.helpers.DateHelper;
import sbs.helpers.FileHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3BomPart;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3DeliverySimpleInfo;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductMachine;
import sbs.model.x3.X3SaleInfo;
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.system.MemoryService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;
import sbs.singleton.LockSingleton;

@Controller
@RequestMapping("prodcomp")
public class ProductionComponentsController {

	
	/*
	 1. find components: /make
	 2. find final products: /findchains
	 3. check plan: /makeplan
	 */
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcOracleGeodeService geodeService;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;
	@Autowired
	MemoryService memoryService;
	@Autowired
	Environment environment;
	@Autowired
	FileHelper fileHelper;
	@Autowired
	LockSingleton lock;
	@Autowired
	UserService userService;
	
	private double outOfMemoryThreshold;
	private String outOfMemoryMessage;
	private boolean doLog;

	public ProductionComponentsController() {
		this.doLog = false;
	}

	@RequestMapping("/main")
	public String view(Model model, Locale locale,
			@CookieValue(value = "startDateChain", defaultValue = "0") String startDateLong,
			@CookieValue(value = "endDateChain", defaultValue = "0") String endDateLong) {
		/*
		 * Calendar cal = Calendar.getInstance(); cal.add(Calendar.MONTH, -1);
		 * cal.set(Calendar.DAY_OF_MONTH, 1); formComponent.setStartDate(new
		 * Timestamp(cal.getTimeInMillis()));
		 * 
		 * cal = Calendar.getInstance(); cal.add(Calendar.MONTH, 1);
		 * cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 * formComponent.setEndDate(new Timestamp(cal.getTimeInMillis()));
		 */
		
		this.outOfMemoryThreshold = Double.parseDouble(environment.getRequiredProperty("prodcomp.memory.threshold"));
		this.outOfMemoryMessage = messageSource.getMessage("prodcomp.error.memory", null, "OUT OF MEMORY!", locale);

		FormComponent formComponent = new FormComponent();
		if (startDateLong.length() > 1 && endDateLong.length() > 1) {
			formComponent.setStartDate(new java.util.Date(Long.parseLong(startDateLong)));
			formComponent.setEndDate(new java.util.Date(Long.parseLong(endDateLong)));
		}
		model.addAttribute("formComponent", formComponent);
		model.addAttribute("formFindComponents", new FormFindComponents());
		return "prodcomp/main";
	}

	@RequestMapping(value = "/findcomponents", params = { "find" }, method = RequestMethod.POST)
	public String findComponents(@Valid FormFindComponents formFindComponents, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("formComponent", new FormComponent());
			model.addAttribute("error", "components lock");
			return "prodcomp/main";
		}

		formFindComponents.setItem(formFindComponents.getItem().toUpperCase().trim());
		String itemDescription = x3Service.findItemDescription("ATW", formFindComponents.getItem());
		if (itemDescription == null) {
			bindingResult.rejectValue("item", "error.no.such.product", "ERROR");
			model.addAttribute("formComponent", new FormComponent());
			return "prodcomp/main";
		}

		redirectAttrs.addFlashAttribute("code", formFindComponents.getItem());
		redirectAttrs.addFlashAttribute("quantity", formFindComponents.getQuantity());
		redirectAttrs.addFlashAttribute("tillDate", formFindComponents.getTillDate());
		redirectAttrs.addFlashAttribute("single", true);

		return "redirect:/prodcomp/make";
	}

	private Map<String, Integer> getDemandMapByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		java.util.Date start, end;
		cal.setTime(date);
		end = cal.getTime();
		cal.add(Calendar.YEAR,-100);
		start = cal.getTime();
		return x3Service.getDemandListInPeriod(start, end, "ATW");
	}
	
	@RequestMapping("/make")
	public String doMake(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {
		
		// activity log
		if(doLog) {
			fileHelper.appendToTmpLog("make");
		}
		
		String singleCode = null;
		Map<String, Integer> demandMap = new HashMap<>(); 
		
		try {
			// get file
			File file;

			if (model.asMap().containsKey("single")) {
				String code = (String) model.asMap().get("code");
				singleCode = code;
				int quantity = (int) model.asMap().get("quantity");

				File tmpFile = File.createTempFile("prodcom", ".tmp");
				tmpFile.deleteOnExit();
				BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
				bw.write(code + ";" + quantity);
				bw.close();
				file = tmpFile;
				
				// demand map (for single version - in file version other call)
				demandMap = getDemandMapByDate((java.util.Date)model.asMap().get("tillDate"));
			} else {
				file = (File) model.asMap().get("file");
			}

			if (file != null) {
				// file exist
				Map<String, Integer> fileInfo = new TreeMap<>();
				// READ FILE
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String line;
					String code;
					int quantity;
					String[] split;
					int lineNo = 0;
					while ((line = br.readLine()) != null) {
						lineNo++;
						// extract date from first line
						if(!line.contains(";")) {
							// try parse date
							try {
								SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
								Date date = formatter.parse(line.trim());
								// make demand map - for file version - in single version other call
								demandMap = getDemandMapByDate(date);
								continue;
							} catch (ParseException e) {
								redirectAttrs.addFlashAttribute("error", line + " - " + messageSource.getMessage("general.bad.format", null, locale));
								br.close();
								file.delete();
								return "redirect:/prodcomp/main";
							}
						}
						// lines other than date
						split = line.split(";");
						// structure
						if (split.length != 2) {
							redirectAttrs.addFlashAttribute("error",
									messageSource.getMessage("error.bad.file.structure", null, locale) + ". "
											+ messageSource.getMessage("general.line", null, locale) + " " + lineNo
											+ ": " + line);
							br.close();
							file.delete();
							return "redirect:/prodcomp/main";
						}
						// values
						try {
							code = split[0].toUpperCase().trim();
							quantity = Math.abs(Integer.parseInt(split[1]));
							if (fileInfo.containsKey(code)) {
								fileInfo.put(code, fileInfo.get(code) + quantity);
							} else {
								fileInfo.put(code, quantity);
							}
						} catch (NumberFormatException ex) {
							redirectAttrs.addFlashAttribute("error",
									messageSource.getMessage("error.mustbeanumber", null, locale) + ". "
											+ messageSource.getMessage("general.line", null, locale) + " " + lineNo
											+ ": " + line);
							br.close();
							file.delete();
							return "redirect:/prodcomp/main";
						}
					}
					br.close();
					file.delete();
				}

				// pending lock start
				if(this.lock.isComponentsLock()) {
					redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.procedure.pending", null, locale) + " (" + lock.getComponentsLockUser() + ")");
					return "redirect:/prodcomp/main";
				}
				else {
					this.lock.setComponentsLock(true);
					this.lock.setComponentsLockUser(userService.getAuthenticatedUser().getName());
				}
				
				// DO CALCULATION
				// get all boms
				Map<String, Double> allComponents = new TreeMap<>();
				Map<String, Double> subComponents;
				Map<String, Double> quantities = x3Service.getAllProductsQuantities("ATW");
				Map<String, Double> geodeStock = geodeService.getStockOnProductionAndReceptions();
				Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
				Map<String, X3ProductMachine> finalMachines = x3Service.findX3ProductFinalMachines("ATW");
				Map<String, X3ProductMachine> firstMachines = x3Service.findX3ProductFirstMachines("ATW");
				// get acv info
				List<X3ConsumptionProductInfo> acvInfo = x3Service.getListForConsumptionReport("ACV", "ATW");
				Map<String, X3DeliverySimpleInfo> upcomingDeliveries = x3Service
						.getFirstUpcomingDeliveriesMapByCodeAfterDate(new java.util.Date(), "ATW");
				// safety stock map
				Map<String, Integer> safetyStockMap = new TreeMap<>();
				Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");

				
				for (Map.Entry<String, Integer> main : fileInfo.entrySet()) {
					subComponents = getComponentsQuantitiesMultilevel(allBoms, main.getKey(), products, false);
					for (Map.Entry<String, Double> sub : subComponents.entrySet()) {
						if (allComponents.containsKey(sub.getKey())) {
							allComponents.put(sub.getKey(),
									allComponents.get(sub.getKey()) + (sub.getValue() * main.getValue()));
						} else {
							allComponents.put(sub.getKey(), sub.getValue() * main.getValue());
						}
					}
				}

				Map<String, X3ConsumptionProductInfo> acvInfoMap = new TreeMap<>();
				for (X3ConsumptionProductInfo info : acvInfo) {
					safetyStockMap.put(info.getProductCode(), info.getSafetyStock());
					acvInfoMap.put(info.getProductCode(), info);
				}
				// replenish point map
				Map<String, Integer> replenishMap = new TreeMap<>();
				for (X3ConsumptionProductInfo info : acvInfo) {
					replenishMap.put(info.getProductCode(), info.getReorderPoint());
				}

				List<List<String>> table = new ArrayList<>();
				List<String> line;
				String gr2;
				boolean showUpcomming;
				double x3, qty, geode;
				Calendar cal;
				int leadTimeDays;
				for (Map.Entry<String, Double> entry : allComponents.entrySet()) {
					// show or hide upcomming dates/quantities
					gr2 = products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getGr2() : "-";
					if(gr2.equalsIgnoreCase("PRE") || gr2.equalsIgnoreCase("BLA")) {
						showUpcomming = false;
					}
					else {
						showUpcomming = true;
					}
					
					x3 = quantities.getOrDefault(entry.getKey(), 0.0);
					// quantities.containsKey(entry.getKey()) ? quantities.get(entry.getKey()) : 0;
					qty = entry.getValue();
					geode = geodeStock.getOrDefault(entry.getKey(), 0.0);

					line = new ArrayList<>();
					
					// product code (0)
					line.add(entry.getKey());
					// description (1)
					line.add(
							products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getDescription() : "-");
					// category (2)
					line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getCategory() : "-");
					// group 2 (3)
					line.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getGr2() : "-");
					// final machine (4)
					line.add(finalMachines.containsKey(entry.getKey()) ? finalMachines.get(entry.getKey()).getMachineCode() : "-");
					// safety stock (5)
					line.add(safetyStockMap.containsKey(entry.getKey())
							? textHelper.numberFormatIntegerRoundNoSpace(safetyStockMap.get(entry.getKey()))
							: "-");
					// replenish point (6)
					line.add(replenishMap.containsKey(entry.getKey())
							? textHelper.numberFormatIntegerRoundNoSpace(replenishMap.get(entry.getKey()))
							: "-");
					// x3 stock (7)
					line.add(textHelper.numberFormatIntegerRoundNoSpace(x3));
					// demand X3 (8)
					if(demandMap != null) {
						if(demandMap.containsKey(entry.getKey())) {

							line.add(textHelper.numberFormatIntegerRoundNoSpace(demandMap.get(entry.getKey())));
						}
						else {
							line.add("-");
						}
					}
					else {
						line.add("-");
					}
					// geode prd/rcp (9)
					line.add(textHelper.numberFormatIntegerRoundNoSpace(geode));
					// quantity (10)
					line.add(textHelper.numberFormatIntegerRoundNoSpace(qty));
					// demand (11)
					if (x3 - qty >= 0) {
						line.add(textHelper.numberFormatIntegerRoundNoSpace(0.0));
					} else {
						line.add(textHelper.numberFormatIntegerRoundNoSpace(Math.abs(x3 - qty)));
					}
					// lead time days (12)
					leadTimeDays = acvInfoMap.containsKey(entry.getKey()) ? acvInfoMap.get(entry.getKey()).getAverageDeliveryDays() : 0;
					line.add(leadTimeDays+"");
					// theoretical delivery date (13) & quantity left to receive (14)
					if(upcomingDeliveries.containsKey(entry.getKey()) && showUpcomming){
						cal = Calendar.getInstance();
						cal.setTime(upcomingDeliveries.get(entry.getKey()).getDate());
						cal.add(Calendar.DAY_OF_MONTH, leadTimeDays);
						line.add(dateHelper.formatYyyyMmDd(cal.getTime()));
						line.add(upcomingDeliveries.get(entry.getKey()).getQuantityLeftToGet()+"");
					}
					else {
						line.add("-");
						line.add("-");
					}
					// expected delivery date (15)
					line.add((upcomingDeliveries.containsKey(entry.getKey()) && showUpcomming)
							? dateHelper.formatYyyyMmDd(upcomingDeliveries.get(entry.getKey()).getDate())
							: "-");
					// final machine (16)
					line.add(firstMachines.containsKey(entry.getKey()) ? firstMachines.get(entry.getKey()).getMachineCode() : "-");
					table.add(line);
				}
				model.addAttribute("components", table);
				if (singleCode != null) {
					model.addAttribute("title", singleCode);
				} else {
					model.addAttribute("title", messageSource.getMessage("general.list", null, locale));
				}
			} else {
				// no file
				redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.choose.file", null, locale));
				return "redirect:/prodcomp/main";
			}

			return "prodcomp/view";
		} catch (OutOfHeapMemoryException er) {
			model.addAttribute("error", this.outOfMemoryMessage);
			return "prodcomp/view";
		} finally {
			// pending lock end
			this.lock.cancelComponentsLock();
		}
	}

	@RequestMapping("/makeplan")
	public String doMakePlan(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {
		
		// activity log
		if(doLog) {
			fileHelper.appendToTmpLog("makeplan");
		}
		
		try {
			if (model.asMap().get("days") == null) {
				return "redirect:/prodcomp/main";
			}

			boolean doReplenish = (boolean) model.asMap().get("replenish");
			int days = (int) model.asMap().get("days");
			File file = (File) model.asMap().get("file");

			if (file != null) {
				// file exist
				List<PlanLine> fileInfo = new ArrayList<>();
				// Map<String, Integer> fileInfo = new TreeMap<>();
				// READ FILE
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String line;
					String[] split;
					String order;
					String code;
					String description;
					String date;
					String clientName;
					String country;
					int quantity;
					double lineValue;

					int lineNo = 0;
					PlanLine planLine;

					while ((line = br.readLine()) != null) {
						lineNo++;
						split = line.split(";");
						// structure
						if (split.length != 8) {
							redirectAttrs.addFlashAttribute("error",
									messageSource.getMessage("error.bad.file.structure", null, locale) + ". "
											+ messageSource.getMessage("general.line", null, locale) + " " + lineNo
											+ ": " + line);
							br.close();
							file.delete();
							return "redirect:/prodcomp/main";
						}
						// values
						try {
							order = split[0].toUpperCase().trim();
							code = split[1].toUpperCase().trim();
							description = split[2].toUpperCase().trim();
							date = split[3].toUpperCase().trim();
							clientName = split[4].toUpperCase().trim();
							country = split[5].toUpperCase().trim();
							quantity = Math.abs(Integer.parseInt(split[6]));
							lineValue = Math.abs(Double.parseDouble(split[7].replace(',', '.')));

							planLine = new PlanLine();
							planLine.setOrder(order);
							planLine.setCode(code);
							planLine.setDescription(description);
							planLine.setDate(date);
							planLine.setClientName(clientName);
							planLine.setCountry(country);
							planLine.setQuantity(quantity);
							planLine.setLineValue(lineValue);
							fileInfo.add(planLine);
						} catch (NumberFormatException ex) {
							redirectAttrs.addFlashAttribute("error",
									messageSource.getMessage("error.mustbeanumber", null, locale) + ". "
											+ messageSource.getMessage("general.line", null, locale) + " " + lineNo
											+ ": " + line);
							br.close();
							file.delete();
							return "redirect:/prodcomp/main";
						}
					}
					br.close();
					file.delete();
				}
				
				// pending lock start
				if(this.lock.isComponentsLock()) {
					redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.procedure.pending", null, locale) + " (" + lock.getComponentsLockUser() + ")");
					return "redirect:/prodcomp/main";
				}
				else {
					this.lock.setComponentsLock(true);
					this.lock.setComponentsLockUser(userService.getAuthenticatedUser().getName());
				}
				
				// DO CALCULATION
				// get all boms
				Map<String, List<X3BomItem>> allBoms = x3Service.getAllBomPartsTopLevel("ATW");
				// get products info
				Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
				// get general stock of all products
				Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
				if (doReplenish) {
					// get in replenish (prod orders, buy orders) quantity
					Map<String, Integer> replenish = x3Service.findProductsInReplenish("ATW");
					// add stock + replenish
					for (Map.Entry<String, Integer> entry : replenish.entrySet()) {
						stock.put(entry.getKey(), stock.get(entry.getKey()) + entry.getValue());
					}
				}
				Map<String, Integer> initStock = new HashMap<>();
				// for working list of stock and expected delivery
				// copy when making initStock list and avcStock list, used to calculate current
				// store stock decrease
				Map<String, Integer> workStock = new HashMap<>();
				Map<String, Integer> workDelivery = new HashMap<>();
				for (Map.Entry<String, Integer> entry : stock.entrySet()) {
					initStock.put(entry.getKey(), entry.getValue());
					workStock.put(entry.getKey(), entry.getValue());
				}
				// get acv info
				List<X3ConsumptionProductInfo> acvInfo = x3Service.getListForConsumptionReport("ACV", "ATW");
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, days);
				Map<String, Double> expectedDelivery = x3Service.getExpectedDeliveriesByDate(cal.getTime(), "ATW");
				Map<String, Date> latestExpectedDeliveryDates = x3Service
						.getLatestExpectedDeliveryDateForCodeByDate(cal.getTime(), "ATW");
				// copy exp delivery to work delivery for calculation of coverage
				for (Map.Entry<String, Double> entry : expectedDelivery.entrySet()) {
					workDelivery.put(entry.getKey(), entry.getValue().intValue());
				}
				// set in acv info: stock + expected delivery
				Map<String, Integer> acvStock = new HashMap<>();
				for (X3ConsumptionProductInfo info : acvInfo) {
					if (expectedDelivery.containsKey(info.getProductCode())) {
						info.setStock(info.getStock() + expectedDelivery.get(info.getProductCode()).intValue());
					}
					acvStock.put(info.getProductCode(), info.getStock());
				}

				String code;
				// total requirement
				int qreq;
				// unit requirement
				double qunitreq;
				int qstock;
				int shortage;
				// max units that could be produced regarding shortages
				int maxProd;
				// to count max production for each requirement (while counting shortage cost
				// per req-code)
				int currentMaxProdForReq;
				double tmpCurrentShortageCost;

				Map<String, Integer> shortageList = new HashMap<>();
				Map<String, Double> shortageCost = new HashMap<>();

				// for stock vs. delivery part
				int wstck;
				int wdlv;
				int wstckAfter;
				int wdlvAfter;
				int componentCoverState;
				Map<Integer, String> coverCode = new HashMap<>();
				coverCode.put(PlanLine.COVER_STOCK, "prodcomp.cover.stock");
				coverCode.put(PlanLine.COVER_DELIVEY, "prodcomp.cover.delivery");
				coverCode.put(PlanLine.COVER_SHORTAGE, "prodcomp.cover.shortage");
				Date latestDeliveryDate;
				String latestDeliveryInfo;
				Calendar year3000 = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_YEAR, 3000);
				// /for stock vs. delivery part

				// calculate shortage
				for (PlanLine main : fileInfo) {
					// set init coverage for line as stock (optimistic) - for stock vs. delivery
					latestDeliveryDate = null;
					latestDeliveryInfo = "";
					componentCoverState = 1;
					main.setCoverLineState(PlanLine.COVER_STOCK);

					// FOR ALL LINES IN FILE
					// set in lines: components requirement info
					main.setRequirements(getCurrentAcvRequirementQuantitiesByStock(allBoms, main.getCode(), products,
							stock, main.getQuantity()));
					maxProd = (int) main.getQuantity();
					for (Map.Entry<String, Double> req : main.getRequirements().entrySet()) {
						// FOR ALL REQUIREMENTS IN PRODUCT
						// decrease acv stock and save info if shortage
						code = req.getKey();
						qreq = req.getValue().intValue();
						qunitreq = req.getValue() / main.getQuantity();
						qstock = acvStock.getOrDefault(req.getKey(), 0);
						shortage = qreq - qstock;
						currentMaxProdForReq = (int) (qstock / qunitreq);
						if (shortage > 0) {
							if (qstock / qunitreq < maxProd) {
								maxProd = (int) (qstock / qunitreq);
							}
							qstock = 0;
							main.addShortage(code, shortage);
							if (shortageList.containsKey(code)) {
								shortageList.put(code, shortageList.get(code) + shortage);
							} else {
								shortageList.put(code, shortage);
							}
						} else {
							qstock -= qreq;
						}

						// for shortage cost map
						if (currentMaxProdForReq < main.getQuantity()) {
							tmpCurrentShortageCost = (main.getQuantity() - currentMaxProdForReq) * main.getUnitPrice();
							if (shortageCost.containsKey(code)) {
								shortageCost.put(code, shortageCost.get(code) + tmpCurrentShortageCost);
							} else {
								shortageCost.put(code, tmpCurrentShortageCost);
							}
						}

						acvStock.put(code, qstock);

						// stock vs. delivery part
						wstck = workStock.getOrDefault(code, 0);
						wdlv = workDelivery.getOrDefault(code, 0);
						// initially set cover from stock (optimistic)
						componentCoverState = PlanLine.COVER_STOCK;
						wstckAfter = wstck - qreq;
						if (wstckAfter < 0) {
							workStock.put(code, 0);
							qreq = (-1) * wstckAfter;
							// set cover from delivery (optimistic)
							componentCoverState = PlanLine.COVER_DELIVEY;
						} else {
							workStock.put(code, wstckAfter);
						}

						if (componentCoverState == PlanLine.COVER_DELIVEY) {
							wdlvAfter = wdlv - qreq;
							if (wdlvAfter < 0) {
								workDelivery.put(code, 0);
								qreq = (-1) * wdlvAfter;
								// set cover to general shortage
								componentCoverState = PlanLine.COVER_SHORTAGE;
							} else {
								if (latestDeliveryDate == null) {
									latestDeliveryDate = latestExpectedDeliveryDates.getOrDefault(code,
											year3000.getTime());
									latestDeliveryInfo = dateHelper.formatYyyyMmDd(latestDeliveryDate) + " [" + code
											+ "]";
								} else if (latestDeliveryDate
										.before(latestExpectedDeliveryDates.getOrDefault(code, year3000.getTime()))) {
									latestDeliveryDate = latestExpectedDeliveryDates.getOrDefault(code,
											year3000.getTime());
									latestDeliveryInfo = dateHelper.formatYyyyMmDd(latestDeliveryDate) + " [" + code
											+ "]";
								}
								workDelivery.put(code, wdlvAfter);
								main.setLatestDeliveryInfo(latestDeliveryInfo);
								main.addInOrderCode(code);
							}
						}

						main.setCoverLineState(Integer.max(main.getCoverLineState(), componentCoverState));
						// /stock vs. delivery part
					}
					// stock vs. delivery - take min state from current component vs main coverage
					main.setMaxProduction(maxProd);
				}
				List<List<String>> table = new ArrayList<>();
				List<String> line;
				String shortages;
				String delivery;
				for (PlanLine main : fileInfo) {
					shortages = "";
					delivery = "";
					line = new ArrayList<>();
					line.add(main.getOrder());
					line.add(main.getCode());
					line.add(main.getDescription());
					line.add(products.containsKey(main.getCode()) ? products.get(main.getCode()).getGr2() : "XXX");
					line.add(main.getDate());
					line.add(main.getClientName());
					line.add(main.getCountry());
					line.add(main.getLineValue() + "");
					line.add(main.getShortageValue() + "");
					line.add(main.getQuantity() + "");
					line.add(main.getMaxProduction() + "");
					line.add(main.getShortageQuantity() + "");
					line.add(coverCode.get(main.getCoverLineState()));
					for (String dl : main.getInDeliverySet()) {
						delivery += dl + "; ";
					}
					line.add(delivery);
					line.add(main.getLatestDeliveryInfo());
					for (Map.Entry<String, Integer> sh : main.getShortage().entrySet()) {
						shortages += sh.getKey() + " (" + sh.getValue() + "); ";
					}
					line.add(shortages);
					table.add(line);
				}

				Map<String, X3DeliverySimpleInfo> upcomingDeliveries = x3Service
						.getFirstUpcomingDeliveriesMapByCodeAfterDate(new java.util.Date(), "ATW");
				Map<String, X3DeliverySimpleInfo> recentDeliveries = x3Service
						.getMostRecentDeliveriesMapByCodeBeforeDate(new java.util.Date(), "ATW");
				Map<String, Integer> stockQ = x3Service.findStockByState("Q", "ATW");
				Map<String, Integer> stockR = x3Service.findStockByState("R", "ATW");

				List<List<String>> shortageSummary = new ArrayList<>();
				List<String> shortageLine;
				String shCode;

				for (Map.Entry<String, Integer> entry : shortageList.entrySet()) {
					shortageLine = new ArrayList<>();
					shCode = entry.getKey();
					shortageLine.add(entry.getKey());
					shortageLine
							.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getDescription()
									: "XXX");
					shortageLine
							.add(products.containsKey(entry.getKey()) ? products.get(entry.getKey()).getGr2() : "XXX");
					shortageLine.add(initStock.getOrDefault(entry.getKey(), 0) + "");
					shortageLine.add(stockQ.containsKey(shCode) ? stockQ.get(shCode) + "" : 0 + "");
					shortageLine.add(stockR.containsKey(shCode) ? stockR.get(shCode) + "" : 0 + "");
					shortageLine.add((expectedDelivery.getOrDefault(entry.getKey(), 0.0)).intValue() + "");
					shortageLine.add(entry.getValue() + "");
					shortageLine.add(shortageCost.containsKey(shCode) ? shortageCost.get(shCode) + "" : "-");
					shortageLine.add(recentDeliveries.containsKey(shCode)
							? dateHelper.formatYyyyMmDd(recentDeliveries.get(shCode).getDate())
							: "-");
					shortageLine.add(recentDeliveries.containsKey(shCode)
							? recentDeliveries.get(shCode).getQuantityReceived() + ""
							: "-");
					shortageLine
							.add(recentDeliveries.containsKey(shCode) ? recentDeliveries.get(shCode).getSupplierCode()
									: "-");
					shortageLine
							.add(recentDeliveries.containsKey(shCode) ? recentDeliveries.get(shCode).getSupplierName()
									: "-");
					shortageLine.add(
							recentDeliveries.containsKey(shCode) ? recentDeliveries.get(shCode).getCountry() : "-");
					shortageLine.add(upcomingDeliveries.containsKey(shCode)
							? dateHelper.formatYyyyMmDd(upcomingDeliveries.get(shCode).getDate())
							: "-");
					shortageLine.add(upcomingDeliveries.containsKey(shCode)
							? upcomingDeliveries.get(shCode).getQuantityLeftToGet() + ""
							: "-");
					shortageLine.add(
							upcomingDeliveries.containsKey(shCode) ? upcomingDeliveries.get(shCode).getSupplierCode()
									: "-");
					shortageLine.add(
							upcomingDeliveries.containsKey(shCode) ? upcomingDeliveries.get(shCode).getSupplierName()
									: "-");
					shortageLine.add(
							upcomingDeliveries.containsKey(shCode) ? upcomingDeliveries.get(shCode).getCountry() : "-");

					shortageSummary.add(shortageLine);
				}
				model.addAttribute("days", days);
				model.addAttribute("shortage", shortageSummary);
				model.addAttribute("planlines", table);
				model.addAttribute("title", messageSource.getMessage("prodcomp.shortage.list", null, locale));
			} else {
				// no file
				redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.choose.file", null, locale));
				return "redirect:/prodcomp/main";
			}
			return "prodcomp/view";
		} catch (OutOfHeapMemoryException er) {
			model.addAttribute("error", this.outOfMemoryMessage);
			return "prodcomp/view";
		} finally {
			// pending lock end
			this.lock.cancelComponentsLock();
		}
	}

	/**
	 * 
	 * @param allBoms           all BOM info from X3
	 * @param code              main product code
	 * @param products          all X3 products general info
	 * @param stock             all X3 products stock
	 * @param quantityToProduce quantity of main product to calculate requirements
	 * @return
	 */
	private Map<String, Double> getCurrentAcvRequirementQuantitiesByStock(Map<String, List<X3BomItem>> allBoms,
			String itemCode, Map<String, X3Product> products, Map<String, Integer> stock, double quantityToProduce)
			throws OutOfHeapMemoryException {

		// check if we have main item on stock
		int mainStock = stock.getOrDefault(itemCode, 0);
		if (mainStock >= quantityToProduce) {
			mainStock -= quantityToProduce;
			quantityToProduce = 0;
		} else {
			quantityToProduce -= mainStock;
			mainStock = 0;
		}
		stock.put(itemCode, mainStock);
		// calculate requirements after stock check for main

		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);

		String code;
		double qtyReq;
		int currentStock;
		Map<String, Double> subMap;
		for (X3BomItem item : list) {
			code = item.getPartCode();
			qtyReq = item.getModelQuantity() * quantityToProduce;
			currentStock = stock.getOrDefault(code, 0);

			// skip if empty line
			if (products.get(code) == null) {
				continue;
			}
			// skip if no stock verify flag
			//if (!products.get(code).isVerifyStock()) {
			//	continue;
			//}
			
			// skip if FAR or WUR
			if (
					products.get(code).getGr2().equalsIgnoreCase("FAR") || 
					products.get(code).getGr2().equalsIgnoreCase("WUR") ||
					products.get(code).getGr2().equalsIgnoreCase("OPK")
					) {
				continue;
			}

			if (products.get(code).getCategory().equalsIgnoreCase("ACV")) {
				// add to requirements list if ACV
				if (resultMap.containsKey(code)) {
					resultMap.put(code, resultMap.get(code) + qtyReq);
				} else {
					resultMap.put(code, qtyReq);
				}
			} else {
				// for all production (AFV) codes:
				if (currentStock >= qtyReq) {
					// if there is stock for all current component req,
					// decrease stock and ignore subcomponents
					// IF IN PRODUCATION ORDERS ALSO SKIP...?
					currentStock -= qtyReq;
					continue;
				} else {
					qtyReq = qtyReq - currentStock;
					currentStock = 0;
				}
				// SAVE NEW STOCK INFO
				stock.put(code, currentStock);
				if (memoryService.getCurrentHeapUsageProc() > this.outOfMemoryThreshold) {
					throw new OutOfHeapMemoryException("Out of memory!");
				}
				subMap = getCurrentAcvRequirementQuantitiesByStock(allBoms, code, products, stock, qtyReq);
				for (Map.Entry<String, Double> entry : subMap.entrySet()) {
					if (resultMap.containsKey(entry.getKey())) {
						resultMap.put(entry.getKey(), resultMap.get(entry.getKey()) + entry.getValue());
					} else {
						resultMap.put(entry.getKey(), entry.getValue());
					}
				}
			}

		}
		return resultMap;
	}

	// for "make"
	private Map<String, Double> getComponentsQuantitiesMultilevel(Map<String, List<X3BomItem>> allBoms, String itemCode,
			Map<String, X3Product> products, boolean acvOnly) throws OutOfHeapMemoryException {
		Map<String, Double> resultMap = new TreeMap<>();

		List<X3BomItem> list = findBomPartsByParentCode(allBoms, itemCode);
		// x3Service.findBomPartsByParent("ATW", itemCode);

		String code;
		double qty;
		Map<String, Double> subMap;
		boolean toAdd;
		for (X3BomItem item : list) {

			code = item.getPartCode();
			qty = item.getModelQuantity();

			// skip if not verify stock
			if (products.get(code) != null && !products.get(code).isVerifyStock()) {
				continue;
			}

			// decide
			if (acvOnly) {
				if (products.get(code) == null) {
					continue;
				}
				if (products.get(code).getCategory().equalsIgnoreCase("ACV")) {
					toAdd = true;
				} else {
					toAdd = false;
				}
			} else {
				toAdd = true;
			}

			if (toAdd) {
				if (resultMap.containsKey(code)) {
					resultMap.put(code, resultMap.get(code) + qty);
				} else {
					resultMap.put(code, qty);
				}
			}
			if (memoryService.getCurrentHeapUsageProc() > this.outOfMemoryThreshold) {
				throw new OutOfHeapMemoryException("Out of memory!");
			}
			subMap = getComponentsQuantitiesMultilevel(allBoms, code, products, acvOnly);
			for (Map.Entry<String, Double> entry : subMap.entrySet()) {
				if (resultMap.containsKey(entry.getKey())) {
					resultMap.put(entry.getKey(),
							resultMap.get(entry.getKey()) + item.getModelQuantity() * entry.getValue());
				} else {
					resultMap.put(entry.getKey(), item.getModelQuantity() * entry.getValue());
				}
			}

		}
		return resultMap;
	}

	private List<X3BomItem> findBomPartsByParentCode(Map<String, List<X3BomItem>> allBoms, String itemCode) {
		List<X3BomItem> list = new ArrayList<>();
		for (Map.Entry<String, List<X3BomItem>> entry : allBoms.entrySet()) {
			if (entry.getKey().equals(itemCode)) {
				return entry.getValue();
			}
		}
		return list;
	}

	@RequestMapping(value = "/findchains", params = { "find" }, method = RequestMethod.POST)
	public String findChains(@Valid FormComponent formComponent, BindingResult bindingResult, Model model,
			Locale locale, RedirectAttributes redirectAttrs, HttpServletResponse response) {
		
		// activity log
		if(doLog) {
			fileHelper.appendToTmpLog("findchains");
		}
	
		try {
			// standard validation
			if (bindingResult.hasErrors()) {
				model.addAttribute("formFindComponents", new FormFindComponents());
				return "prodcomp/main";
			}

			// create a cookie
			Cookie startDateCookie = new Cookie("startDateChain", "" + formComponent.getStartDate().getTime());
			Cookie endDateCookie = new Cookie("endDateChain", "" + formComponent.getEndDate().getTime());
			// set time to live (seconds)
			startDateCookie.setMaxAge(60 * 60 * 24 * 31);
			endDateCookie.setMaxAge(60 * 60 * 24 * 31);
			// add cookie to response
			response.addCookie(startDateCookie);
			response.addCookie(endDateCookie);

			// check if component exist
			String component;
			String componentDescription;
			formComponent.setComponent(formComponent.getComponent().toUpperCase().trim());
			component = formComponent.getComponent();
			
			X3Product x3Product = x3Service.findProductByCode("ATW", component);
			//componentDescription = x3Service.findItemDescription("ATW", component);
			if (x3Product == null) {
				bindingResult.rejectValue("component", "error.no.such.product", "ERROR");
				model.addAttribute("formFindComponents", new FormFindComponents());
				return "prodcomp/main";
			}
			else {
				componentDescription = x3Product.getDescription();
			}
			// get component suppliers
			List<String> suppliers = x3Service.getComponentSuppliers(component, "ATW");

			// get sales orders
			List<X3SalesOrderLine> orders = x3Service.findOpenedSalesOrderLinesInPeriod(formComponent.getStartDate(),
					formComponent.getEndDate(), "ATW");
			if (orders.size() == 0) {
				bindingResult.rejectValue("startDate", "prodcomp.error.noordersfund", "ERROR");
				model.addAttribute("formFindComponents", new FormFindComponents());
				return "prodcomp/main";
			}

			// get BOM parts association [long]
			List<X3BomPart> allBoms = x3Service.getAllBomEntries("ATW");

			// prepare for quantities calculation loop
			List<List<X3BomPart>> allChains;
			List<X3BomPart> chain;

			X3BomPart initComponent = new X3BomPart();
			initComponent.setParentCode(component);
			initComponent.setPartCode(component);
			initComponent.setQuantityOfSubcode(1.0);
			initComponent.setQuantityOfSelf(1.0);

			// check if component is used in any BOM
			boolean found = false;
			for (X3BomPart part : allBoms) {
				if (initComponent.getParentCode().equalsIgnoreCase(part.getPartCode())) {
					found = true;
				}
			}
			if (!found) {
				bindingResult.rejectValue("component", "prodcomp.error.notusedinbom", "ERROR");
				model.addAttribute("formFindComponents", new FormFindComponents());
				return "prodcomp/main";
			}

			// prepare calculation variables
			allChains = new ArrayList<>();
			chain = new ArrayList<>();
			chain.add(initComponent);

			// pending lock start
			if(this.lock.isComponentsLock()) {
				redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.procedure.pending", null, locale) + " (" + lock.getComponentsLockUser() + ")");
				return "redirect:/prodcomp/main";
			}
			else {
				this.lock.setComponentsLock(true);
				this.lock.setComponentsLockUser(userService.getAuthenticatedUser().getName());
			}
			
			// just do it (BOM calculation)
			calculateBomChains(initComponent, chain, allChains, allBoms);
			List<List<X3BomPart>> finalChains = reverseLists(allChains);
			calculateChainsQuantities(finalChains);

			// get general stock info
			Map<String, Integer> generalStock = x3Service.findGeneralStockForAllProducts("ATW");

			// create sales objects and calculate chains
			List<SalesLineAndChains> salesObjects = new ArrayList<>();
			SalesLineAndChains object;
			int targetCodeDemand = 0;
			for (X3SalesOrderLine line : orders) {
				object = new SalesLineAndChains(line);
				for (List<X3BomPart> finalChain : finalChains) {
					if (finalChain.get(0).getParentCode().equalsIgnoreCase(line.getProductCode())) {
						fillCurrentStockInfo(finalChain, generalStock);
						object.addClonedAndCalculatedChain(finalChain);
						object.setBaseComponentQuantity(object.getTargetProductDemand(component));;
						// calculate demand based on upper components availability
						object.updateRelativeTargetDemand();
					}
				}
				if (object.getChains().size() > 0) {
					salesObjects.add(object);
					targetCodeDemand += object.getTargetProductDemand(component);
				}
			}
			updateMainLinesSupplyState(salesObjects);
			model.addAttribute("salesObjects", salesObjects);
			model.addAttribute("coverage", "[" + targetCodeDemand + "/" + generalStock.get(component) + "]");
			/*
			 * for(SalesLineAndChains obj: salesObjects) { S
			 * ystem.out.println(obj.getLine()); for(List<X3BomPart> schain:
			 * obj.getChains()){ S ystem.out.println(chainToString(schain)); } }
			 */
			
			model.addAttribute("startDate", formComponent.getStartDate());
			model.addAttribute("endDate", formComponent.getEndDate());
			model.addAttribute("component", component);
			model.addAttribute("componentDescription", componentDescription);
			model.addAttribute("title", component);
			model.addAttribute("suppliers", suppliers);
			model.addAttribute("category", x3Product.getCategory());
			return "prodcomp/view";
		} catch (OutOfHeapMemoryException er) {
			model.addAttribute("error", this.outOfMemoryMessage);
			return "prodcomp/view";
		} finally {
			// pending lock end
			this.lock.cancelComponentsLock();
		}
	}

	private void updateMainLinesSupplyState(List<SalesLineAndChains> salesObjects) {
		for(SalesLineAndChains line: salesObjects) {
			line.updateMainLineSupplyState();
		}
		
	}

	private void fillCurrentStockInfo(List<X3BomPart> finalChain, Map<String, Integer> generalStock) {
		for (X3BomPart part : finalChain) {
			part.setCurrentStock(new Double(generalStock.getOrDefault(part.getParentCode(), 0)));
		}
	}

	/**
	 * chain has to be in correct order (after REV, main to detail)
	 * 
	 * @param chains
	 */
	private void calculateChainsQuantities(List<List<X3BomPart>> chains) {
		double tmpMulti = 1.0;
		for (List<X3BomPart> chain : chains) {
			tmpMulti = 1.0;
			for (X3BomPart part : chain) {
				part.setQuantityOfSelf(part.getQuantityOfSelf() * tmpMulti);
				tmpMulti = part.getQuantityOfSubcode() * part.getQuantityOfSelf();
			}
		}
	}

	private List<List<X3BomPart>> reverseLists(List<List<X3BomPart>> allChains) {
		List<List<X3BomPart>> revLists = new ArrayList<>();
		List<X3BomPart> revList;
		for (int i = 0; i < allChains.size(); i++) {
			revList = new ArrayList<>();
			for (int j = allChains.get(i).size() - 1; j >= 0; j--) {
				revList.add(allChains.get(i).get(j));
			}
			revLists.add(revList);
		}
		return revLists;

	}

	/**
	 * 
	 * @param component X3BomPart to add its parents to list
	 * @param chain     list chain for current part
	 * @param allChains list to save finished chain lists
	 * @param allBoms   list of all BOM entries to check dependencies
	 */
	private boolean calculateBomChains(X3BomPart component, List<X3BomPart> chain, List<List<X3BomPart>> allChains,
			List<X3BomPart> allBoms) throws OutOfHeapMemoryException {

		boolean found = false;
		List<X3BomPart> localChain;

		for (X3BomPart part : allBoms) {
			if (component.getParentCode().equalsIgnoreCase(part.getPartCode())) {
				found = true;
				localChain = cloneBomPartList(chain);
				localChain.add(part);
				// memory check
				if (memoryService.getCurrentHeapUsageProc() > this.outOfMemoryThreshold) {
					throw new OutOfHeapMemoryException("Out of memory!");
				}
				if (!calculateBomChains(part, localChain, allChains, allBoms)) {
					allChains.add(localChain);
				}
			}
		}
		return found;
	}

	private List<X3BomPart> cloneBomPartList(List<X3BomPart> list) {
		List<X3BomPart> clone = new ArrayList<>();
		for (X3BomPart part : list) {
			clone.add(new X3BomPart(part));
		}
		return clone;
	}

	@RequestMapping(value = "/nobomcodes")
	public String noBomCodes(Model model) {
		model.addAttribute("formNoBomCodes", new FormNoBomCodes());
		return "/prodcomp/nobomcodesform";
	}

	@RequestMapping(value = "/nobomcodes", method = RequestMethod.POST)
	public String noBomCodesExec(@Valid FormNoBomCodes formNoBomCodes, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model) {

		// get list or single code to process
		List<NoBomCodeInfo> noBomCodes = new ArrayList<>();
		if (formNoBomCodes.getCode().trim().length() == 0) {
			noBomCodes = x3Service.getNoBomCodesListIncompleteObjects("ATW");
		} else {
			NoBomCodeInfo singleInfo = x3Service.getNoBomCodeIncompleteObject(formNoBomCodes.getCode().trim(), "ATW");
			if (singleInfo == null) {
				bindingResult.rejectValue("code", "error.no.such.product", "ERROR");
				return "prodcomp/nobomcodesform";
			}
			else {
				noBomCodes.add(singleInfo);
			}
			

		}
		Calendar today = Calendar.getInstance();

		// get sale for current period (year)
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MONTH, 0);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MONTH, 11);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		Map<String, X3SaleInfo> saleCurrent = x3Service.getSaleInfoInPeriod(startDate.getTime(), endDate.getTime(),
				"ATW");

		// get sale for previous period (year - 1)
		startDate.add(Calendar.YEAR, -1);
		endDate.add(Calendar.YEAR, -1);
		Map<String, X3SaleInfo> salePrevious = x3Service.getSaleInfoInPeriod(startDate.getTime(), endDate.getTime(),
				"ATW");

		// get sale for previous period (year - 2)
		startDate.add(Calendar.YEAR, -1);
		endDate.add(Calendar.YEAR, -1);
		Map<String, X3SaleInfo> sale2YearsBack = x3Service.getSaleInfoInPeriod(startDate.getTime(), endDate.getTime(),
				"ATW");

		// get GEODE stock
		Map<String, Integer> geoProduction = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_PRODUCTION);
		Map<String, Integer> geoReceptions = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_RECEPTIONS);
		Map<String, Integer> geoShipments = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_SHIPMENTS);

		int gsProd, gsRcp, gsShi;
		for (NoBomCodeInfo inf : noBomCodes) {
			inf.setSaleCurrentYear(
					(int) (saleCurrent.containsKey(inf.getCode()) ? saleCurrent.get(inf.getCode()).getValuePln() : 0));
			inf.setSalePreviousYear(
					(int) (salePrevious.containsKey(inf.getCode()) ? salePrevious.get(inf.getCode()).getValuePln()
							: 0));
			inf.setSale2YearsBack(
					(int) (sale2YearsBack.containsKey(inf.getCode()) ? sale2YearsBack.get(inf.getCode()).getValuePln()
							: 0));
			gsProd = geoProduction.containsKey(inf.getCode()) ? geoProduction.get(inf.getCode()) : 0;
			gsRcp = geoReceptions.containsKey(inf.getCode()) ? geoReceptions.get(inf.getCode()) : 0;
			gsShi = geoShipments.containsKey(inf.getCode()) ? geoShipments.get(inf.getCode()) : 0;
			inf.setStockGeode(gsProd + gsRcp + gsShi);
		}

		model.addAttribute("noBomCodes", noBomCodes);
		model.addAttribute("y0", today.get(Calendar.YEAR));
		model.addAttribute("y1", today.get(Calendar.YEAR) - 1);
		model.addAttribute("y2", today.get(Calendar.YEAR) - 2);

		return "/prodcomp/nobomcodesview";
	}

}
