package org.dsa.mediator.smithwaterman

import org.dsa.utils.DSAFunSuite

/**
  * Created by xubo on 2017/1/12.
  */
class SWSuite extends DSAFunSuite {
  test("a") {
    sc.stop()
    var sw = new SmithWatermanAffine("CGAC", "CCCAA", "blosum50", 12, 2)
    println(sw.cigarX)
    println(sw.xStart)
    println(sw.cigarY)
    println(sw.yStart)
  }
}
