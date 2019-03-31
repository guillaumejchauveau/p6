#!/usr/bin/env bash

if [[ $# -eq 0 ]]; then
  echo "Choisir une classe principale dans le dossier demos/java"
  exit 1
fi

cd $(dirname $0)/..
javac -cp "cli/lib/*" demos/java/*.java
java -cp "cli/lib/*:." $1
rm demos/java/*.class
