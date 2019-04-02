#!/usr/bin/env bash

if [[ $# -eq 0 ]]; then
  echo "Choisir une classe principale dans le dossier dist/demos/demo"
  exit 1
fi

cd $(dirname $0)/..
java -cp "cli/lib/*:demos" $1
