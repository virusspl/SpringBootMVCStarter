package sbs.controller.weldlog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.controller.nameplates.NameplatesErrorLine;

// http://www.codejava.net/java-se/networking/ftp/java-ftp-file-download-tutorial-and-example
// https://www.google.pl/search?client=firefox-b-ab&dcr=0&ei=08kKWuy-Fs3GwALfybqIDg&q=easy+java+get+ftp+file&oq=easy+java+get+ftp+file&gs_l=psy-ab.3..33i22i29i30k1l10.268718.272644.0.272780.22.20.0.0.0.0.161.1552.10j6.16.0....0...1.1.64.psy-ab..6.16.1548...0j35i39k1j0i131k1j0i67k1j0i10k1j0i19k1j0i22i30i19k1j0i22i30k1.0.RQDFEUti8e0

@Controller
@RequestMapping("weldlog")
public class WeldLogController {
	
	@Autowired
	MessageSource messageSource;
	
	String server;
	int port;
	String user;
	String pass;
	String remoteFile;
	String destinationFile;
    File downloadFile;
    BufferedReader in;
	
    @Autowired
    public WeldLogController(Environment env) {
    	server = env.getRequiredProperty("general.casarini.ftp.server");
    	port = Integer.parseInt(env.getRequiredProperty("general.casarini.ftp.port"));
    	user = env.getRequiredProperty("general.casarini.ftp.user");
    	pass = env.getRequiredProperty("general.casarini.ftp.pass"); 
    	remoteFile = env.getRequiredProperty("general.casarini.ftp.remotefile"); 
    	destinationFile = env.getRequiredProperty("general.casarini.ftp.destinationfile"); 
    }
	
    @RequestMapping("/list")
    public String view(Model model, Locale locale){
    	
        FTPClient ftpClient = new FTPClient();
        try {
 
        	ftpClient.setAutodetectUTF8(true);
        	// CONNECT
            ftpClient.connect(server, port);
            if(ftpClient.getReplyCode()!=220){
            	model.addAttribute("error",
            			messageSource.getMessage("error.ftp.not.connected",null, locale)
            			+ " " + server +":"+port + "[#" +ftpClient.getReplyCode()+"]"
            			);
            	throw new FtpException();
            }
            // LOGIN
            ftpClient.login(user, pass);
            if(ftpClient.getReplyCode()!=230){
            	model.addAttribute("error",
            			messageSource.getMessage("error.ftp.not.authenticated",null, locale)
            			+ " " + server +":"+port + " ["+user+":######]"
            			+ " [#" +ftpClient.getReplyCode()+"]"
            			);
            	throw new FtpException();
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.enterLocalActiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // RETRIEVE FILE
            downloadFile = new File(destinationFile);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
            ftpClient.retrieveFile(remoteFile, outputStream1);
            outputStream1.close();
            if(ftpClient.getReplyCode()!=226){
            	model.addAttribute("error",
            			messageSource.getMessage("error.file.not.found",null, locale)
            			+ " " + server +":"+port + " [" + remoteFile + "]" 
            			+ " [#" +ftpClient.getReplyCode()+"]"
            			);
            	throw new FtpException();
            }
            /*
            // APPROACH #2: using InputStream retrieveFileStream(String)
			String remoteFile2 = file; File downloadFile2 = new File("C:/2.index.php"); OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2)); InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2); System.out.println("#2 retrieve: "+ftpClient.getReplyCode()); byte[] bytesArray = new byte[20000]; int bytesRead = -1; while ((bytesRead = inputStream.read(bytesArray)) != -1) { outputStream2.write(bytesArray, 0, bytesRead); } success = ftpClient.completePendingCommand(); System.out.println("#2 pending: "+ftpClient.getReplyCode()); outputStream2.close(); inputStream.close();
            */ 
            
            // PROCESS FILE
            in = new BufferedReader(new FileReader(destinationFile));
			
			String line, result;
			List<String> split;
			long i = 1;
			
			result = "";
			while ((line = in.readLine()) != null) {
				//if (line.split(",").length != 7){
				// out of while: ArrayList<NameplatesErrorLine> errorLines = new ArrayList<>();
				// errorLines.add(new NameplatesErrorLine(i,line));
				//}
				
				result += line + "<br/>";
				
				i++;
			}
			in.close();
			downloadFile.delete();
			model.addAttribute("result", result);
        } catch (IOException ex) {
        	model.addAttribute("error",
        			messageSource.getMessage("error.ftp.exception",null, locale)
        			+ " " + server +":"+port + " [#" +ftpClient.getReplyCode()+"]"
        			);
            ex.printStackTrace();
        } catch (FtpException ignore) {
        	
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    	
    	
    	
    	
    	
    	
    	
    	return "weldlog/list";
    }
    /*
	@RequestMapping("/list")
	public String list(Model model, Locale locale) throws FileNotFoundException {
		ArrayList<NameplatesLogLine> logLines = new ArrayList<>();
		ArrayList<NameplatesErrorLine> errorLines = new ArrayList<>();
		ArrayList<NameplatesLogLine> backlistLog;
		ArrayList<NameplatesErrorLine> backlistError;
		try {
			BufferedReader in = new BufferedReader(new FileReader(rfidPath));
			
			String line;
			List<String> split;
			NameplatesLogLine logLine;
			long i = 1;
			
			while ((line = in.readLine()) != null) {
				if (line.split(",").length != 7){
					errorLines.add(new NameplatesErrorLine(i,line));
				}
				else{
					logLine= new NameplatesLogLine();
					logLine.setLineNo(i);
					line = line.replace("\"","");
					split = Arrays.asList(line.split(","));
					logLine.setActionDate(split.get(0));
					logLine.setAction(split.get(1));
					logLine.setRfid(split.get(2));
					logLine.setProduct(split.get(3));
					logLine.setDate(split.get(4));
					logLine.setFlag(split.get(5));
					logLine.setComment(split.get(6));
					logLines.add(logLine);
				}
				i++;
			}
			in.close();
			
			backlistLog = new ArrayList<>();
			for (ListIterator<NameplatesLogLine> iterator = logLines.listIterator(logLines.size()); iterator.hasPrevious();) {
				backlistLog.add(iterator.previous());
			}

			backlistError = new ArrayList<>();
			for (ListIterator<NameplatesErrorLine> iterator = errorLines.listIterator(errorLines.size()); iterator.hasPrevious();) {
				backlistError.add(iterator.previous());
			}
			
			model.addAttribute("logLines", backlistLog);
			model.addAttribute("errorLines", backlistError);
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException(
					messageSource.getMessage("error.file.not.found",null, locale)
					+ " "
					+ ex.getMessage());
		} catch (IOException ignore){}
		return "nameplates/list";
	}
	*/

}
