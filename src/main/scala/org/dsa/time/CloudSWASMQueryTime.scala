package org.dsa.time

import org.dsa.core._
import org.dsa.utils.ArgsDefault

/**
  * Created by xubo on 2016/12/11.
  */
object CloudSWASMQueryTime {
  def main(args: Array[String]) {

    //    var subject = ArgsDefault.DSWQueryD8NCluster
    for (kk <- 0 until 5) {


            var subject = ArgsDefault.DSWQueryDL8L32
            for (j <- 0 until subject.length) {
              for (i <- 0 until 1) {
                subject(j).foreach { each =>
                  print(each + "\t")
                }
                println()
                CloudSWASMWithTop.main(subject(j))
                CloudSWASM.main(subject(j))
              }
            }

      //            var subject = ArgsDefault.DSWQueryDL8N100
      //            var subject = ArgsDefault.DSWQueryDL1

//      var subject = ArgsDefault.DSWQueryDL8N2
      //      subject = ArgsDefault.DSWQueryDL8L32
      //      for (j <- 0 until subject.length) {
      //
      //        subject(j).foreach { each =>
      //          print(each + "\t")
      //        }
      //        println()
      //        for (i <- 0 until 5) {
      //          DSW2SATM.main(subject(j))
      //        }
      //      }

      //           subject = ArgsDefault.DSWQueryD8BLOSUM50
      //          for (j <- 0 until subject.length) {
      //            for (i <- 0 until 5) {
      //              subject(j).foreach { each =>
      //                print(each + "\t")
      //              }
      //              println()
      //              DSW2ATM.main(subject(j))
      //            }
      //            for (i <- 0 until 5) {
      //              subject(j).foreach { each =>
      //                print(each + "\t")
      //              }
      //              println()
      //              DSW2SATM.main(subject(j))
      //            }
      //          }


      subject = ArgsDefault.DSWQueryDL8N100
      for (j <- 0 until subject.length) {

        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()

        for (i <- 0 until 1) {
          DSW.main(subject(j))
        }
        //              for (i <- 0 until 5) {
        //                DSW2ATM.main(subject(j))
        //
        //              }
        //              for (i <- 0 until 5) {
        //                DSW2SATM.main(subject(j))
        //              }

      }

      subject = ArgsDefault.DSWQueryDL8L32
      for (j <- 0 until subject.length) {

        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()

        for (i <- 0 until 1) {
          DSW.main(subject(j))
        }
      }

      subject = ArgsDefault.DSWQueryDL8L32N900
      subject(0).foreach { each =>
        print(each + "\t")
      }
      println()

      //      for (i <- 0 until 2) {
      //        DSW2ATM.main(subject(1))
      //      }

      for (i <- 0 until 5) {
        CloudSWATM.main(subject(1))
      }

      for (j <- 2 until subject.length) {

        subject(j).foreach { each =>
          print(each + "\t")
        }
        println()
        for (i <- 0 until 5) {
          CloudSWATMWithTop.main(subject(j))

        }
        for (i <- 0 until 5) {
          CloudSWATM.main(subject(j))
        }
      }
    }

    /**
      * 201701192201 start
      */
    //    var subject = ArgsDefault.DSWQueryDL8L2048L4096N100
    //    //      for (j <- 0 until subject.length) {
    //    //        for (i <- 0 until 1) {
    //    subject(0).foreach { each =>
    //      print(each + "\t")
    //    }
    //    println()
    //
    //    DSW2SATM.main(subject(0))
    //    subject(1).foreach { each =>
    //      print(each + "\t")
    //    }
    //    println()
    //    DSW2ATM.main(subject(1))
    //    DSW2SATM.main(subject(1))
    //        }
    //      }

    /**
      * 201701192201 end
      */

    /**
      * 201701191740 start
      */
    //      var subject = ArgsDefault.DSWQueryDL8N100
    //      var subject = ArgsDefault.DSWQueryDL1
    //      var subject = ArgsDefault.DSWQueryDL8L2048L4096N100
    //      for (j <- 0 until subject.length) {
    //        for (i <- 0 until 1) {
    //          subject(j).foreach { each =>
    //            print(each + "\t")
    //          }
    //          println()
    //          DSW2ATM.main(subject(j))
    //          DSW2SATM.main(subject(j))
    //        }
    //      }
    /**
      * 201701191740 end
      */

    /**
      * 201701152140 start
      */
    //    var subject = ArgsDefault.DSWQueryDL8L32
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 1) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //        DSW2SATM.main(subject(j))
    //      }
    //    }
    //
    //    subject = ArgsDefault.DSWQueryDL8N100
    //    for (j <- 0 until subject.length) {
    //      for (i <- 0 until 1) {
    //        subject(j).foreach { each =>
    //          print(each + "\t")
    //        }
    //        println()
    //        DSW2ATM.main(subject(j))
    //        DSW2SATM.main(subject(j))
    //      }
    //    }

    /**
      * 201701152140 end
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
