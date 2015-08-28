package com.chaoslabgames.bigbro.app

import java.net.URL
import java.nio.charset.Charset

import com.chaoslabgames.bigbro.analyzer.TextAnalyzer
import com.chaoslabgames.bigbro.sevinfo.{ForumThreadPageParser, ForumUtils}
import resource._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, _}
import scala.language.postfixOps

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
object ForumThreadAnalyzerApp {
  def main(args:Array[String]):Unit = {
    val threadUrl = args(0)
    val totalPosts = args(1).toInt
    val postsPerPage = args(2).toInt

    val requestTimeOutPerThread = 1000
    val threadCount = 8
    val topTermNum = 100

    val textAnalyzer = new TextAnalyzer
    val pageParser = new ForumThreadPageParser
    val postPages = ForumUtils.calcPageHrefs(threadUrl, totalPosts, postsPerPage)
    val parts = postPages.grouped(postPages.size / threadCount)


    val partFutures = Future.sequence( parts.map( part =>
      Future {
        part.foreach( pageUrl => {
          for (is <- managed(new URL(pageUrl).openStream())) {
            val page = pageParser.scan(is, Charset.forName("utf-8"), threadUrl)
            textAnalyzer.synchronized {

              page.pagePosts.foreach { post =>
                println(s"post message ${post.message}")
                textAnalyzer.push(post.message)
              }
              println(s"processed $pageUrl")
            }
            Thread.sleep(requestTimeOutPerThread)
          }
    }) } ))

    Await.result(partFutures, Duration.Inf)

    println(s"total token count: ${textAnalyzer.totalTokenCount}");
    textAnalyzer.topTerms(topTermNum).zipWithIndex.foreach { case (term, indx) => println(f"${indx + 1} - ${term.name} ${term.value}%2.4f") }
  }
}
