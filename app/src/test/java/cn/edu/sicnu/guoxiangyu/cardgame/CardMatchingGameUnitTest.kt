package cn.edu.sicnu.guoxiangyu.cardgame

import cn.edu.sicnu.guoxiangyu.cardgame.model.Card
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame
import cn.edu.sicnu.guoxiangyu.cardgame.model.Deck
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardMatchingGameUnitTest {

    @Test
    fun card_threeCardMatch_returnsCorrectScore() {
        val heartA = Card("♥", "A")
        val spadeA = Card("♠", "A")
        val clubA = Card("♣", "A")
        val heartK = Card("♥", "K")
        val heartQ = Card("♥", "Q")
        val club2 = Card("♣", "2")

        // 3 cards with same rank -> 8 points
        assertEquals(8, heartA.match(arrayOf(spadeA, clubA)))
        // 3 cards with same suit -> 2 points
        assertEquals(2, heartA.match(arrayOf(heartK, heartQ)))
        // 2 of 3 cards with same rank -> 4 points
        assertEquals(4, heartA.match(arrayOf(spadeA, club2)))
        // No match -> 0 points
        assertEquals(0, heartA.match(arrayOf(heartK, club2)))
    }

    @Test
    fun deck_drawAll52Cards_exhaustsDeck() {
        val deck = Deck()
        val drawn = mutableSetOf<String>()
        for (i in 1..52) {
            val c = deck.drawRandomCard()
            assertNotNull(c)
            drawn.add(c.toString())
        }
        assertEquals(52, drawn.size)
        assertNull(deck.drawRandomCard())
    }

    @Test
    fun game_chooseToggleAndReset_updatesStateAndScore() {
        val game = CardMatchingGame(24)
        assertEquals(24, game.cards.size)
        assertEquals(0, game.score)

        game.chooseCardAtIndex(0)
        assertTrue(game.cardAtIndex(0).isChosen)
        assertEquals(-1, game.score)

        // Toggling the same card flips it back without extra cost
        game.chooseCardAtIndex(0)
        assertFalse(game.cardAtIndex(0).isChosen)
        assertEquals(-1, game.score)

        // Reset restores 24 fresh cards and resets score to 0
        game.reset()
        assertEquals(24, game.cards.size)
        assertEquals(0, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }
}
