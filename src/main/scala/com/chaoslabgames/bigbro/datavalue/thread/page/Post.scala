package com.chaoslabgames.bigbro.datavalue.thread.page

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
class Post(val message:String) {


  def canEqual(other: Any): Boolean = other.isInstanceOf[Post]

  override def equals(other: Any): Boolean = other match {
    case that: Post =>
      (that canEqual this) &&
        message == that.message
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(message)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
