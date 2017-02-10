#!/bin/bash

#sbt clean
#sbt package
#/home/zgg/lib/spark-1.0.1-bin-hadoop2/bin/spark-submit \
##
exeNum=30
for i in {1..3}
do
  spark-submit \
  --class "org.dsa.time.aliyunDSW2ATMQueryTime" \
  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/lib" \
  --master yarn-client \
  --executor-memory 10G \
  --driver-memory 10G \
  --num-executors  $exeNum \
  --executor-cores 4 \
  DSA.jar
done

for i in {1..3}
do
  spark-submit \
  --class "org.dsa.time.aliyunDSW2ATMQueryD9L392N40Time" \
  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/lib" \
  --master yarn-client \
  --executor-memory 10G \
  --driver-memory 10G \
  --num-executors  $exeNum \
  --executor-cores 4 \
  DSA.jar
done

