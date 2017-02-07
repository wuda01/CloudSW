## CloudSW

CloudSW is an efficient Spark-based distributed Smith-Waterman algorithm optimized for producing the optimal local alignment between pairwise sequences and obtaining the most K similar pairs of the alignments in a horizontally scalable distributed environment.

CloudSW is more efficient algorithm than DSW in DSA system【1】.

In order to facilitate comparison, this project still remains the DSW and SparkSW code. There will be removed after the paper published.

##Building CloudSW
###1.prepare
we should prepare before run CLoudSW.
###1.1 Apache Spark
Please refer: https://github.com/apache/spark
###1.2 Alluxio
Please refer: http://www.alluxio.org/
###1.3 Parasail
Please refer: https://github.com/jeffdaily/parasail
### 2.build

	mvn -DskipTests clean package

###3.run

	spark-submit \
	  --conf "spark.executor.extraJavaOptions=-Djava.library.path=/home/hadoop/disk2/xubo/lib" \
	  --jars /home/hadoop/cloud/adam/lib/adam-apis_2.10-0.18.3-SNAPSHOT.jar,/home/hadoop/cloud/adam/lib/adam-cli_2.10-0.18.3-SNAPSHOT.jar,/home/hadoop/cloud/adam/lib/adam-core_2.10-0.18.3-SNAPSHOT.jar \
	  --class "org.dsa.api.CloudSW" \
	  --master spark://219.219.220.149:7077 \
	  --executor-memory 8G \
	  DSA.jar $1 $2 $3 $4 $5 $6 $7 $8 $9


###ref:  
UniRef100 from http://www.uniprot.org/downloads

###query:  
It can find in UniRef100 or download from http://www.uniprot.org/uniprot/

For example: P18691
http://www.uniprot.org/uniprot/P18691

There are some function in DSA system:
 
	org.dsa.core.prepare.Find

##Paper
Efficient Distributed Smith-Waterman Algorithm Based on Apache Spark (unpublished)


--2017.2.6

The code will be upload after the paper has been published.

If the reviewer of conference need to view source code, please tell me:xubo245@mail.ustc.edu.cn. I will send the source code by email.

##Reference

	【1】B. Xu, C. Li, H. Zhuang, J. Wang, Q. Wang, J. Zhou, et al. (2017, January 1, 2017). DSA: Scalable Distributed Sequence Alignment System Using SIMD Instructions. ArXiv e-prints 1701. Available: https://arxiv.org/abs/1701.01575