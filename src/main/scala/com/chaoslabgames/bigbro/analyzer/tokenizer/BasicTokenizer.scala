package com.chaoslabgames.bigbro.analyzer.tokenizer

import java.io.{Reader, StringReader}

import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.{TokenStream, Tokenizer}
import org.apache.lucene.analysis.core.LowerCaseTokenizer
import org.apache.lucene.analysis.ru.RussianAnalyzer

import scala.collection.mutable

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
class BasicTokenizer {
  val analyzer = new LowercaseWhitespaceTokenizer(2, 30)

  def tokenize(docText: String):Seq[String] = {
    val tokenList:mutable.MutableList[String] = mutable.MutableList()

    val reader: Reader = new StringReader(docText)
    try {
      val tokenStream: TokenStream = analyzer.tokenStream("contents", reader)
      val term: CharTermAttribute = tokenStream.getAttribute(classOf[CharTermAttribute])

      tokenStream.reset()
      while (tokenStream.incrementToken) {
        tokenList += term.toString
      }
      tokenStream.close()
    }
    tokenList
  }

}
