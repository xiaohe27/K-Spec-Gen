@echo off

set SRC_ROOT=%~dp0..

set RELEASE=%SRC_ROOT%\lib

set CP=%RELEASE%\*

java -cp "%CP%;%CLASSPATH%" transform.Main %*
