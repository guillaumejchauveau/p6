#!/usr/bin/env bash

chmod +x $(dirname $0)/../cli/bin/cli
$(dirname $0)/../cli/bin/cli $*
