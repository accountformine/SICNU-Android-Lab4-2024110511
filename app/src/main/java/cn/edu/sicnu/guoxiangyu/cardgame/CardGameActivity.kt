package cn.edu.sicnu.guoxiangyu.cardgame

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame

class CardGameActivity : AppCompatActivity() {

    companion object {
        var game: CardMatchingGame = CardMatchingGame(24)
        var maxSessionScore = 0
    }

    lateinit var adapter: CardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        recyclerView = findViewById(R.id.recyclerView)
        scoreTextView = findViewById(R.id.score)
        val resetButton = findViewById<Button>(R.id.reset)
        val exitButton = findViewById<Button>(R.id.btnExit)

        adapter = CardAdapter(game)
        recyclerView.adapter = adapter

        val configure = resources.configuration
        if (configure.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 6)
        }

        adapter.setOnClickListener {
            game.chooseCardAtIndex(it)
            updateUI()
        }

        updateUI()

        resetButton.setOnClickListener {
            game.reset()
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
        adapter.notifyDataSetChanged()
        if (game.score > maxSessionScore) {
            maxSessionScore = game.score
        }
        scoreTextView.text = getString(R.string.score_format, game.score)
    }

    private fun finishWithHighScore() {
        val data = Intent().apply {
            putExtra(MainActivity.EXTRA_HIGH_SCORE, maxSessionScore)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}
