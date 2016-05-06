package burraco

import scala.reflect.macros.whitebox

final class IntToRank(private val i: Int) extends AnyVal {
  def rank: Rank = macro CardMacros.intToRankImpl
}

final class RankToCard(private val rank: Rank) extends AnyVal {
  def    clubs: RegularCard = macro CardMacros.rankToCardImpl
  def diamonds: RegularCard = macro CardMacros.rankToCardImpl
  def   hearts: RegularCard = macro CardMacros.rankToCardImpl
  def   spades: RegularCard = macro CardMacros.rankToCardImpl
}

final class IntToCard(private val i: Int) extends AnyVal {
  def    clubs: RegularCard = macro CardMacros.intToCardImpl
  def diamonds: RegularCard = macro CardMacros.intToCardImpl
  def   hearts: RegularCard = macro CardMacros.intToCardImpl
  def   spades: RegularCard = macro CardMacros.intToCardImpl
}

final class CardMacros(val c: whitebox.Context) {
  import c.universe._

  def intToRankImpl: Tree = c.prefix.tree match {
    case Apply(_, Seq(Literal(Constant(x)))) => intToRank(x)
  }

  def rankToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(rank)), TermName(suitStr)) => createCard(rank, suitStr)
  }

  def intToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(x)), suit: TermName) => q"$x.rank.$suit"
  }

  private def createCard(rank: Tree, suitStr: String): Tree =
    q"_root_.burraco.RegularCard($rank, ${stringToSuit(suitStr)})"

  private def intToRank(x: Any): Tree = x match {
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

  private def stringToSuit(s: String): Tree = s match {
    case "clubs"    => q"_root_.burraco.Clubs"
    case "diamonds" => q"_root_.burraco.Diamonds"
    case "hearts"   => q"_root_.burraco.Hearts"
    case "spades"   => q"_root_.burraco.Spades"
  }
}
