package org.dsa.time

import org.dsa.core.CloudSWATMWithTop
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWATMWithTopSubjectTime {
  def main(args: Array[String]) {

//    var subject = ArgsDefault.DSWQueryD9HDFSSmall
var subject = ArgsDefault.DSWSubject
    for (j <- 0 until subject.length) {
      for (i <- 0 until 50) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWATMWithTop.main(subject(j))
      }
    }

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
