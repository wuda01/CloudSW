#!/bin/bash

#sbt clean
#sbt package
#/home/zgg/lib/spark-1.0.1-bin-hadoop2/bin/spark-submit \
  spark-submit \
  --class "org.dsa.time.aliyunDSW2QueryHDFSTime" \
  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/lib" \
  --master local \
  --executor-memory 8G \
  DSA.jar
