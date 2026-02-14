echo Creating snackhttp server!
mkdir out/
javac -d out/ net/snacktank/httpserver/*.java
cd out/ 
cp ../MANIFEST.MF .
jar -cfm WebServer.jar MANIFEST.MF net/snacktank/httpserver/*.class net/snacktank/httpserver/request/*.class
