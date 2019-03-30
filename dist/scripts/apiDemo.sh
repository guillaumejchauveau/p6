#!/usr/bin/env bash
cd $(dirname $0)/..
javac -cp "cli/lib/*" demos/api/Demo.java
java -cp "cli/lib/*:." demos.api.Demo
rm demos/api/Demo.class demos/api/CharElement.class
