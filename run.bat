@echo off
chcp 65001 >nul
echo Compiling with debug info...
echo.

REM Create bin directory
if not exist "bin" mkdir bin

REM Compile step by step with verbose output
echo Step 1: Compiling utils...
javac -d bin -verbose src/utils/*.java
if %errorlevel% neq 0 goto error

echo Step 2: Compiling tabComponents...
javac -d bin -cp "bin" -verbose src/tabComponents/*.java
if %errorlevel% neq 0 goto error

echo Step 3: Compiling main classes...
javac -d bin -cp "bin" -verbose src/*.java
if %errorlevel% neq 0 goto error

echo.
echo All files compiled successfully!
echo.
echo Running App...
java -cp "bin" App
pause
exit /b 0

:error
echo.
echo Compilation failed at step above.
pause
exit /b 1
