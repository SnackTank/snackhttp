package net.snacktank.httpserver.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Request {

	public HashMap<Integer, String> statusCodes = new HashMap<>();
	
	public BufferedReader inFromClient;
	public DataOutputStream outToClient;
	public Socket connectionSocket;
	public String HTTP = "HTTP/1.0 ";
	public String requestMessage;
	
	public Request() {
		WriteStatus();
	}
	
	public void sendStatus(int code, boolean ret) {
		try {
    		outToClient.writeBytes(HTTP + statusCodes.get(code) + "\r\n");
    		if(ret) {
        		outToClient.writeBytes("\r\n");
    		}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void WriteStatus() {
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
	

	
}
