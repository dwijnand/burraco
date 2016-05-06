package burraco

import scala.reflect.macros.blackbox

final class IntToRank(private val i: Int) extends AnyVal {
  def rank: Rank = macro IntToRankMacros.rankImpl
}

final class RankToCard(private val rank: Rank) extends AnyVal {
  def    clubs: RegularCard = macro RankToCardMacros.rankToCardImpl
  def diamonds: RegularCard = macro RankToCardMacros.rankToCardImpl
  def   hearts: RegularCard = macro RankToCardMacros.rankToCardImpl
  def   spades: RegularCard = macro RankToCardMacros.rankToCardImpl
}

final class IntToCard(private val i: Int) extends AnyVal {
  def    clubs: RegularCard = macro IntToCardMacros.intToCardImpl
  def diamonds: RegularCard = macro IntToCardMacros.intToCardImpl
  def   hearts: RegularCard = macro IntToCardMacros.intToCardImpl
  def   spades: RegularCard = macro IntToCardMacros.intToCardImpl
}

trait IntToRankMacroFn {
  val c: blackbox.Context
  import c.universe._

  def intToRank(x: Any): Tree = x match {
    case 1  => q"_root_.burraco.Ace"
    case 2  => q"_root_.burraco.Deuce"
    case 3  => q"_root_.burraco.Rank._3"
    case 4  => q"_root_.burraco.Rank._4"
    case 5  => q"_root_.burraco.Rank._5"
    case 6  => q"_root_.burraco.Rank._6"
    case 7  => q"_root_.burraco.Rank._7"
    case 8  => q"_root_.burraco.Rank._8"
    case 9  => q"_root_.burraco.Rank._9"
    case 10 => q"_root_.burraco.Rank._10"
    case 11 => q"_root_.burraco.Jack"
    case 12 => q"_root_.burraco.Queen"
    case 13 => q"_root_.burraco.King"
  }
}

trait StringToSuitMacroFn {
  val c: blackbox.Context
  import c.universe._

  def stringToSuit(s: String): Tree = s match {
    case "clubs"    => q"_root_.burraco.Clubs"
    case "diamonds" => q"_root_.burraco.Diamonds"
    case "hearts"   => q"_root_.burraco.Hearts"
    case "spades"   => q"_root_.burraco.Spades"
  }
}

final class IntToRankMacros(val c: blackbox.Context) extends IntToRankMacroFn {
  import c.universe._

  def rankImpl: Tree = c.prefix.tree match {
    case Apply(_, Seq(Literal(Constant(x)))) => intToRank(x)
  }
}

final class RankToCardMacros(val c: blackbox.Context) extends StringToSuitMacroFn {
  import c.universe._

  def rankToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(rank)), TermName(suitStr)) =>
      q"_root_.burraco.RegularCard($rank, ${stringToSuit(suitStr)})"
  }
}

final class IntToCardMacros(val c: blackbox.Context) extends IntToRankMacroFn with StringToSuitMacroFn {
  import c.universe._

  def intToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(Literal(Constant(x)))), TermName(suitStr)) =>
      q"_root_.burraco.RegularCard(${intToRank(x)}, ${stringToSuit(suitStr)})"
  }
}
