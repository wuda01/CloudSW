package org.dsa.mediator.ssw

import org.apache.spark.unsafe.types.ByteArray
import org.dsa.rdd.AlignmentRecord
import ssw.SSW

/**
  * Created by xubo on 2016/11/25.
  */
object SSWScala {
  def main(args: Array[String]) {

    val score = SSWScala.align(args(0), args(1))
    println(score)

  }

  def align(query: String, ref: String,scoreMaxtrix:String="blosum50",open:Int=12,extension:Int=2): AlignmentRecord = {
    val alignment = SSW.align(query, ref,scoreMaxtrix,open,extension)
    new AlignmentRecord(alignment)
  }

  def alignNew(query: String, ref: String): AlignmentRecord = {
    val alignment = SSW.alignNew(query, ref)
    new AlignmentRecord(alignment)
  }

}
