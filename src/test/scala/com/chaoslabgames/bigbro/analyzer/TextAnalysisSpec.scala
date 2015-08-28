package com.chaoslabgames.bigbro.analyzer

import org.scalatest._

import scala.collection.Seq

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 27.08.2015.
 */
class TextAnalysisSpec extends FlatSpec with Matchers {
  it should " consume documents" in {
    val analyzer = new TextAnalyzer
    analyzer.push("i want by a Car.")
    analyzer.push("i have the Car.")
    analyzer.totalDocNum should be (2)
  }

  it should "calculate terms count" in {
    val analyzer = new TextAnalyzer

    analyzer.push("Red-Blue-Orange Old Car")

    analyzer.termCount should be (5)
  }

  it should "calculate term occurrence" in {
    val analyzer = new TextAnalyzer

    analyzer.push("is car bad?")
    analyzer.push("Car is good!")

    analyzer.term("car").occurrence should be (2)
    analyzer.term("bad").occurrence should be (1)
    analyzer.term("good").occurrence should be (1)
  }

  it should "calculate top terms" in {
    val analyzer = new TextAnalyzer
    analyzer.push("red")
    analyzer.push("blue")
    analyzer.push("blue") //blue is common word

    analyzer.topTerms(2).map(_.name) should equal (Seq("red", "blue"))
  }

}
