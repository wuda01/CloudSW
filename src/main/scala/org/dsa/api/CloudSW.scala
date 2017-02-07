package org.dsa.api

import org.apache.spark.{SparkConf, SparkContext}
import org.dsa.core.{CloudSWATM, CloudSWASM}
import org.dsa.rdd.AlignmentRecordTopK
import org.dsa.utils.Constants

/**
  * Created by xubo on 2017/2/6.
  */
class CloudSW {
  def ASM(sc: SparkContext,queryFile: String, refFile: String,scoreMatrixFile: String = "BLOSUM50",  open: Int = 12, ext: Int = 2,  splitNum: Int = 128, taskNum: Int = 1, topK: Int = 5): Array[AlignmentRecordTopK] = {
    val cloudSWASM = new CloudSWASM
    val result = cloudSWASM.run(sc,queryFile, refFile,scoreMatrixFile,open,ext,splitNum, taskNum, topK)
    result
  }

  def ATM(sc: SparkContext,queryFile: String, refFile: String,scoreMatrixFile: String = "BLOSUM50",  open: Int = 12, ext: Int = 2,  splitNum: Int = 128, taskNum: Int = 1, topK: Int = 5): Array[AlignmentRecordTopK] = {
    val cloudSWATM = new CloudSWATM
    val result = cloudSWATM.run(sc,queryFile, refFile,scoreMatrixFile,open,ext,splitNum, taskNum, topK)
    result
  }

}

object CloudSW {
  def main(args: Array[String]) {
    val cloudSW = new CloudSW()
    val conf = new SparkConf().setAppName("CloudSW").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    if (System.getProperties.getProperty("os.name").contains("Windows")) {
      conf.setMaster("local[16]")
    }
    val sc = new SparkContext(conf)

    var SM=Constants.ScoreMatrix
    var open = Constants.Open;
    var ext = Constants.Extension;
    var splitNum=Constants.SplitNum
    var taskNum=Constants.TaskNum
    var topK=Constants.TopK

    val cloudSWType = args(0)
    val queryFile = args(1)
    val refFile = args(2)


    if (args.length > 3) {
      SM = args(3);
    }
    if (args.length > 4) {
      open = args(4).toInt;
    }
    if (args.length > 5) {
      ext = args(5).toInt;
    }
    if (args.length > 6) {
      splitNum = args(6).toInt;
    }
    if (args.length > 7) {
      taskNum = args(7).toInt;
    }
    if (args.length > 8) {
      topK = args(8).toInt;
    }
    var result = new Array[AlignmentRecordTopK](0);
    if (cloudSWType.equalsIgnoreCase("ASM")) {
      result = cloudSW.ASM( sc,queryFile, refFile,SM,open,ext,splitNum,taskNum,topK)
      printArray(result)
    } else if (cloudSWType.equalsIgnoreCase("ATM")) {
      result = cloudSW.ATM(sc,queryFile, refFile,SM,open,ext,splitNum,taskNum,topK)
      printArray(result)
    }else {
      println("CloudSW type error,please select ASM or ATM.")
    }

  }

  def printArray(result: Array[AlignmentRecordTopK]): Unit = {
    result.foreach { each =>
      println(each.getQueryName())
      println(each.getTopK())
      each.getAlignmentRcoreds().foreach(println)
    }
  }

}
