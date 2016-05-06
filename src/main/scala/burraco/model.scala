package burraco

sealed trait Suit
final case object Clubs    extends Suit { override def toString = "\u2663" }
final case object Diamonds extends Suit { override def toString = "\u2666" }
final case object Hearts   extends Suit { override def toString = "\u2665" }
final case object Spades   extends Suit { override def toString = "\u2660" }

object Suit {
  def values = Vector(Clubs, Diamonds, Hearts, Spades)
}

sealed trait Rank
object Rank {
  def values = Vector(Ace, Deuce, _3, _4, _5, _6, _7, _8, _9, _10, Jack, Queen, King)

  final case object _3  extends Rank { override def toString = "3" }
  final case object _4  extends Rank { override def toString = "4" }
  final case object _5  extends Rank { override def toString = "5" }
  final case object _6  extends Rank { override def toString = "6" }
  final case object _7  extends Rank { override def toString = "7" }
  final case object _8  extends Rank { override def toString = "8" }
  final case object _9  extends Rank { override def toString = "9" }
  final case object _10 extends Rank { override def toString = "10" }
}
final case object Jack  extends Rank { override def toString = "J" }
final case object Queen extends Rank { override def toString = "Q" }
final case object King  extends Rank { override def toString = "K" }
final case object Ace   extends Rank { override def toString = "A" }
final case object Deuce extends Rank { override def toString = "2" }

sealed abstract class Card {
  // TODO: Decide if the definition of Wildcard is this or if it's dependant on the Model
  def isWildcard: Boolean = this == Joker || (this.rank contains Deuce)
}
final case object Joker extends Card { override def toString = "\u2605" }
final case class RegularCard(rank: Rank, suit: Suit) extends Card { override def toString = "" + rank + suit }

object Card extends ((Rank, Suit) => RegularCard) {
  def apply(rank: Rank, suit: Suit): RegularCard = RegularCard(rank, suit)
  def unapply(c: Card): Option[(Rank, Suit)] = c match {
    case Joker                   => None
    case RegularCard(rank, suit) => Some((rank, suit))
  }

  def values: Vector[Card] = (for { s <- Suit.values; r <- Rank.values } yield Card(r, s)) :+ Joker

  implicit class CardOps(private val c: Card) extends AnyVal {
    def rank: Option[Rank] = Card unapply c map (_._1)
    def suit: Option[Suit] = Card unapply c map (_._2)
  }
}

sealed trait Meld extends Any {
  def cards: Vector[Card]
  override def toString = cards mkString ("[ ", ", ", " ]")
}

final class Group private (val cards: Vector[Card]) extends AnyVal with Meld

object Group {
  def fromVector(cards: Vector[Card]): Option[Group] = {
    if (cards.size < 3) return None

    val (wildcards, nonWildCards) = cards partition (_.isWildcard)

    if (wildcards.size > 1) return None

    Some(new Group(nonWildCards ++ wildcards))
  }
}

final class Run private(val cards: Vector[Card]) extends AnyVal with Meld

object Run {
  def fromVector(cards: Vector[Card]): Option[Run] = {
    if (cards.size < 3) return None

    val (wildcards, nonWildCards) = cards partition (_.isWildcard)

    if (wildcards.size > 1) return None

    // Contains cards in sequential order, with optional Joker
    // Ordered from greatest to lowest
    ???
  }
}

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
