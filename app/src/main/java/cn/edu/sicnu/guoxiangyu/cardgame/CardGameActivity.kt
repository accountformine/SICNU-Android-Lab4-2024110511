package cn.edu.sicnu.guoxiangyu.cardgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame

class CardGameActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var game: CardMatchingGame
    val cardButtons = mutableListOf<Button>()
    private lateinit var scoreTextView: TextView
    private var maxSessionScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        scoreTextView = findViewById(R.id.score)
        val resetButton = findViewById<Button>(R.id.reset)
        val exitButton = findViewById<Button>(R.id.btnExit)

        for (v in gridLayout.children) {
            if (v is Button) {
                v.setOnClickListener(this)
                cardButtons.add(v)
            }
        }

        game = CardMatchingGame(cardButtons.count())
        updateUI()

        resetButton.setOnClickListener {
            game = CardMatchingGame(cardButtons.count())
            updateUI()
        }

        exitButton.setOnClickListener {
            finishWithHighScore()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishWithHighScore()
            }
        })
    }

    fun updateUI() {
        for (button in cardButtons) {
            val index = cardButtons.indexOf(button)
            val card = game.cardAtIndex(index)
            button.isEnabled = !card.isMatched
            if (card.isChosen) {
                button.text = card.toString()
                button.setBackgroundColor(Color.WHITE)
            } else {
                button.text = getString(R.string.empty_text)
                button.setBackgroundResource(R.drawable.sicnu)
            }
        }
        if (game.score > maxSessionScore) {
            maxSessionScore = game.score
        }
        scoreTextView.text = getString(R.string.score_format, game.score)
    }

    override fun onClick(v: View?) {
        if (v is Button) {
            val index = cardButtons.indexOf(v)
            game.chooseCardAtIndex(index)
            updateUI()
        }
    }

    private fun finishWithHighScore() {
        val data = Intent().apply {
            putExtra(MainActivity.EXTRA_HIGH_SCORE, maxSessionScore)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}
