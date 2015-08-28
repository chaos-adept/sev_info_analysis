package com.chaoslabgames.bigbro.app

import java.io.File
import java.net.{URLEncoder, URL}
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger

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

    val requestTimeOutPerThread = 500
    val threadCount = 8
    val topTermNum = 200

    val textAnalyzer = new TextAnalyzer
    val pageParser = new ForumThreadPageParser
    val postPages = ForumUtils.calcPageHrefs(threadUrl, totalPosts, postsPerPage)
    val parts = postPages.grouped(postPages.size / threadCount)

    val counter = new AtomicInteger()
    val partFutures = Future.sequence( parts.map( part =>
      Future {
        part.foreach( pageUrl => {
          for (is <- managed(new URL(pageUrl).openStream())) { //fixme proxy or cache into dump
            val page = pageParser.scan(is, Charset.forName("utf-8"), threadUrl)
            textAnalyzer.synchronized {
              page.pagePosts.foreach { post =>
                textAnalyzer.push(post.message)
              }
            }
            counter.addAndGet(page.pagePosts.size)
            println(s"processed ${counter.get()} of $totalPosts")
            Thread.sleep(requestTimeOutPerThread)
          }
    }) } ))

    Await.result(partFutures, Duration.Inf)

    println(s"total token count: ${textAnalyzer.totalTokenCount}");
    textAnalyzer.topTerms(topTermNum).zipWithIndex.foreach { case (term, indx) => println(f"${indx + 1} - ${term.name} ${term.value}%2.4f") }

    Seq("путин", "ватник", "укр").map(token => textAnalyzer.term(token)).foreach( term => println(f"${term.name} ${term.docNum}%2.4f") )

  }
}
