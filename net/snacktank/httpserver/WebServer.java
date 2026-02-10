package net.snacktank.httpserver;

import java.io.*;
import java.net.*;
import java.util.*;

//OG code snippet here: http://www2.ic.uff.br/~michael/kr1999/2-application/2_08-webserver.htm
//specifications        http://www.rfc-editor.org/rfc/rfc1945

/* TODO
 * DELETE
 * LINK
 * UNLINK
 */

public class WebServer {
	public static void main(String argv[]) throws Exception  {
    	while(true) {
    	boolean enablePUT = false;
        String requestMessageLine;
        String fileName;
        
        String fourofour = "<html>"
        		+ "<h1>404: NOT FOUND</h1><hr>"
        		+ "<p>HTTP/1.0 | Hyperchat Delivery System 0.1 <br> The file requested could not be found!</p></html>";
        int fourofourLength = fourofour.length();
        
        //Start listening on a port.
        ServerSocket listenSocket = new ServerSocket(8080);
        Socket connectionSocket = listenSocket.accept();

        //Read the request
        InputStream in = connectionSocket.getInputStream();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
        //Send data back to client.
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        requestMessageLine = inFromClient.readLine();

        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
        String method = tokenizedLine.nextToken();
        //Check what type of witchcraft there is.
        if (method.equals("GET") || method.equals("HEAD")){
        	
        	/*Split the request into multiple stings.
        	* Id est:
        	* "I will find a bridge" into
        	* "I", "will", "find", "a", "bridge"
        	* Or
        	* "GET /obama.txt HTTP/1.0"
        	* "GET", "/obama.txt", "HTTP/1.0"
        	*/
        	
        	//Take the file requested and separate it from the rest of the request
        	fileName = tokenizedLine.nextToken();
        	if (fileName.startsWith("/") == true ) {
        		fileName  = fileName.substring(1);
        	
                    File file = new File(fileName);
                                        
                    int numOfBytes = (int) file.length();
                    FileInputStream inFile = null;
                    try {
                    	inFile  = new FileInputStream (fileName);
                    	

                    byte[] fileInBytes = new byte[numOfBytes];
                    inFile.read(fileInBytes);

                    outToClient.writeBytes("HTTP/1.0 200 OKAY\r\n");
                    
                    //Program crashes if the second one is null. And I'm too lazy to figure out why.
                    outToClient.writeBytes(fileExt(fileName, "no"));
                    
                    outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");


                    if(method.equals("GET")) {
                        outToClient.writeBytes("\r\n");
                    	outToClient.write(fileInBytes, 0, numOfBytes);
                    }
                    connectionSocket.close();
                    } catch (IOException e) {
                        outToClient.writeBytes("HTTP/1.0 404 NOT FOUND\r\n");
                        outToClient.writeBytes("Content-Type: text/html\r\n");
                        outToClient.writeBytes("Content-Length: " + fourofourLength + "\r\n");
                        if(method.equals("GET")) {
                            outToClient.writeBytes("\r\n");
                        	outToClient.writeBytes(fourofour);
                        }
                        connectionSocket.close();
                    	//e.printStackTrace();
                    }
                   }
        } 
        if(method.equals("POST")) {

        	int bodyLength = 0;
        	fileName = tokenizedLine.nextToken();
        	if (fileName.startsWith("/post/") == true ) {
        		fileName  = fileName.substring(1);
        		
        		String content;

        		//Read request from client, and figure out length of body for parsing.
        		
        		while((content = inFromClient.readLine()) != null) {
        			/* TODO change file extension with content-type
        			if(content.startsWith("Content-Type:")) {
        				System.out.println(content);
        			}
        			*/
        			if(content.startsWith("Content-Length:")) {
        				//Find out how long the body is
        				bodyLength = Integer.parseInt(content.replaceAll("[^0-9]", ""));
        			}
        			if(content.isEmpty()) {
        				//Headers are now over, time for the body
        				break;
        			}
        		}

        		//TODO Support any binary file
        		//Takes the BufferedInput into an array of chars to be stored.
        		String date;
        		char[] bodyChars = new char[bodyLength];
				int readChars = 0;
				while (readChars < bodyLength) {
				    int r = inFromClient.read(bodyChars, readChars, bodyLength - readChars);
				    readChars += r;
				    if(r == -1) {
		            	outToClient.writeBytes("HTTP/1.0 500 INTERNAL SERVER ERROR\r\n");
		            	outToClient.writeBytes("\r\n");
		            	connectionSocket.close();
		            	return;
				    }
				}
				String toWrite = new String(bodyChars);
				try {
					date = Long.toString(System.currentTimeMillis());
					FileWriter writer = new FileWriter("uploads/request-" + date + ".txt");
	        		writer.write(toWrite);
	            	writer.close();

				} catch(FileNotFoundException e) {
	            	outToClient.writeBytes("HTTP/1.0 400 BAD REQUEST\r\n");
	            	outToClient.writeBytes("\r\n");
	            	connectionSocket.close();
	            	return;
				}

        		/*
        		for(int i = 0; i < bodyLength; i++) {
        			System.out.print(bodyChars[i]);
        		}
        		//To print the body of the POST request
        		*/
				//To tell the client that the file has been created!
        		outToClient.writeBytes("HTTP/1.0 201 CREATED\r\n"); 		
        		outToClient.writeBytes("Location: /uploads/request-" + date + ".txt\r\n");
        		outToClient.writeBytes("\r\n");
        	} else {
            	outToClient.writeBytes("HTTP/1.0 400 BAD REQUEST\r\n");
            	outToClient.writeBytes("\r\n");
        	}
            connectionSocket.close();
        } 
        //Literally the same function as POST just more explicit yo.
        if(method.equals("PUT") && enablePUT) {
        	int bodyLength = 0;
        	fileName = tokenizedLine.nextToken();
        	if (fileName.startsWith("/") == true ) {
        		fileName  = fileName.substring(1);
        		
        		String content;

        		//Read request from client, and figure out length of body for parsing.
        		
        		while((content = inFromClient.readLine()) != null) {
        			/* TODO change file extension with content-type
        			if(content.startsWith("Content-Type:")) {
        				System.out.println(content);
        			}
        			*/
        			if(content.startsWith("Content-Length:")) {
        				//Find out how long the body is
        				bodyLength = Integer.parseInt(content.replaceAll("[^0-9]", ""));
        			}
        			if(content.isEmpty()) {
        				//Headers are now over, time for the body
        				break;
        			}
        		}

        		//TODO Support any binary file
        		//Takes the BufferedInput into an array of chars to be stored.
        		char[] bodyChars = new char[bodyLength];
				int readChars = 0;
				while (readChars < bodyLength) {
				    int r = inFromClient.read(bodyChars, readChars, bodyLength - readChars);
				    readChars += r;
				    if(r == -1) {
		            	outToClient.writeBytes("HTTP/1.0 500 INTERNAL SERVER ERROR\r\n");
		            	outToClient.writeBytes("\r\n");
		            	connectionSocket.close();
		            	return;
				    }
				}
				String toWrite = new String(bodyChars);
				try {
					FileWriter writer = new FileWriter(fileName);
	        		writer.write(toWrite);
	            	writer.close();

				} catch(FileNotFoundException e) {
	            	outToClient.writeBytes("HTTP/1.0 400 BAD REQUEST\r\n");
	            	outToClient.writeBytes("\r\n");
	            	connectionSocket.close();
	            	return;
				}

        		/*
        		for(int i = 0; i < bodyLength; i++) {
        			System.out.print(bodyChars[i]);
        		}
        		//To print the body of the POST request
        		*/
				//To tell the client that the file has been created!
        		outToClient.writeBytes("HTTP/1.0 201 CREATED\r\n"); 		
        		outToClient.writeBytes("Location: /" + fileName + "\r\n");
        		outToClient.writeBytes("\r\n");
        	} else {
            	outToClient.writeBytes("HTTP/1.0 400 BAD REQUEST\r\n");
            	outToClient.writeBytes("\r\n");
        	}
        }
        if (!method.equals("GET")  && 
        	!method.equals("HEAD") &&
        	!method.equals("POST") &&
        	!method.equals("PUT")) {
        	outToClient.writeBytes("HTTP/1.0 501 NOT IMPLEMENTED\r\n");
        	outToClient.writeBytes("\r\n");
        } else if(method.equals("PUT") && !enablePUT) {
        	outToClient.writeBytes("HTTP/1.0 503 SERVICE UNAVAILABLE\r\n");
        	outToClient.writeBytes("\r\n");
        }
        if(connectionSocket != null) {
            connectionSocket.close();
        }
        listenSocket.close();
    	} 
    	
   }
	
	public static String fileExt(String name, String ext) {

		if (name.endsWith(".jpg")) {
			if(ext.equals("no")) {
				return "Content-Type: image/jpeg\r\n";
			} else {
				return ".jpeg";
			}
		}
		if (name.endsWith(".gif")) {
			if(ext.equals("no")) {
				return "Content-Type: image/gif\r\n";
			} else {
				return ".gif";
			}
		}
		if(name.endsWith(".html")) {
			if(ext.equals("no")) {
				return "Content-Type: text/html\r\n";
			} else {
				return ".html";
			}
		}
		return "Content-Type: text\r\n";
	}
}
