package org.dsa.time

import org.dsa.core.{CloudSWASMWithTop, CloudSWATMWithTop}
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWATMWithTopQueryTime {
  def main(args: Array[String]) {

    //    var subject = ArgsDefault.DSWQueryD9HDFSSmall

//    var subject = ArgsDefault.DSWQueryD8NCluster
    //    var subject = ArgsDefault.DSWQueryD8N
    var subject = ArgsDefault.DSWQueryDL8L32
    for (j <- 0 until subject.length) {
      for (i <- 0 until 1) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWATMWithTop.main(subject(j))
      }
    }


    /**
      * DSW2ATM,ASM
      **/
    //    var subject = ArgsDefault.DSWQueryD8BLOSUM50
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 5) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //      }
    //    }
    //
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 5) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }
    /**
      *
      */


    /**
      * time450DSW2atmQuery
      * start
      */
    //var subject = ArgsDefault.DSWQueryHDFS
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 50) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //      }
    //    }
    //
    //    subject = ArgsDefault.DSWQueryHDFS
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 50) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }
    //
    //    subject = ArgsDefault.DSWQueryD8
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 50) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //      }
    //    }
    //
    //    subject = ArgsDefault.DSWQueryHDFS
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 50) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }
    /**
      * time450DSW2atmQuery
      * end
      */

    //     subject = ArgsDefault.DSWQueryD9HDFS
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 5) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //      }
    //    }

  }
}
