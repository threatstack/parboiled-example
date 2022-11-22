// Copyright 2017-2022 F5 Inc.
// Licensed under the MIT license; see LICENSE.md for more information.

package com.threatstack.blog

import com.threatstack.blog.SlackSearchAst.{SearchInChannel, SearchOnDate, SearchMessagesFrom, SearchTerm}
import org.joda.time.DateTime
import org.parboiled2.ParseError
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class ParboiledSlackSpec extends FlatSpec with Matchers {

  "The Slack Search Filter" should "parse a term" in {
    val term = "hello"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run().get
    parsed shouldEqual SearchTerm("hello")
  }

  it should "not parse an empty input" in {
    val term = ""
    val parsed = new SlackAssistedSearchParser(term).InputLine.run()
    assert(parsed.isFailure)
  }

  it should "parse a Channel Filter" in {
    val term = "detective in:bigthings"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run().get
    parsed shouldEqual SearchInChannel(SearchTerm("detective"), "bigthings")
  }

  it should "not parse a Channel Filter without a Search Term" in {
    val term = "in:bigthings"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run()
    assert(parsed.isFailure)
  }

  it should "parse a From Filter" in {
    val term = "dangerzone from:thedetective"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run().get
    parsed shouldEqual SearchMessagesFrom(SearchTerm("dangerzone"), "thedetective")
  }

  it should "not parse a From Filter without a Search Term" in {
    val term = "from:thedetective"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run()
    assert(parsed.isFailure)
  }

  it should "parse a US Date Filter" in {
    val term = "hello on:12/10/2016"
    val expectedTime = new DateTime(2016, 12, 10, 0, 0, 0)
    val parser = new SlackAssistedSearchParser(term)
    parser.InputLine.run() match {
      case Failure(error: ParseError) => fail(parser.formatError(error))
      case Failure(error) => fail(error)
      case Success(parsed) =>
        parsed shouldEqual SearchOnDate(SearchTerm("hello"), expectedTime)
    }
  }

  it should "parse an Internaional Date Filter" in {
    val term = "hello on:2016/12/10"
    val expectedTime = new DateTime(2016, 12, 10, 0, 0, 0)
    val parser = new SlackAssistedSearchParser(term)
    parser.InputLine.run() match {
      case Failure(error: ParseError) => fail(parser.formatError(error))
      case Failure(error) => fail(error)
      case Success(parsed) =>
        parsed shouldEqual SearchOnDate(SearchTerm("hello"), expectedTime)
    }
  }

  it should "not parse a US Date Filter without a Search Term" in {
    val term = "on:10/12/2016"
    val parsed = new SlackAssistedSearchParser(term).InputLine.run()
    assert(parsed.isFailure)
  }

  "Dates" should "be parsed" in {
    val date = "12/10/2016"
    val parsed = new DateParser(date).InputLine.run()
    assert(parsed.isSuccess)
    parsed.get shouldEqual new DateTime(2016, 12, 10, 0, 0, 0)
  }

  it should "also work internationally" in {
    val date = "2016/12/10"
    val parsed = new DateParser(date).InputLine.run()
    assert(parsed.isSuccess)
    parsed.get shouldEqual new DateTime(2016, 12, 10, 0, 0, 0)
  }
}
