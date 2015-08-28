package com.chaoslabgames.bigbro.sevinfo

import org.scalatest.{Matchers, FlatSpec}

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
class ForumUtilsSpec extends FlatSpec with Matchers {
  it should " get list of pages " in {
    val totalPosts = 10
    val postsPerPage = 3
    val baseUrl = "http://localhost?f=1&t=2"
    val pageHrefs:Seq[String] = ForumUtils.calcPageHrefs(baseUrl, totalPosts, postsPerPage)

    pageHrefs should be (Seq(
      "http://localhost?f=1&t=2",
      "http://localhost?f=1&t=2&start=3",
      "http://localhost?f=1&t=2&start=6",
      "http://localhost?f=1&t=2&start=9"))
  }
}
