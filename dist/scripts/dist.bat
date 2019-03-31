@echo off

pushd %~dp0\..\..
gradlew.bat dist
popd
