@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Begin all REM://
@echo off

@REM Set the current directory to the location of this script
set MAVEN_PROJECTBASEDIR=%~dp0
@REM Remove trailing backslash so we can safely quote paths
set MAVEN_PROJECTBASEDIR_NO_SLASH=%MAVEN_PROJECTBASEDIR:~0,-1%

@REM Find java.exe
set "JAVA_EXE=java.exe"

@REM Check JAVA_HOME
if defined JAVA_HOME goto findJavaFromJavaHome
goto findJavaFromPath

:findJavaFromJavaHome
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if exist "%JAVA_EXE%" goto checkMavenWrapper
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
goto error

:findJavaFromPath
where java >nul 2>nul
if %ERRORLEVEL% equ 0 goto checkMavenWrapper
echo ERROR: java is not found in your PATH.
goto error

:checkMavenWrapper
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

if exist "%WRAPPER_JAR%" goto runMaven

@REM Download maven-wrapper.jar if it doesn't exist
echo Downloading Maven Wrapper...
powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%WRAPPER_JAR%' }"

if not exist "%WRAPPER_JAR%" (
    echo ERROR: Failed to download Maven Wrapper jar.
    goto error
)

:runMaven
"%JAVA_EXE%" ^
  -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR_NO_SLASH%" ^
  -cp "%WRAPPER_JAR%" ^
  org.apache.maven.wrapper.MavenWrapperMain ^
  %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
exit /B %ERROR_CODE%
