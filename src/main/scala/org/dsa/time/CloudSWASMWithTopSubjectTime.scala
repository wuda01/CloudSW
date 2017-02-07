package org.dsa.time

import org.dsa.core.CloudSWASMWithTop
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWASMWithTopSubjectTime {
  def main(args: Array[String]) {

    var subject = ArgsDefault.DSWSubjectD8D10
    for (j <- 0 until subject.length) {
      for (i <- 0 until 50) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }
    subject = ArgsDefault.DSWSubjectBig
    for (j <- 0 until subject.length) {
      for (i <- 0 until 300) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }

    subject = ArgsDefault.DSWSubject
    for (j <- 0 until subject.length) {
      for (i <- 0 until 50) {
        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        CloudSWASMWithTop.main(subject(j))
      }
    }

  }
}
