#!/bin/bash
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-shanghai-internal.aliyuncs.com get 'oss://xubo245/ref/'$1 $1
tar -zxvf $1
hadoop fs -mkdir /Luniref
hadoop fs -put DL9Line.fasta /Luniref
