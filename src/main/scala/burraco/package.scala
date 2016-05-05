package object burraco {
  implicit def intToRank(i: Int): IntToRank = new IntToRank(i)
  implicit def intToCard(i: Int): IntToCard = new IntToCard(i)
  implicit def rankToCard(rank: Rank): RankToCard = new RankToCard(rank)
}
