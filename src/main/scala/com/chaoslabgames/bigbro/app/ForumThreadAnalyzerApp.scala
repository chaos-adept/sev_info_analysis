package com.chaoslabgames.bigbro.app

import java.net.URL
import java.nio.charset.Charset

import com.chaoslabgames.bigbro.analyzer.TextAnalyzer
import com.chaoslabgames.bigbro.sevinfo.{ForumThreadPageParser, ForumUtils}
import resource._

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
object ForumThreadAnalyzerApp {
  def main(args:Array[String]):Unit = {
    val threadUrl = args(0)
    val totalPosts = args(1).toInt
    val postsPerPage = args(2).toInt

    val textAnalyzer = new TextAnalyzer
    val pageParser = new ForumThreadPageParser
    val postPages = ForumUtils.calcPageHrefs(threadUrl, totalPosts, postsPerPage)
    postPages.foreach( pageUrl => {
        for (is <- managed(new URL(pageUrl).openStream())) {
          val page = pageParser.scan(is, Charset.forName("utf-8"), threadUrl)
          page.pagePosts.foreach { post => textAnalyzer.push(post.message) }
        }
    })


    textAnalyzer.topTerms(15).foreach( term => println(f"${term.name} ${term.value}%2.2f") )
  }
}
