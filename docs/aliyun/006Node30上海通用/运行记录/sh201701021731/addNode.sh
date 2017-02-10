#!/bin/sh

#mkdir -p /home/hadoop/test/time20161230
hadoop fs -rm /Luniref/DL9Line.fasta
hadoop fs -put DL9Line.fasta /Luniref/
