package org.dsa.core

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.dsa.core.sa.SWNormal
import org.dsa.mediator.ssw.SSWScala
import org.dsa.rdd.{AlignmentRecord, AlignmentRecordTopK}
import org.dsa.utils.{DSABoundedPriorityQueue, NameOrdering}
import parasail.{Profile, RunParasail}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by xubo on 2016/12/25.
  */
class CloudSWATM extends DSASSequenceAlignmentTime {

  val sswScoreThreshold = 32700


  /**
    * 对当条query序列和refRDD进行序列比对，并返回结果
    *
    * @param sc          SparkContext
    * @param querys      查询序列
    * @param refRDD      ref database
    * @param scoreMatrix 平均矩阵，如蛋白质的blosum50
    * @param topK        topK
    * @return 比对后的结果
    */
  override def align(sc: SparkContext, querys: Array[(String, String)], refRDD: RDD[(String, String)], scoreMatrix: String = defaultScoreMatrix, open: Int = defaultOpen, extension: Int = defaultGap, topK: Int = defaultTopK): Array[AlignmentRecordTopK] = {
    //1 compute
    var alignmentRecordTopK = new AlignmentRecordTopK()
    var flagLocal = true

    var queryArr = querys.map { each =>
      each._2
    }

    /**
      * 没问题
      * var mapRDD = refRDD.map { ref =>
      * val result = new ArrayBuffer[AlignmentRecord]()
      * var profiles: Array[Profile] = null
      * profiles = RunParasail.createProfileS(queryArr, scoreMatrix)
      * //      querys.foreach { query =>
      * //        if (flagLocal == true) {
      * //          flagLocal = false;
      * //        }
      * for (i <- 0 until querys.length) {
      * val alignment = runAlign(profiles(i), ref._2, open, extension)
      * alignment.refName = ref._1
      * alignment.refSequence = ref._2
      * result.append(alignment)
      * }
      * var resultArr = result.toArray
      * resultArr
      * }
      */

    val mapRDD = refRDD.mapPartitions { refs =>

      var profiles: Array[Profile] = null
      profiles = RunParasail.createProfileS(queryArr, scoreMatrix)

      val refR = refs.map { ref =>
        var arrQueue = new Array[DSABoundedPriorityQueue[AlignmentRecord]](querys.length)
        for (iq <- 0 until querys.length) {
          arrQueue(iq) = new DSABoundedPriorityQueue[AlignmentRecord](topK)(NameOrdering)
        }

        var arrQueue2 = new Array[Int](topK)
        //        val result = new ArrayBuffer[AlignmentRecord]()
        for (i <- 0 until querys.length) {

          val alignment = runAlign(profiles(i), ref._2, open, extension)
          alignment.refName = ref._1
          alignment.refSequence = ref._2
          //          result.append(alignment)
          arrQueue(i) ++= Iterator.single(alignment)
          //          alignment
        }
        arrQueue
      }
      refR
    }

    var reduceRDD = mapRDD.reduce { (arr1, arr2) =>
      for (i <- 0 until arr1.length) {
        arr1(i) ++= arr2(i)
      }
      arr1
    }

    //    var out = new Array[AlignmentRecordTopK](querys.length)
    //
    //    for (j <- 0 until (out.length)) {
    //      out(j) = new AlignmentRecordTopK
    //    }

    val outR = new Array[AlignmentRecordTopK](querys.length)
    for (i <- 0 until querys.length) {
      outR(i) = new AlignmentRecordTopK
    }

    var r1 = reduceRDD.toArray
    for (i <- 0 until r1.length) {
      //      val r2 = r1(i)
      var asm = r1(i).toArray.sorted(NameOrdering.reverse)
      for (j <- 0 until asm.length) {
        var alignmentSecond: AlignmentRecord = new AlignmentRecord()

        if (asm(j).score1 < sswScoreThreshold) {
          alignmentSecond = SSWScala.align(querys(i)._2, asm(j).refSequence, scoreMatrix, open, extension)
        } else {
          alignmentSecond = SWNormal.SSWLocal(querys(i)._2, asm(j).refSequence, scoreMatrix, open, extension)
        }

        alignmentSecond.refName = asm(j).refName
        alignmentSecond.refSequence = asm(j).refSequence
        asm(j) = alignmentSecond
      }

      outR(i).setAlignmentRcoreds(asm)
      outR(i).setTopK(topK)
      outR(i).setQueryName(querys(i)._1)
      //      outR(i)..
    }
    outR
  }

  /**
    * 对scoreMartix进行预处理
    *
    * @param scoreMatrixFile scoreMartix文件
    * @param sc              SparkContext
    * @return 返回预处理结果
    */
  override def preprocessScoreMatrix(scoreMatrixFile: String, sc: SparkContext): String = {
    scoreMatrixFile
  }


  def runAlign(profile: Profile, ref: String, open: Int, gap: Int): AlignmentRecord = {
    new AlignmentRecord(RunParasail.runSW_striped_profile_sat(profile, ref, open, gap))
  }

}

//object NameOrderingArray extends Ordering[Array[AlignmentRecord]] {
//  def compare(a: Array[AlignmentRecord], b: Array[AlignmentRecord]): Int =
//    implicitly[Ordering[Int]].compare(a.score1, b.score1)
//}


object CloudSWATM {
  var startTime = System.currentTimeMillis()
  var stopTime = System.currentTimeMillis()
  var outStr = new String
  var appName = new String
  var outFile = new StringBuilder
  var flag = true;

  def main(args: Array[String]) {

    val dsw2satm = new CloudSWATM()
    val scoreMatrix = args(0)
    val queryFile = args(1)
    val dbFile = args(2)
    val splitNum = args(3).toInt
    val taskNum = args(4).toInt
    val topK = args(5).toInt
    var open = dsw2satm.defaultOpen;
    var gap = dsw2satm.defaultGap;
    if (args.length > 6) {
      open = args(6).toInt;
    }
    if (args.length > 7) {
      gap = args(7).toInt;
    }

    val queryArr = queryFile.toString.split("/")
    val dbArr = dbFile.toString.split("/")

    val iString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())

    val alg = dsw2satm.className
    outStr = "/xubo/project/SparkSW/output/time/" +
      iString + alg + "_" + "queryFile_" + queryArr(queryArr.length - 1) +
      "_dbFile_" + dbArr(dbArr.length - 1) + "_splitNum_" + splitNum.toString +
      "_taskNum_" + taskNum.toString + "_topK_" + topK.toString
    if (args.length > 8) {
      outStr = args(8) + iString
    }
    outFile.append("initTime1\t")
      .append("alg" + "\t")
      .append("data" + "\t")
      .append("query" + "\t")
      .append("scoreMatrix" + "\t")
      .append("open" + "\t")
      .append("gap" + "\t")
      .append("splitNum" + "\t")
      .append("taskNum" + "\t")
      .append("topK" + "\t")
      .append("totalTime\t")
      .append("fileName\t")
      .append("\n")

    outFile.append(iString + "\t")
      .append(alg + "\t")
      .append(dbArr(dbArr.length - 1) + "\t")
      .append(queryArr(queryArr.length - 1) + "\t")
      .append(scoreMatrix + "\t")
      .append(open + "\t")
      .append(gap + "\t")
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

    val result = dsw2satm.run(spark, queryFile, dbFile, scoreMatrix, open, gap, splitNum, taskNum, topK)

    result.foreach { each =>
      println("topK:" + each.getTopK() + " Query:" + each.getQueryName())
      each.getAlignmentRcoreds().foreach { alignmentRecord =>
        println(alignmentRecord)
      }
      println()
    }
    spark.stop()
    stopTime = System.currentTimeMillis()
    outFile.append((stopTime - startTime) / 1000.0 + "\t").append(outStr + "\t")

    saveResult(outFile.toString())
    outFile.clear()
  }

  def saveResult(str: String): Unit = {
    val conf = new SparkConf().setAppName("DSW2SATM" + " Application:saveResult,out:" + outStr)
    // initialize SparkContext
    val sc = new SparkContext(conf)
    val rddSave = sc.parallelize(Array(str.toString()), 1)
    rddSave.saveAsTextFile(outStr)
    sc.stop
  }
}






