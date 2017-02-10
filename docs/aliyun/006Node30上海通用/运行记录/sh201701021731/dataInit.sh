#!/bin/bash
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-shanghai-internal.aliyuncs.com get oss://xubo245/ref/DL9Line.fasta.gz DL9Line.fasta.gz
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-shanghai-internal.aliyuncs.com get oss://xubo245/ref/D9L10240N4 D9L10240N4
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-shanghai-internal.aliyuncs.com get oss://xubo245/ref/D9L392N40 D9L392N40
hadoop fs -mkdir /queryD
hadoop fs -put D9L10240N4 /queryD
hadoop fs -put D9L392N40 /queryD
tar -zxvf DL9Line.fasta
hadoop fs -mkdir /Luniref
hadoop fs -put DL9Line.fasta /Luniref

