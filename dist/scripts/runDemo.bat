@echo off

if [%1] == [] (
 echo Choisir une classe principale dans le dossier dist\demos\demo
 exit /B 1
)

pushd %~dp0\..
java -cp "cli\lib\*:demos" $1
popd
