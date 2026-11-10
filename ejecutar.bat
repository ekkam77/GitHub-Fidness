@echo off
cd /d "%~dp0"
java -jar dist\FidnessAvance2.jar
if errorlevel 1 pause
