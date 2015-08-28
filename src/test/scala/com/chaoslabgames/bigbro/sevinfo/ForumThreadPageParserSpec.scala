package com.chaoslabgames.bigbro.sevinfo

import java.nio.charset.Charset

import com.chaoslabgames.bigbro.datavalue.thread.page.Post
import org.scalatest.{Matchers, FlatSpec}

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class ForumThreadPageParserSpec extends FlatSpec with Matchers {
  it should " get list of threads " in {
    val scaner = new ForumThreadPageParser
    val stream = this.getClass.getResourceAsStream("/forum/samples/sevinfo/sample_1.html")
    val scanResults = scaner.scan(stream, Charset.forName("utf-8"), "http://localhost")

    scanResults.topic should be ("Подскажите пожалуйста, что делать????")
    scanResults.pagePosts should be (Seq(
        new Post("У меня post1"),
        new Post("Вам сюда post2"),
        new Post("не мягкие post3"),
        new Post("все мягкие спасибо! post4")))
  }
}
