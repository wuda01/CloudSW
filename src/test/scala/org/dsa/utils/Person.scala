package org.dsa.utils

/**
  * Created by xubo on 2017/1/15.
  */
class Person {

  var name: String = "jack"
  var age: Int = 20

  def this(name: String, age: Int) {
    this
    this.name = name
    this.age = age
  }

  override def toString = s"Person($name, $age)"
}
