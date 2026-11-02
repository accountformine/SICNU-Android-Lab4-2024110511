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
    fun card_matchRankAndSuit_returnsCorrectScore() {
        val heartA = Card("♥", "A")
        val spadeA = Card("♠", "A")
        val heartK = Card("♥", "K")
        val club2 = Card("♣", "2")

        assertEquals(4, heartA.match(arrayOf(spadeA)))
        assertEquals(1, heartA.match(arrayOf(heartK)))
        assertEquals(0, heartA.match(arrayOf(club2)))
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
    fun game_chooseAndToggleCard_updatesStateAndScore() {
        val game = CardMatchingGame(16)
        assertEquals(0, game.score)

        game.chooseCardAtIndex(0)
        assertTrue(game.cardAtIndex(0).isChosen)
        assertEquals(-1, game.score)

        // Toggling the same card flips it back without extra cost
        game.chooseCardAtIndex(0)
        assertFalse(game.cardAtIndex(0).isChosen)
        assertEquals(-1, game.score)
    }
}
