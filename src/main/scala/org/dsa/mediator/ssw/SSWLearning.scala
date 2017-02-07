package org.dsa.mediator.ssw

import ssw.SSW

/**
  * Created by xubo on 2016/11/25.
  */
object SSWLearning {
  def main(args: Array[String]) {

    val score = SSW.align("AGCT", "ACT","blosum50",12,2).score1
    println(score)

  }
}
