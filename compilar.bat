@echo off
cd /d "%~dp0"
if not exist build\classes mkdir build\classes
if not exist dist mkdir dist
javac -encoding UTF-8 -source 8 -target 8 -d build\classes src\*.java
if errorlevel 1 goto error
jar cfm dist\FidnessAvance2.jar manifest.mf -C build\classes .
if errorlevel 1 goto error
echo Compilacion terminada.
pause
exit /b 0
:error
echo No se pudo compilar. Revise que tenga un JDK instalado.
pause
exit /b 1
