package org.dsa.core.etl

import java.io.PrintWriter

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by xubo on 2016/12/31.
  */
object SelectNumOfData {
  def main(args: Array[String]) {

    run()
  }

  def run(): Unit = {
    val conf = new SparkConf().setAppName(this.getClass().getSimpleName().filter(!_.equals('$')))
    if (System.getProperties.getProperty("os.name").contains("Windows")) {
      conf.setMaster("local[16]")
    }

    val sc = new SparkContext(conf)
    //    val file = "file/input/query/D9L392.fasta"
    val file = "file/input/query/DL8L32"
    //    val file = "file/input/query/DL8L425"
    //    val file = "file/input/query/DL8L4096"
    var num = 1300
    //    val out = "file/input/query/DL8L425N" + num
    for(i<-1 to 25){
      num=i*100
      val out = file + "N" + num
      compute(file, out, num, sc)
    }

    sc.stop()
  }

  def compute(input: String, output: String, num: Int, sc: SparkContext): Unit = {


    val rdd = sc.textFile(input).take(num)
    val outQuery = new PrintWriter(output)
    rdd.foreach { each =>
      outQuery.println(each)
    }
    outQuery.close()

  }

}
