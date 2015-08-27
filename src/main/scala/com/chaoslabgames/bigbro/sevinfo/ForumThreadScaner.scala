package com.chaoslabgames.bigbro.sevinfo

import java.io.InputStream
import java.nio.charset.Charset
import java.util.regex.Pattern

import com.chaoslabgames.bigbro.datavalue.thread.ScanResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.util.matching.Regex


/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class ForumThreadScaner {
  def scan(is: InputStream, charset: Charset, baseUri:String):ScanResult = {
    val doc:Document = Jsoup.parse(is, charset.name(), baseUri)
    val title = doc.select(".titles").text()
    val totalCountRegEx = """\s?\[ Сообщений: (\d+) \]\s?""".r
    val docCountElements = doc.getElementsMatchingOwnText(totalCountRegEx.toString())
    if (docCountElements.size() == 0) {
      throw new IllegalStateException("Message count tags couldn't be found")
    }

    var totalPages = 0
    val totalCountOpt = totalCountRegEx.findFirstMatchIn(docCountElements.first().text())
    if (totalCountOpt.isEmpty) {
      throw new IllegalStateException("totalCount couldn't be parsed.")
    } else {
      totalPages = totalCountOpt.get.group(1).toInt
    }

    new ScanResult(topic = title, totalPosts = totalPages)
  }
}
