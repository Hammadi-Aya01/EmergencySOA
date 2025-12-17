@echo off
echo Starting SOAP Server...
start powershell.exe -Command "mvn clean compile exec:java -P soap-server"

echo Starting REST Server...
timeout /t 5
start powershell.exe -Command "mvn clean compile exec:java -P rest-server"

echo Starting GUI Application...
timeout /t 5
mvn javafx:run