package net.snacktank.httpserver.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.snacktank.httpserver.WebServer;

//OG code snippet here: http://www2.ic.uff.br/~michael/kr1999/2-application/2_08-webserver.htm

public class Get {
	
    String fileName;
	
	public Get() throws IOException {
    	try {
        	fileName = WebServer.tokenizedLine.nextToken();
    		fileName  = fileName.substring(1);
        	
    		//Load the default file if no file is requested.
    		if(fileName.equals("")) {
    			fileName = "index.html";
    		}
    		
            File file = new File(fileName);
                                
            int numOfBytes = (int) file.length();
            FileInputStream inFile = null;
    		
    		inFile  = new FileInputStream (fileName);
    	
    		byte[] fileInBytes = new byte[numOfBytes];
    		inFile.read(fileInBytes);

			WebServer.SendStatus(200, false);
    
    		//TODO add content-type back
    
    		WebServer.outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");

    		if(WebServer.method.equals("GET")) {
    			WebServer.outToClient.writeBytes("\r\n");
    			WebServer.outToClient.write(fileInBytes, 0, numOfBytes);
    		}
    		inFile.close();
    	} catch (IOException e) {
    		WebServer.SendStatus(404, true);
    	}
	}
}
