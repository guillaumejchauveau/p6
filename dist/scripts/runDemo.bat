@echo off

if [%1] == [] (
 echo Choisir une classe principale dans le dossier demos\java
 exit /B 1
)

pushd %~dp0\..
javac -cp "cli\lib\*" demos\java\*.java
java -cp "cli\lib\*:." $1
del demos\java\*.class
popd
