package cn.edu.sicnu.guoxiangyu.cardgame

import cn.edu.sicnu.guoxiangyu.cardgame.model.Card
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame
import cn.edu.sicnu.guoxiangyu.cardgame.model.Deck
import cn.edu.sicnu.guoxiangyu.cardgame.model.PersonInfo
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class CardMatchingGameUnitTest {

    @Test
    fun card_twoCardMatch_returnsCorrectScore() {
        val heartA = Card("♥", "A")
        val spadeA = Card("♠", "A")
        val heartK = Card("♥", "K")
        val club2 = Card("♣", "2")

        assertEquals(4, heartA.match(arrayOf(spadeA)))
        assertEquals(1, heartA.match(arrayOf(heartK)))
        assertEquals(0, heartA.match(arrayOf(club2)))
    }

    @Test
    fun card_threeCardMatch_returnsCorrectScore() {
        val heartA = Card("♥", "A")
        val spadeA = Card("♠", "A")
        val clubA = Card("♣", "A")
        val heartK = Card("♥", "K")
        val heartQ = Card("♥", "Q")
        val club2 = Card("♣", "2")

        assertEquals(8, heartA.match(arrayOf(spadeA, clubA)))
        assertEquals(2, heartA.match(arrayOf(heartK, heartQ)))
        assertEquals(4, heartA.match(arrayOf(spadeA, club2)))
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
    fun game_twoAndThreeCardModes_updateStateAndScore() {
        val game2 = CardMatchingGame(count = 24, matchCount = 2)
        assertEquals(24, game2.cards.size)
        assertEquals(2, game2.matchCount)
        game2.chooseCardAtIndex(0)
        assertTrue(game2.cardAtIndex(0).isChosen)
        assertEquals(-1, game2.score)

        val game3 = CardMatchingGame(count = 24, matchCount = 3)
        assertEquals(24, game3.cards.size)
        assertEquals(3, game3.matchCount)
        game3.chooseCardAtIndex(0)
        game3.chooseCardAtIndex(1)
        // Only 2 cards chosen in 3-card mode -> neither matched nor penalized yet
        assertTrue(game3.cardAtIndex(0).isChosen)
        assertTrue(game3.cardAtIndex(1).isChosen)
        assertEquals(-2, game3.score)

        game3.reset()
        assertEquals(0, game3.score)
        assertFalse(game3.cardAtIndex(0).isChosen)
    }

    @Test
    fun serialization_gameAndPersonInfo_roundTripPreservesState() {
        val originalGame = CardMatchingGame(count = 24, matchCount = 3)
        originalGame.chooseCardAtIndex(2)
        originalGame.chooseCardAtIndex(5)

        val gameOut = ByteArrayOutputStream()
        ObjectOutputStream(gameOut).use { it.writeObject(originalGame) }
        val restoredGame = ObjectInputStream(ByteArrayInputStream(gameOut.toByteArray())).use {
            it.readObject() as CardMatchingGame
        }

        assertEquals(originalGame.score, restoredGame.score)
        assertEquals(originalGame.matchCount, restoredGame.matchCount)
        assertEquals(originalGame.cardAtIndex(2).toString(), restoredGame.cardAtIndex(2).toString())
        assertTrue(restoredGame.cardAtIndex(2).isChosen)
        assertTrue(restoredGame.cardAtIndex(5).isChosen)

        val avatarSample = byteArrayOf(1, 2, 3, 4)
        val originalPerson = PersonInfo(
            name = "TestUser",
            gender = "男",
            studentId = "20240001",
            phone = "13800138000",
            email = "test@example.com",
            hobby = "阅读",
            highScore = 28,
            avatarBytes = avatarSample
        )

        val personOut = ByteArrayOutputStream()
        ObjectOutputStream(personOut).use { it.writeObject(originalPerson) }
        val restoredPerson = ObjectInputStream(ByteArrayInputStream(personOut.toByteArray())).use {
            it.readObject() as PersonInfo
        }

        assertEquals("TestUser", restoredPerson.name)
        assertEquals(28, restoredPerson.highScore)
        assertArrayEquals(avatarSample, restoredPerson.avatarBytes)
    }
}
