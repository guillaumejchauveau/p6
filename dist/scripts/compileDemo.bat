@echo off

pushd %~dp0\..
javac -cp cli/lib/* demos/demo/*.java
popd
