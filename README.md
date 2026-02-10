# SnackTank's Hypertext Transfer Protocol Server
A stupid simple HTTP/1.0 server written in Java <br>
Context? There is no context, it exist because it does. <br>

## How to compile
I. Please go to the /net/ directory <br>
II. run 'javac net/snacktank/httpserver/WebServer.java' <br>
Now the server can be ran by 'java net/snacktank/httpserver/WebServer' but we want a Java Archive (JAR) file. <br>
III. run 'jar -cfm WebServer.jar MANIFEST.MF net/snacktank/httpserver/WebServer.class' <br>
This sequence should create a JAR file that can be placed anywhere you want! 

## How to Run
It's pretty simple just do: <br>
'java -jar WebServer.jar'

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
