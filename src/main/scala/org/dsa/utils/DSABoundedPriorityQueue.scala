package org.dsa.utils

import java.io.Serializable
import java.util.{PriorityQueue => JPriorityQueue}
import scala.collection.JavaConverters._
import scala.collection.generic.Growable

/**
  * Created by xubo on 2017/1/15.
  */
class DSABoundedPriorityQueue[A](maxSize: Int)(implicit ord: Ordering[A]) extends Iterable[A] with Growable[A] with Serializable {
  private val underlying = new JPriorityQueue[A](maxSize, ord)

  override def +=(elem: A): DSABoundedPriorityQueue.this.type = {
    if (size < maxSize) {
      underlying.offer(elem)
    } else {
      maybeReplaceLowest(elem)
    }
    this
  }

  override def clear(): Unit = {
    underlying.clear()
  }

  override def iterator: Iterator[A] = underlying.iterator.asScala

  private def maybeReplaceLowest(a: A): Boolean = {
    val head = underlying.peek()
    if (head != null && ord.gt(a, head)) {
      underlying.poll()
      underlying.offer(a)
    } else {
      false
    }
  }

}
