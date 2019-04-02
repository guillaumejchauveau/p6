#!/usr/bin/env bash

cd $(dirname $0)/..
javac -cp "cli/lib/*" demos/demo/*.java
