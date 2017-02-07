package org.dsa.core

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.dsa.mediator.ssw.SSWScala
import org.dsa.rdd.{AlignmentRecord, AlignmentRecordTopK}
import org.dsa.utils.NameOrdering

/**
  * Created by xubo on 2016/11/28.
  */
class DSW extends SequenceAlignment {
  /**
    * 对当条query序列和refRDD进行序列比对，并返回结果
    *
    * @param query       查询序列
    * @param refRDD      ref database
    * @param scoreMatrix 评分矩阵，如蛋白质的blosum50
    * @param topK        topK
    * @param sc          SparkContext
    * @return 比对后的结果
    */
  override def align(query: (String, String), refRDD: RDD[(String, String)], scoreMatrix: Array[Array[Int]], topK: Int, sc: SparkContext): AlignmentRecordTopK = {

    //1 compute
    var alignmentRecordTopK = new AlignmentRecordTopK()
    val mapRDD = refRDD.map { ref =>
      //      implicit val ord = implicitly[NameOrdering]
      val alignment = SSWScala.align(query._2, ref._2)
      //      (alignment.score1, alignment)
      alignment.refName = ref._1
      alignment
    }

    val alignmendRecordArray = mapRDD.top(topK)(NameOrdering)

    //2 transform
    alignmentRecordTopK.setTopK(topK)
    alignmentRecordTopK.setQueryName(query._1)
    alignmentRecordTopK.setAlignmentRcoreds(alignmendRecordArray)

    //3 return
    alignmentRecordTopK
  }

  //  def compare(a: AlignmentRecord, b: AlignmentRecord): Int =
  //    implicitly[Ordering[AlignmentRecord]].compare(a.score1, b.score1)
  /**
    * 对scoreMartix进行预处理
    *
    * @param scoreMatrixFile scoreMartix文件
    * @param sc              SparkContext
    * @return 返回预处理结果
    */
  override def preprocessScoreMatrix(scoreMatrixFile: String, sc: SparkContext): Array[Array[Int]] = null

  //    = {
  //      var test = Array.ofDim[Int](1, 1)
  //      test(0)(0) = 1
  //      test
  //    }
}


object DSW {
  var startTime = System.currentTimeMillis()
  var stopTime = System.currentTimeMillis()
  var outStr = new String
  var appName = new String
  var outFile = new StringBuilder

  def main(args: Array[String]) {

    val subMatrix = args(0)
    val queryFile = args(1)
    val dbFile = args(2)
    val splitNum = args(3).toInt
    val taskNum = args(4).toInt
    val topK = args(5).toInt

    val dsw = new DSW()
    //    //    val result = dsw.run()
    //  }
    //
    //  def run(subMatrix: String, queryFile: String, dbFile: String, splitNum: Int, taskNum: Int, topK: Int) {

    val queryArr = queryFile.toString.split("/")
    val dbArr = dbFile.toString.split("/")

    val iString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())

    val alg = dsw.className
    outStr = "/xubo/project/SparkSW/output/time/" +
      iString + alg + "_" + "queryFile_" + queryArr(queryArr.length - 1) +
      "_dbFile_" + dbArr(dbArr.length - 1) + "_splitNum_" + splitNum.toString +
      "_taskNum_" + taskNum.toString + "_topK_" + topK.toString
    if (args.length > 6) {
      outStr = args(6) + iString
    }
    outFile.append("initTime1\t")
      .append("alg" + "\t")
      .append("data" + "\t")
      .append("query" + "\t")
      .append("splitNum" + "\t")
      .append("taskNum" + "\t")
      .append("org/dsw/topK" + "\t")
      .append("totalTime\t")
      .append("fileName\t")
      .append("\n")

    outFile.append(iString + "\t")
      .append(alg + "\t")
      .append(dbArr(dbArr.length - 1) + "\t")
      .append(queryArr(queryArr.length - 1) + "\t")
      .append(splitNum.toString + "\t")
      .append(taskNum.toString + "\t")
      .append(topK.toString + "\t")

    appName = alg + " Application:" + "queryFile=" + queryFile.toString +
      ",dbFile=" + dbFile.toString + ",splitNum=" + splitNum.toString +
      ",taskNum=" + taskNum.toString + ",topK=" + topK.toString


    startTime = System.currentTimeMillis()
    // set application name
    val conf = new SparkConf().setAppName(appName)

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.OFF)
    // initialize SparkContext
    val spark = new SparkContext(conf)


    val result = dsw.run(subMatrix: String, queryFile: String, dbFile: String, splitNum: Int, taskNum: Int, topK: Int, spark)

    spark.stop()
    stopTime = System.currentTimeMillis()

    outFile.append((stopTime - startTime) / 1000.0 + "\t").append(outStr + "\t")
    result.foreach { each =>
      println("topK:" + each.getTopK() + " Query:" + each.getQueryName())
      each.getAlignmentRcoreds().foreach { alignmentRecord =>
        println(alignmentRecord)
      }
      println()
    }

    saveResult(outFile.toString())
    outFile.clear()
  }

  def saveResult(str: String): Unit = {

    val conf = new SparkConf().setAppName("DSW" + " Application:saveResult,out:" + outStr)
    // initialize SparkContext
    val sc = new SparkContext(conf)
    val rddSave = sc.parallelize(Array(str.toString()), 1)
    rddSave.saveAsTextFile(outStr)
    sc.stop

  }

}
