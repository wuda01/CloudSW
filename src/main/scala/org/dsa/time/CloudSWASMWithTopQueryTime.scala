package org.dsa.time

import org.dsa.core.{CloudSWATMWithTop, CloudSWASMWithTop}
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWASMWithTopQueryTime {
  def main(args: Array[String]) {

    //    var subject8 = ArgsDefault.DSWQueryD9Small
    //    for (j <- 0 until subject8.length) {
    //      for (i <- 0 until 100) {
    //        subject8(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject8(j))
    //      }
    //    }

    var subject = ArgsDefault.DSWQueryD8BLOSUM50
    for (j <- 0 until subject.length) {
      for (i <- 0 until 5) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }

     subject = ArgsDefault.DSWQueryD8HDFSBLOSUM50
    for (j <- 0 until subject.length) {
      for (i <- 0 until 5) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }
    /**
      * start2
      */
    //    var subject = ArgsDefault.DSWQueryD9
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 3) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }
    //
    //    var subject8 = ArgsDefault.DSWQueryD8Small
    //    for (j <- 0 until subject8.length) {
    //      for (i <- 0 until 100) {
    //        subject8(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject8(j))
    //      }
    //    }
    //
    //    var subject2 = ArgsDefault.DSWQueryD8
    //    for (j <- 0 until subject2.length) {
    //      for (i <- 0 until 3) {
    //        subject2(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject2(j))
    //      }
    //    }
    /**
      * end2
      */
  }
}
