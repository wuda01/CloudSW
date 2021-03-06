package org.dsa.time

import org.dsa.core.{CloudSWASMWithTop, CloudSWATMWithTop}
import org.dsa.utils.{ArgsDefaultAliyun, ArgsDefault}

/**
  * Created by xubo on 2016/12/11.
  */
object aliyunDSW2ATMQueryTime {
  def main(args: Array[String]) {

    var subject = ArgsDefaultAliyun.aliyunDSWQueryD9L10240N4
    for (j <- 0 until subject.length) {
      for (i <- 0 until 1) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWATMWithTop.main(subject(j))
      }
    }

    //     subject = ArgsDefaultAliyun.aliyunDSWQueryHDFSD9Longer
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
    //    subject = ArgsDefaultAliyun.aliyunDSWQueryHDFSD8Longer
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 5) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }
    //
    //    subject = ArgsDefaultAliyun.aliyunDSWQueryHDFSD9Longer
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 5) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2.main(subject(j))
    //      }
    //    }

  }
}
