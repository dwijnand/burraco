package burraco

import scala.reflect.macros.blackbox.Context

sealed trait Suit
final case object Clubs    extends Suit
final case object Diamonds extends Suit
final case object Hearts   extends Suit
final case object Spades   extends Suit

sealed trait Rank { override def toString: String }
// TODO: Values or types? eg. Rank(2), Rank(13)..
object Rank {
  final case object _3  extends Rank { override def toString = "3" }
  final case object _4  extends Rank { override def toString = "4" }
  final case object _5  extends Rank { override def toString = "5" }
  final case object _6  extends Rank { override def toString = "6" }
  final case object _7  extends Rank { override def toString = "7" }
  final case object _8  extends Rank { override def toString = "8" }
  final case object _9  extends Rank { override def toString = "9" }
  final case object _10 extends Rank { override def toString = "10" }
}
final case object Jack  extends Rank
final case object Queen extends Rank
final case object King  extends Rank
final case object Ace   extends Rank
final case object Deuce extends Rank

sealed abstract class Card {
  // TODO: Decide if the definition of Wildcard is this or if it's dependant on the Model
  def isWildcard: Boolean = this == Joker || this.rank == Some(Deuce)
}
final case object Joker extends Card
final case class RegularCard(rank: Rank, suit: Suit) extends Card {
  override def toString = rank.toString + " of " + suit
}

object Card extends ((Rank, Suit) => RegularCard) {
  def apply(rank: Rank, suit: Suit): RegularCard = RegularCard(rank, suit)

  implicit class CardOps(private val c: Card) extends AnyVal {
    def rank: Option[Rank] = c match {
      case Joker                => None
      case RegularCard(rank, _) => Some(rank)
    }

    def suit: Option[Suit] = c match {
      case Joker                => None
      case RegularCard(_, suit) => Some(suit)
    }
  }
}

final class IntToRank(private val i: Int) extends AnyVal {
  def rank: Rank = macro IntToRankMacros.rankImpl
}

trait IntToRankMacroFn {
  val c: Context
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

final class IntToRankMacros(val c: Context) extends IntToRankMacroFn {
  import c.universe._

  def rankImpl: Tree = c.prefix.tree match {
    case Apply(_, Seq(Literal(Constant(x)))) => intToRank(x)
  }
}

trait StringToSuitMacroFn {
  val c: Context
  import c.universe._

  def stringToSuit(s: String): Tree = s match {
    case "clubs"    => q"_root_.burraco.Clubs"
    case "diamonds" => q"_root_.burraco.Diamonds"
    case "hearts"   => q"_root_.burraco.Hearts"
    case "spades"   => q"_root_.burraco.Spades"
  }
}

final class IntToCard(private val i: Int) extends AnyVal {
  def    clubs: RegularCard = macro IntToCardMacros.intToCardImpl
  def diamonds: RegularCard = macro IntToCardMacros.intToCardImpl
  def   hearts: RegularCard = macro IntToCardMacros.intToCardImpl
  def   spades: RegularCard = macro IntToCardMacros.intToCardImpl
}

final class IntToCardMacros(val c: Context) extends IntToRankMacroFn with StringToSuitMacroFn {
  import c.universe._

  def intToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(Literal(Constant(x)))), TermName(suitStr)) =>
      q"_root_.burraco.RegularCard(${intToRank(x)}, ${stringToSuit(suitStr)})"
  }
}

final class RankToCard(private val rank: Rank) extends AnyVal {
  def    clubs: RegularCard = macro RankToCardMacros.rankToCardImpl
  def diamonds: RegularCard = macro RankToCardMacros.rankToCardImpl
  def   hearts: RegularCard = macro RankToCardMacros.rankToCardImpl
  def   spades: RegularCard = macro RankToCardMacros.rankToCardImpl
}

final class RankToCardMacros(val c: Context) extends StringToSuitMacroFn {
  import c.universe._

  def rankToCardImpl: Tree = c.macroApplication match {
    case Select(Apply(_, Seq(rank)), TermName(suitStr)) =>
      q"_root_.burraco.RegularCard($rank, ${stringToSuit(suitStr)})"
  }
}

// TODO: toString
sealed trait Meld extends Any {
  def cards: Vector[Card]
  override def toString = cards mkString ("[ ", ", ", " ]")
}

final class Group private (val cards: Vector[Card]) extends AnyVal with Meld {
  override def toString = cards mkString ("[ ", ", ", " ]")
}

object Group {
  def fromVector(cards: Vector[Card]): Option[Group] = {
    if (cards.size < 3) return None

    val (wildcards, nonWildCards) = cards partition (_.isWildcard)

    if (wildcards.size > 1) return None

    Some(new Group(nonWildCards ++ wildcards))
  }
}

//final class Run private(val cards: Vector[Card]) extends AnyVal with Meld {
//  override def toString = cards mkString ("[ ", ", ", " ]")
//}

//object Run {
//  def fromVector(cards: Vector[Card]): Option[Run] = {
//    // At least 3 cards
//    // Up to 1 Wildcard per Meld
//    // Ordered from greatest to lowest
//    ???
//  }
//}

final case class Player()

final class Score(val value: Int) extends AnyVal

final case class Team(
  p1: Player,
  p2: Player,
  score: Score
)

final case class Game(
)

final case class Match(
  team1: Team,
  team2: Team,
  games: Vector[Game]
)
