package com.chaoslabgames.bigbro.sevinfo

import java.io.InputStream
import java.nio.charset.Charset

import com.chaoslabgames.bigbro.datavalue.thread.page.{Post, ScanResult}
import org.jsoup.Jsoup

import scala.collection.JavaConversions._

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class ForumThreadPageParser {
  def scan( is:InputStream, charset: Charset, baseUri:String):ScanResult = {
    val doc = Jsoup.parse(is, charset.name(), baseUri)
    val title = doc.select(".titles").text()

    val posts = doc.select("div.postbody").map( element => new Post(message = element.text()))
    //doc.select("tbody > tr > td > div > a[href*=viewtopic]").first().parent()

    new ScanResult(topic = title, posts)
  }
}