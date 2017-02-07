package org.dsa.core

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.dsa.core.sa.SWNormal
import org.dsa.mediator.ssw.SSWScala
import org.dsa.rdd.{AlignmentRecord, AlignmentRecordTopK}
import org.dsa.utils.{Constants, NameOrdering}
import parasail.{Profile, RunParasail}

/**
  * Created by xubo on 2016/12/25.
  */
class CloudSWATMWithTop extends DSASequenceAlignmentTime {

//  val sswScoreThreshold = 32700

  /**
    * 对当条query序列和refRDD进行序列比对，并返回结果
    *
    * @param sc          SparkContext
    * @param query       查询序列
    * @param refRDD      ref database
    * @param scoreMatrix 平均矩阵，如蛋白质的blosum50
    * @param topK        topK
    * @return 比对后的结果
    */
  override def align(sc: SparkContext, query: (String, String), refRDD: RDD[(String, String)], scoreMatrix: String = Constants.ScoreMatrix, open: Int = Constants.Open, extension: Int = Constants.Extension, topK: Int = Constants.TopK): AlignmentRecordTopK = {
    //1 compute
    var alignmentRecordTopK = new AlignmentRecordTopK()

    var flagLocal = true
    var mapRDD = refRDD.map { ref =>

      if (flagLocal == true) {
        RunParasail.createProfile(query._2, scoreMatrix)
        flagLocal = false;
      }

      val alignment = runAlign(null, ref._2, open, extension)
      alignment.refName = ref._1
      alignment.refSequence = ref._2
      alignment
    }

    //maybe  be error
    flagLocal = true

    var alignmendRecordArray = mapRDD.top(topK)(NameOrdering).map { each =>
      var alignmentSecond: AlignmentRecord = new AlignmentRecord()

      if (each.score1 < Constants.ScoreThreshold) {
        alignmentSecond = SSWScala.align(query._2, each.refSequence, scoreMatrix, open, extension)
      } else {
        alignmentSecond = SWNormal.SSWLocal(query._2, each.refSequence, scoreMatrix, open, extension)
      }

      alignmentSecond.refName = each.refName
      alignmentSecond.refSequence = each.refSequence
      alignmentSecond

    }

    //2 transform
    alignmentRecordTopK.setTopK(topK)
    alignmentRecordTopK.setQueryName(query._1)
    alignmentRecordTopK.setAlignmentRcoreds(alignmendRecordArray)
    //3 return
    alignmentRecordTopK
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


  def runAlign(profile: Profile, ref: String, open: Int, extension: Int): AlignmentRecord = {
    new AlignmentRecord(RunParasail.runSW_striped_profile_sat(profile, ref, open, extension))
  }

}


object CloudSWATMWithTop {
  var startTime = System.currentTimeMillis()
  var stopTime = System.currentTimeMillis()
  var outStr = new String
  var appName = new String
  var outFile = new StringBuilder
  var flag = true;

  def main(args: Array[String]) {

    val dsw2atm = new CloudSWATMWithTop()
    val scoreMatrix = args(0)
    val queryFile = args(1)
    val dbFile = args(2)
    val splitNum = args(3).toInt
    val taskNum = args(4).toInt
    val topK = args(5).toInt
    var open = Constants.Open;
    var extension = Constants.Extension;
    if (args.length > 6) {
      open = args(6).toInt;
    }
    if (args.length > 7) {
      extension = args(7).toInt;
    }

    val queryArr = queryFile.toString.split("/")
    val dbArr = dbFile.toString.split("/")

    val iString = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())

    val alg = dsw2atm.className
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
      .append("extension" + "\t")
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
      .append(extension + "\t")
      .append(splitNum.toString + "\t")
      .append(taskNum.toString + "\t")
      .append(topK.toString + "\t")

    appName = alg + " Application:" + "queryFile=" + queryFile.toString +
      ",dbFile=" + dbFile.toString + ",splitNum=" + splitNum.toString +
      ",taskNum=" + taskNum.toString + ",topK=" + topK.toString


    println("queryFile:" + queryFile + "\tdbFile:" + dbFile + "\t scoreMatrix:" + scoreMatrix + "\t open:" + open + "\t extension:" + extension + "\tsplitNum:" + splitNum + " \ttaskNum:" + taskNum + "\ttopK:" + topK)
    startTime = System.currentTimeMillis()
    // set application name
    val conf = new SparkConf().setAppName(appName)
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.OFF)
    // initialize SparkContext
    val spark = new SparkContext(conf)

    val result = dsw2atm.run(spark, queryFile, dbFile, scoreMatrix, open, extension, splitNum, taskNum, topK)

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
    val conf = new SparkConf().setAppName("DSW2ATM" + " Application:saveResult,out:" + outStr)
    // initialize SparkContext
    val sc = new SparkContext(conf)
    val rddSave = sc.parallelize(Array(str.toString()), 1)
    rddSave.saveAsTextFile(outStr)
    sc.stop
  }
}



