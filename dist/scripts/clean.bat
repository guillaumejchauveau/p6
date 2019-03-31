@echo off

pushd %~dp0\..\..
gradlew.bat cleanDist
popd
