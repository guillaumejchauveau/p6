@echo off

pushd %~dp0\..
javac -cp cli/lib/* -Xdiags:verbose demos/demo/*.java
popd
