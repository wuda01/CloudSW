package org.dsa.time

import org.dsa.core.CloudSWASMWithTop
import org.dsa.utils.{ArgsDefaultAliyun, ArgsDefault}

/**
  * Created by xubo on 2016/12/11.
  */
object aliyunDSW2QueryHDFSTime {
  def main(args: Array[String]) {

    var subject = ArgsDefaultAliyun.aliyunDSWQueryHDFSD9
    for (j <- 0 until subject.length) {
      for (i <- 0 until 5) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }
  }
}
