// Copyright 2017-2022 F5 Inc.
// Licensed under the MIT license; see LICENSE.md for more information.

package com.threatstack.blog

import org.joda.time.DateTime
import org.parboiled2._

sealed trait SlackSearchAst
object SlackSearchAst {
  case class SearchTerm(term: String) extends SlackSearchAst
  case class SearchInChannel(search: SearchTerm, channel: String) extends SlackSearchAst
  case class SearchMessagesFrom(searchTerm: SearchTerm, user: String) extends SlackSearchAst
  case class SearchOnDate(searchTerm: SearchTerm, dateTime: DateTime) extends SlackSearchAst
}

/** An example of using Parboiled to parse the Slack Search Expression */
class SlackAssistedSearchParser(val input: ParserInput) extends Parser {
  import SlackSearchAst._

  def InputLine = rule { SearchFilter ~ EOI }

  def SearchFilter: Rule1[SlackSearchAst] = rule {
    Search ~ DateFilter ~> SearchOnDate       |
    Search ~ InChannel  ~> SearchInChannel    |
    Search ~ FromFilter ~> SearchMessagesFrom |
    Search
  }

  def DateFilter: Rule1[DateTime] = rule {
    "on" ~ ':' ~ runSubParser(new DateParser(_).InputLine)
  }

  def Search = rule(Term ~> SearchTerm)

  def InChannel: Rule1[String] = rule("in" ~ ":" ~ Term)

  def FromFilter: Rule1[String] = rule("from" ~ ":" ~ Term)

  def Term: Rule1[String] = rule { capture(oneOrMore(CharPredicate.AlphaNum)) ~ WhiteSpace }

  def WhiteSpace = rule(zeroOrMore(WhiteSpaceChar))
  def WhiteSpaceChar = CharPredicate(" ")
}

class DateParser(val input: ParserInput) extends Parser {
  import CharPredicate._

  def InputLine = rule(Date ~ EOI)

  def Date: Rule1[DateTime] = rule {
    Month ~ "/" ~ Day ~ "/" ~ Year ~> dateTime _ |
    Year ~ "/" ~ Month ~ "/" ~ Day ~> ((year: Int, month: Int, day: Int) => dateTime(month, day, year))
  }

  def Day: Rule1[Int] = Digit2
  def Month: Rule1[Int] = Digit2
  def Year: Rule1[Int] = Digit4

  def Digit2: Rule1[Int] = rule(capture(2.times(Digit)) ~> ((_: String).toInt))
  def Digit4: Rule1[Int] = rule(capture(4.times(Digit)) ~> ((_: String).toInt))

  private def dateTime(month: Int, day: Int, year: Int): DateTime =
    new DateTime(year, month, day, 0, 0, 0)
}
