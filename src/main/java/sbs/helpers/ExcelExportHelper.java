package sbs.helpers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelExportHelper extends AbstractXlsView {
	
	DateHelper dateHelper;
	
	public ExcelExportHelper() {
		dateHelper = new DateHelper();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// get contents
		ExcelContents contents = (ExcelContents) model.get("contents");
		
		//filename
		if(contents.getFileName() == null){
			contents.setFileName("adrp.excel.export");
		}
		response.setHeader("Content-Disposition", "attachment;filename=\"" + contents.getFileName() + "\"");
		
		// create workbook and sheet
		if(contents.getSheetName() == null){
			contents.setSheetName("export");
		}
		
		CreationHelper creationHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet(contents.getSheetName());
		Cell cell;
		CellStyle cellStyle;
		
		
		// create header
		Row header = sheet.createRow(0);
		int counter = 0;
		// header style
		cellStyle = workbook.createCellStyle();
		
		// titles
		for(String title: contents.getHeaders()){
			//header.createCell(counter).setCellValue(title);
			cell = header.createCell(counter);
			cell.setCellValue(title);
			cell.setCellStyle(cellStyle);
			counter++;
		}
		
		// create values
		Row row;
		counter = 1;
		// date style
		cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
		
		for (ArrayList<Object> rowValues : contents.getValues()) {
			row = sheet.createRow(counter++);
			for(int i = 0; i<rowValues.size(); i++){
				if(rowValues.get(i) instanceof Integer){
					row.createCell(i).setCellValue((Integer)rowValues.get(i));
				}
				else if(rowValues.get(i) instanceof Long){
					row.createCell(i).setCellValue((Long)rowValues.get(i));
				}
				else if(rowValues.get(i) instanceof Float){
					row.createCell(i).setCellValue((Float)rowValues.get(i));
				}
				else if(rowValues.get(i) instanceof Double){
					row.createCell(i).setCellValue((Double)rowValues.get(i));
				}
				else if (rowValues.get(i) instanceof Boolean){
					row.createCell(i).setCellValue((Boolean)rowValues.get(i));
				}
				else if (rowValues.get(i) instanceof Date){
					//row.createCell(i).setCellValue(dateHelper.formatYyyyMmDd());
					cell = row.createCell(i);
					cell.setCellValue((Date)rowValues.get(i));
					cell.setCellStyle(cellStyle);
					
				}
				else if (rowValues.get(i) instanceof Timestamp){
					//row.createCell(i).setCellValue(dateHelper.formatYyyyMmDd((Timestamp)rowValues.get(i)));
					cell = row.createCell(i);
					cell.setCellValue((Timestamp)rowValues.get(i));
					cell.setCellStyle(cellStyle);
				}
				else{
					row.createCell(i).setCellValue(""+rowValues.get(i));
				}
			}
		}
	}

}