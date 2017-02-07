package org.dsa.mediator.ssw

import parasail.RunParasail

import scala.io.Source

/**
  * Created by xubo on 2016/11/30.
  */
object SSWRunBuffer {
  def main(args: Array[String]) {
    if (null == args) {
      throw new Exception(s"input null")
    }

    if (2 != args.length) {
      throw new Exception(s"input should:queryFile,refFile")
    }

    print("query:\t" + args(0))
    print("\tref:\t" + args(1))
    val start = System.currentTimeMillis()

    var topScore = 0
    var topName = ""

    val sources = Source.fromFile(args(0))
    val querys = sources.getLines().toArray.map { each =>
      val str = each.split(",")
      (str(0), str(1))
    }

    //    val refs = Source.fromFile(args(1)).getLines().toArray.map { each =>
    //      val str = each.split(",")
    //      (str(0), str(1))
    //    }
    var queryLength = 0
    var refLength = 0
    querys.map { query =>


      val refs = Source.fromFile(args(1)).bufferedReader()
      var line = refs.readLine()
      var startFlag = true
      while (line != null) {
        val strArr = line.split(",")

        //        if (strArr(1).length / 100 <= 19) {
        //          out(strArr(1).length / 100).println(line)
        //        }


        //        runSW_striped_profile_sat
        val result = SSWScala.align(query._2, strArr(1)).score1
        //        val result = RunParasail.runSW_striped_profile_sat(profile, strArr(1), 12, 2).getScore
        //        val result = RunParasail.runSW_striped_sat(query._2, strArr(1), 12, 2, Matrix.blosum50)
        if (result > topScore) {
          topScore = result
          topName = strArr(0)
        }

        line = refs.readLine()
      }


      //      refs.map { ref =>
      //        if(refLength==0){
      //          refLength=ref._2.length
      //          queryLength=query._2.length
      //        }
      //        //        println("query\t" + query._2 + "\nref:\t" + ref._2)
      //        val result = SSWScala.align(query._2, ref._2)
      //        result.refName=ref._1
      //        result.refSequence=ref._2
      //        if (result.score1 > topScore) {
      //          topScore = result.score1
      //          topName = ref._1
      //        }
      //        println()
      //        println(result)
      //        println()
      //      }
    }
    //    print("\tSuccess")

    val stop = System.currentTimeMillis()

    print("\tqueryLength:\t" + queryLength)
    print("\trefLength:\t" + refLength)
    print("\ttopScore:" + topScore)
    print("\ttopName:" + topName)
    print("\ttime:\t" + (stop - start) + "\tms\n")
  }


}


