package sbs.controller.weldlog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPClientConfig;
//import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// http://www.codejava.net/java-se/networking/ftp/java-ftp-file-download-tutorial-and-example
// https://www.google.pl/search?client=firefox-b-ab&dcr=0&ei=08kKWuy-Fs3GwALfybqIDg&q=easy+java+get+ftp+file&oq=easy+java+get+ftp+file&gs_l=psy-ab.3..33i22i29i30k1l10.268718.272644.0.272780.22.20.0.0.0.0.161.1552.10j6.16.0....0...1.1.64.psy-ab..6.16.1548...0j35i39k1j0i131k1j0i67k1j0i10k1j0i19k1j0i22i30i19k1j0i22i30k1.0.RQDFEUti8e0

@Controller
@RequestMapping("weldlog")
public class WeldLogController {
	
	@Autowired
	MessageSource messageSource;
	
	String server;
	int port;
	int bufferSize;
	String user;
	String pass;
	String remoteFile;
	String destinationFile;
    File downloadFile;
    BufferedReader in;
    String ftpUrl;
    String ftpSecretUrl;
	
    @Autowired
    public WeldLogController(Environment env) {
    	server = env.getRequiredProperty("general.casarini.ftp.server");
    	port = Integer.parseInt(env.getRequiredProperty("general.casarini.ftp.port"));
    	bufferSize = Integer.parseInt(env.getRequiredProperty("general.casarini.ftp.buffersize"));
    	user = env.getRequiredProperty("general.casarini.ftp.user");
    	pass = env.getRequiredProperty("general.casarini.ftp.pass"); 
    	remoteFile = env.getRequiredProperty("general.casarini.ftp.remotefile"); 
    	destinationFile = env.getRequiredProperty("general.casarini.ftp.destinationfile");
    	ftpUrl = "ftp://" + user + ":" + pass + "@" + server + "/" + remoteFile;
    	ftpSecretUrl = "ftp://" + user + "@" + server + "/";
    }
	
    
    @RequestMapping("/list")
    public String view(Model model, Locale locale){
		model.addAttribute("ftpSecretUrl", ftpSecretUrl);
		try{
			// GET FILE
			URL url = new URL(ftpUrl) ;
			URLConnection conn = url.openConnection() ;
			InputStream inputStream = conn.getInputStream() ;
			FileOutputStream outputStream = new FileOutputStream(destinationFile);
			byte[] buffer = new byte[bufferSize];
			int bytesRead = -1 ;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead) ;
			}
			outputStream.close() ;
			inputStream.close() ;
			
            // PROCESS FILE
            in = new BufferedReader(new FileReader(destinationFile));
			String line;
			int lineNo;
			String warningLines = "";
			WeldLogLine weldLine;
			ArrayList<WeldLogLine> okLines = new ArrayList<WeldLogLine>();
			List<String> headers;
			// GET TITLES
			if((line = in.readLine()) != null){
				line = line.replace("\"","");
				if (line.split(",").length != 29){
					throw new FtpException();
				}
				else{
					headers = Arrays.asList(line.split(","));
					model.addAttribute("headers", headers);
				}
			}
			// GET LINES
			lineNo = 0;
			while ((line = in.readLine()) != null) {
				lineNo++;
				line = line.replace("\"","");
				if (line.split(",").length != 29){
						warningLines+= lineNo + ", "; 
				}
				else{
					weldLine = new WeldLogLine();
					weldLine.setFields(Arrays.asList(line.split(",")));
					okLines.add(weldLine);
				}
			}
			// pass warning if exist
			if(warningLines.length() > 0){
				model.addAttribute("warning",
	        			messageSource.getMessage("error.ftp.badfilestructure",null, locale)
	        			+ " " + server +":"+port + " [" + remoteFile+ "]: #" + warningLines
	        			);
			}

			model.addAttribute("okLines", okLines);
			in.close();
			
			// DELETE FILE		
			downloadFile = new File(destinationFile);
			downloadFile.delete();
			
		}
		catch (FtpException ex){
			model.addAttribute("error",
        			messageSource.getMessage("error.ftp.badfilestructure",null, locale)
        			+ " " + server +":"+port + " [" + remoteFile+ "]"
        			);
		}
		catch (Exception ex){
			model.addAttribute("error",
        			messageSource.getMessage("error",null, locale)
        			+ " " + server +":"+port + " [" + remoteFile+ "]: " + ex.getMessage()
        			);
		}
    	
    	return "weldlog/list";
    }
    
    
    @RequestMapping("/ftpclient")
    public String ftpClient(Model model, Locale locale){
    	
        FTPClient ftpClient = new FTPClient();
        try {
        	//add server address for URL creation
			model.addAttribute("url", server);
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
			
			String line;
			WeldLogLine weldLine;
			ArrayList<WeldLogLine> okLines = new ArrayList<WeldLogLine>();
			List<String> headers;
			
			// GET TITLES
			if((line = in.readLine()) != null){
				line = line.replace("\"","");
				if (line.split(",").length != 29){
					model.addAttribute("error",
		        			messageSource.getMessage("error.ftp.badfilestructure",null, locale)
		        			+ " " + server +":"+port + " [" + remoteFile+ "]"
		        			);
				}
				else{
					headers = Arrays.asList(line.split(","));
					model.addAttribute("headers", headers);
				}
			}
			
			// GET LINES
			
			while ((line = in.readLine()) != null) {
				line = line.replace("\"","");
				if (line.split(",").length != 29){
					model.addAttribute("warning",
		        			messageSource.getMessage("error.ftp.badfilestructure",null, locale)
		        			+ " " + server +":"+port + " [" + remoteFile+ "]"
		        			);
				}
				else{
					weldLine = new WeldLogLine();
					weldLine.setFields(Arrays.asList(line.split(",")));
					okLines.add(weldLine);
				}
			}
			model.addAttribute("okLines", okLines);
			in.close();
			// DELETE FILE			
			downloadFile.delete();
			
        } catch (IOException ex) {
        	model.addAttribute("error",
        			messageSource.getMessage("error.ftp.exception",null, locale)
        			+ " " + server +":"+port + " [#" +ftpClient.getReplyCode()+"]"
        			);
            ex.printStackTrace();
            model.addAttribute("warning",ex.getMessage());
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
}
