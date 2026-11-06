package cn.edu.sicnu.guoxiangyu.cardgame.model

import java.io.Serializable

class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    fun match(otherCards: Array<Card>): Int {
        var score = 0
        if (otherCards.size == 2) {
            val c1 = otherCards[0]
            val c2 = otherCards[1]
            if (rank == c1.rank && rank == c2.rank) {
                score = 8
            } else if (suit == c1.suit && suit == c2.suit) {
                score = 2
            } else if (rank == c1.rank || rank == c2.rank || c1.rank == c2.rank) {
                score = 4
            }
        } else if (otherCards.size == 1) {
            val otherCard = otherCards[0]
            if (otherCard.rank == rank) {
                score = 4
            } else if (otherCard.suit == suit) {
                score = 1
            }
        }
        return score
    }
}
