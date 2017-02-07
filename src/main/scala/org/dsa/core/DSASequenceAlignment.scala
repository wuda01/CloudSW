package org.dsa.core

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.dsa.rdd.AlignmentRecordTopK
import org.dsa.utils.Constants

import scala.collection.mutable.ArrayBuffer

/**
  * Created by xubo on 2016/12/28.
  */
abstract class DSASequenceAlignment extends DSAAligmentTrait with Serializable {

  val className = this.getClass().getSimpleName().filter(!_.equals('$'))
//  val defaultScoreMatrix = "BLOSUM50"
//  val defaultOpen = 12
//  val defaultext = 2
//  val defaultSplitNum = 128
//  val defaultTaskNum = 1
//  val defaultTopK = 5
//  val querysThreshold = 2

  def run(sc: SparkContext, queryFile: String, refFile: String, scoreMatrixFile: String = Constants.ScoreMatrix, open: Int = Constants.Open, ext: Int = Constants.Extension, splitNum: Int = Constants.SplitNum, taskNum: Int = Constants.TaskNum, topK: Int = Constants.TopK): Array[AlignmentRecordTopK] = {

    // 1 preprocess

    //1.1 preprocessQuery
    val querys = preprocessQuery(queryFile, sc)

    //1.2 preprocessQuery
    val refRDD = preprocessRef(refFile, splitNum, sc)

    //1.3 preprocessScoreMatrix
    val scoreMatrix = preprocessScoreMatrix(scoreMatrixFile, sc)

    // 2 align
    if (querys.length > Constants.QuerysThreshold) {
      refRDD.persist(StorageLevel.MEMORY_ONLY)
    }
    var result = new ArrayBuffer[AlignmentRecordTopK]()
    querys.foreach { query =>
      result.append(align(sc, query, refRDD, scoreMatrix, open, ext, topK))
    }
    if (querys.length > Constants.QuerysThreshold) {
      refRDD.unpersist()
    }
    return result.toArray
  }


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
  def align(sc: SparkContext, query: (String, String), refRDD: RDD[(String, String)], scoreMatrix: String, open: Int, ext: Int, topK: Int): AlignmentRecordTopK

  /**
    * 将queryFile读入并进行预处理，返回字符串数组
    *
    * @param queryFile 输入文件名
    * @param sc        SparkContext
    * @return 返回预处理结果
    */
  def preprocessQuery(queryFile: String, sc: SparkContext): Array[(String, String)] = {
    val querySource = sc.textFile(queryFile).map { each =>
      val str = each.split(",")
      (str(0), str(1))
    }
    querySource.toArray()
  }

  /**
    * 对ref文件进行预处理
    *
    * @param refFile  ref文件
    * @param splitNum 读入的partition数
    * @param sc       SparkContext
    * @return 返回预处理结果
    */
  def preprocessRef(refFile: String, splitNum: Int, sc: SparkContext): RDD[(String, String)] = {
    val refSource = sc.textFile(refFile, splitNum)
    val refRDD = refSource.map { eachLine =>
      val eachDbSequence = eachLine.split(",")
      (eachDbSequence(0), eachDbSequence(1))
    }
    refRDD
  }

  /**
    * 对scoreMartix进行预处理
    *
    * @param scoreMatrixFile scoreMartix文件
    * @param sc              SparkContext
    * @return 返回预处理结果
    */
  def preprocessScoreMatrix(scoreMatrixFile: String, sc: SparkContext): String = {
    scoreMatrixFile
  }

}

