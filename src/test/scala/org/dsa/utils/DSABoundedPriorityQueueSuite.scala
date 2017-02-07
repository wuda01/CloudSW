package org.dsa.utils

import org.dsa.rdd.AlignmentRecord

/**
  * Created by xubo on 2017/1/15.
  */
class DSABoundedPriorityQueueSuite extends DSAFunSuite {
  test("test1") {
    sc.stop()

    var BPqueue = new DSABoundedPriorityQueue[Int](5)
    var arr = Array(1, 8, 4, 7, 9, 11, 55, 33, 22, 2)
    BPqueue.foreach(println)
    println(BPqueue.size)
    BPqueue ++= arr
    BPqueue.foreach(println)
    println(BPqueue.size)
  }
  test("test2") {
    sc.stop()

    var BPqueue = new DSABoundedPriorityQueue[Int](5)
    var arr = Array(1, 8, 4, 7, 9, 11, 55, 33, 22, 2, 44)
    BPqueue.foreach(println)
    println(BPqueue.size)
    BPqueue ++= arr
    BPqueue.foreach(println)
    println(BPqueue.size)
  }

  test("test3") {
    sc.stop()

    //    var BPqueue = new DSABoundedPriorityQueue[Array[Int]](5)
    //    var arr = Array(Array(1, 8, 4), Array(7, 9, 11), Array(55, 33, 22), Array(2,100,0))
    //    BPqueue.foreach(println)
    //    println(BPqueue.size)
    //    BPqueue ++= arr
    //    BPqueue.foreach(println)
    //    println(BPqueue.size)
  }

  test("test4") {
    sc.stop()

    var arr = Array(new Person("a", 22), new Person("x", 1), new Person("z", 124), new Person("f", 20), new Person("c", 25), new Person("s", 24), new Person("6", 666))
    var personqueue = new DSABoundedPriorityQueue[Person](3)(PersonOrdering)
    personqueue ++= arr
    personqueue.foreach(println)
  }

}

object PersonOrdering extends Ordering[Person] {
  def compare(a: Person, b: Person): Int =
    implicitly[Ordering[Int]].compare(a.age, b.age)
}
