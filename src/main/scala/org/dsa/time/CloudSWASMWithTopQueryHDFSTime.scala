package org.dsa.time

import org.dsa.core.CloudSWASMWithTop
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWASMWithTopQueryHDFSTime {
  def main(args: Array[String]) {

    var subject = ArgsDefault.DSWQueryHDFS
    for (j <- 0 until subject.length) {
      for (i <- 0 until 3) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }
  }
}
