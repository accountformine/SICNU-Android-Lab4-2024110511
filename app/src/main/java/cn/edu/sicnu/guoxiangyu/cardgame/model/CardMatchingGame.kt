package cn.edu.sicnu.guoxiangyu.cardgame.model

import java.io.Serializable

class CardMatchingGame(val count: Int, val matchCount: Int = 2) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    var score = 0
        private set

    val cards: MutableList<Card>

    init {
        val deck = Deck()
        cards = mutableListOf()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun reset() {
        val deck = Deck()
        score = 0
        cards.clear()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun cardAtIndex(index: Int): Card {
        return cards[index]
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (!card.isMatched) {
            if (card.isChosen) {
                card.isChosen = false
            } else {
                val chosenCards = mutableListOf<Card>()
                for (otherCard in cards) {
                    if (otherCard.isChosen && !otherCard.isMatched) {
                        chosenCards.add(otherCard)
                    }
                }
                if (chosenCards.size == matchCount - 1) {
                    val matchScore = card.match(chosenCards.toTypedArray())
                    if (matchScore > 0) {
                        score += matchScore * MATCH_BONUS
                        card.isMatched = true
                        chosenCards.forEach { it.isMatched = true }
                    } else {
                        score -= MISMATCH_PENALTY
                        chosenCards.forEach { it.isChosen = false }
                    }
                }
                score -= COST_TO_CHOOSE
                card.isChosen = true
            }
        }
    }
}
