#!/usr/bin/env bash

cd $(dirname $0)/..
javac -cp "cli/lib/*" -Xdiags:verbose demos/demo/*.java
