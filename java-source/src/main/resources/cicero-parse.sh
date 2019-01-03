#!/usr/bin/env bash

cicero parse --template $1 --out /tmp/test2.txt > /tmp/cicero.log 2>&1

cat /tmp/test2.txt