#!/usr/bin/env bash

java -cp "$(dirname $0)/../cli/lib/*:." com.p6.cli.App $*
