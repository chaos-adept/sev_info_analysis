package com.chaoslabgames.bigbro.sevinfo

import java.nio.charset.Charset

import org.scalatest.{Matchers, FlatSpec}

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class ForumThreadScannerSpec extends FlatSpec with Matchers {
  it should " get list of threads " in {
    val scaner = new ForumThreadScaner
    val stream = this.getClass.getResourceAsStream("/forum/samples/sevinfo/sample_1.html")
    val scanResults = scaner.scan(stream, Charset.forName("utf-8"), "http://localhost")

    scanResults.topic should be ("Китайский язык.")
    scanResults.totalPosts should be (44)
  }
}
