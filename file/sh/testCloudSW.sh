#!/bin/bash
  spark-submit \
  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/disk2/xubo/lib" \
  --class "org.dsa.api.CloudSW" \
  --master spark://master:7077 \
  --executor-memory 8G \
  DSA.jar $1 $2 $3 $4 $5 $6 $7 $8 $9

