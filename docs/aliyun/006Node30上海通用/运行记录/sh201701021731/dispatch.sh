#!/usr/bin/env bash

if [ $# -ne 1 ]; then
    echo "Usage: dispath.sh filename"
    exit 1
fi

cur_dir=$(cd "$(dirname "$1")"; pwd) 
for dst in {1..50}
do

    scp -r $1 emr-worker-$dst:$cur_dir
done
