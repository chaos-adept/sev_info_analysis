package com.chaoslabgames.bigbro.analyzer

import com.chaoslabgames.bigbro.analyzer.tokenizer.BasicTokenizer
import com.chaoslabgames.bigbro.datavalue.{TermWeight, Term}

import scala.collection.mutable

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class TextAnalyzer {
  var totalDocNum = 0
  val termDict = new mutable.HashMap[String, Term]
  val tokenizer = new BasicTokenizer

  def push(docText: String) = {
    totalDocNum += 1

    val tokens = tokenizer.tokenize(docText)

    tokens.foreach { token =>
      val term = termDict.getOrElseUpdate(token, new Term(token, 0, 0))
      term.occurrence += 1
    }

    //to binary occurrence
    tokens.toSet[String].foreach { token =>
      termDict.get(token).get.docNum += 1
    }
  }

  def termCount():Int = {
    termDict.size
  }

  def term(term:String):Term = {
    termDict.getOrElse(term, new Term(term, 0, 0))
  }

  def topTerms(num:Int):Seq[TermWeight] = {
    //calc basic tf idf for each term
    val termWeights = termDict.mapValues( term => Math.log10(term.occurrence) * (1 - term.docNum.toDouble/totalDocNum.toDouble)).toSeq
    val tops = termWeights.sortBy(_._2).reverse.take(num).map( it => new TermWeight(name = it._1, value = it._2) )
    tops.toSeq
  }

}



