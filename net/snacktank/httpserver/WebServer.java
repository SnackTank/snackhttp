package net.snacktank.httpserver;

import java.io.*;
import java.net.*;
import java.util.*;

import net.snacktank.httpserver.request.*;

//specifications        http://www.rfc-editor.org/rfc/rfc1945

/* TODO
 * DELETE
 * LINK
 * UNLINK
 * MULTITHREADING
 */

public class WebServer {
	public static ServerSocket listenSocket;
	public static Socket connectionSocket;
	public static BufferedReader inFromClient;
	public static DataOutputStream outToClient;
	public static StringTokenizer tokenizedLine;
	public static String method;
	public static String requestMessageLine;
	public static boolean putEnable = false;
	public static boolean postEnable = true;
	public static int port = 8080;
	public static HashMap<Integer, String> statusCodes;
	public static String HTTP = "HTTP/1.0 ";
	
	public static void main(String argv[]) throws Exception  {
		
		statusCodes = new HashMap<>();
		WriteStatus();
    	while(true) {
        //Start listening on a port.
        listenSocket = new ServerSocket(port);
        connectionSocket = listenSocket.accept();

        //Read the request
        InputStream in = connectionSocket.getInputStream();
        inFromClient = new BufferedReader(new InputStreamReader(in));
        //Send data back to client.
        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        requestMessageLine = inFromClient.readLine();

        tokenizedLine = new StringTokenizer(requestMessageLine);
        method = tokenizedLine.nextToken();
        //Check what type of witchcraft there is.
        switch (method) {
        	case "GET":
            new Get();
        		break;
        	case "HEAD":
        	new Get();
        		break;
        	case "POST":
        		if(postEnable) {
            		new Post();
        		} else {
        			SendStatus(503, true);
        		}
        		break;
        	case "PUT":
        		if(putEnable) {
        			new Post();
        		} else {
        			SendStatus(503, true);
        		}
        		break;
        }
        if(connectionSocket != null) {
            connectionSocket.close();
        }
        listenSocket.close();
    	} 
    	
   }
	public static void WriteStatus() {
		//2xx
		statusCodes.put(200, "200 OK");
		statusCodes.put(201, "201 CREATED");
		statusCodes.put(202, "202 ACCEPTED");
		statusCodes.put(204, "204 NO CONTENT");
		//3xx
		statusCodes.put(301, "301 MOVED PERMANENTLY");
		statusCodes.put(302, "302 MOVED TEMPORARILY");
		statusCodes.put(304, "304 NOT MODIFIED");
		//4xx
		statusCodes.put(400, "400 BAD REQUEST");
		statusCodes.put(401, "401 UNAUTHORIZED");
		statusCodes.put(403, "403 FORBIDDEN");
		statusCodes.put(404, "404 NOT FOUND");
		//5xx
		statusCodes.put(500, "500 INTERNAL SERVER ERROR");
		statusCodes.put(501, "501 NOT IMPLEMENTED");
		statusCodes.put(502, "502 BAD GATEWAY");
		statusCodes.put(503, "503 SERVICE UNAVAILABLE");
	}
	
	public static void SendStatus(int code, boolean ret) {
		try {
    		WebServer.outToClient.writeBytes(HTTP + statusCodes.get(code) + "\r\n");
    		if(ret) {
        		WebServer.outToClient.writeBytes("\r\n");
    		}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}