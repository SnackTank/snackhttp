# SnackTank's Hypertext Transfer Protocol Server
A stupid simple HTTP/1.0 server written in Java <br>
Context? There is no context, it exist because it does. <br>
This project was designed for Java 1.8.0 <br>

## How to compile
There are two diffrent ways, either A manually, or B with the provided script. <br>
Here's the manual way if you don't trust my script: <br>
I. Please go to the directory that contains net/ <br>
You're file manager should show something along this file structure: <br>
'LICENSE MANIFEST.MF net README.md' <br>
And go ahead and create an 'out' directory: mkdir out/ (Unix example) <br>
II. run 'javac -d out/ net/snacktank/httpserver/\*.java' <br>
Now the server can be ran by 'java net/snacktank/httpserver/WebServer' but we want a Java Archive (JAR) file. <br>
III. run 'jar -cfm WebServer.jar MANIFEST.MF net/snacktank/httpserver/\*.class net/snacktank/httpserver/request/*.class' <br>
This sequence should create a JAR file that can be placed anywhere you want! 

## How to Run
It's pretty simple just do: <br>
'java -jar WebServer.jar' <br>

## Behaviors 
POST & PUT are disabled by default. You will need to enable them in the source code. <br>
If POST is enabled the request will be stored as plain ASCII in a request/ sub-folder. <br>
If no file or '/' is requested by GET, the server will return 'index.html'

## Features
I.   GET  <br>
II.  HEAD <br>
III. POST <br>
IV.  PUT  <br>

## TODO
I. [ ] LINK <br>
II. [ ] UNLINK <br>
III. [ ] DELETE <br>
IV. [ ] AUTHENTICATION 

## License
MIT <br>
Copyright MMXXVI SnackTank
