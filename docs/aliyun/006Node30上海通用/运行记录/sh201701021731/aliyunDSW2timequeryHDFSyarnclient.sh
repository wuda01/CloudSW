#!/bin/bash

#sbt clean
#sbt package
#/home/zgg/lib/spark-1.0.1-bin-hadoop2/bin/spark-submit \
  spark-submit \
  --class "org.dsa.time.aliyunDSW2QueryHDFSTime" \
  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/lib" \
  --master yarn-client \
  --executor-memory 8G \
  --num-executors  5 \
  --executor-cores 4 \
  DSA.jar
