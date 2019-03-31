@echo off

if [%1] == [] (
 echo Choisir une classe principale dans le dossier dist\demos\demo
 exit /B 1
)

pushd %~dp0\..
javac -cp "cli\lib\*" demos\demo\*.java
java -cp "cli\lib\*:demos" $1
del demos\demo\*.class
popd
