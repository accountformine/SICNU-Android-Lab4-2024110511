package cn.edu.sicnu.guoxiangyu.cardgame

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ThreeCardGameFragment : Fragment() {

    lateinit var game: CardMatchingGame
    lateinit var adapter: CardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        scoreTextView = view.findViewById(R.id.score)
        val resetButton = view.findViewById<Button>(R.id.reset)

        val rgame = loadData()
        if (rgame != null) {
            game = rgame
        } else {
            game = CardMatchingGame(count = 24, matchCount = 3)
        }

        adapter = CardAdapter(game)
        recyclerView.adapter = adapter

        val configuration = resources.configuration
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(activity, 6)
        } else {
            recyclerView.layoutManager = GridLayoutManager(activity, 4)
        }

        updateUI()

        adapter.setOnCardClickListener { index ->
            if (index != RecyclerView.NO_POSITION) {
                game.chooseCardAtIndex(index)
                updateUI()
            }
        }

        resetButton.setOnClickListener {
            game.reset()
            updateUI()
        }
    }

    fun updateUI() {
        adapter.notifyDataSetChanged()
        scoreTextView.text = getString(R.string.score_format, game.score)
        (activity as? MainActivity)?.updateHighScore(game.score)
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun saveData() {
        if (!::game.isInitialized) return
        try {
            val output = activity?.openFileOutput(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
            if (output != null) {
                val objectOutputStream = ObjectOutputStream(output)
                objectOutputStream.writeObject(game)
                objectOutputStream.close()
                output.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadData(): CardMatchingGame? {
        return try {
            val input = activity?.openFileInput(FILE_NAME) ?: return null
            val objectInputStream = ObjectInputStream(input)
            val loadedGame = objectInputStream.readObject() as CardMatchingGame
            objectInputStream.close()
            input.close()
            loadedGame
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val FILE_NAME = "game_data_3"

        @JvmStatic
        fun newInstance(): ThreeCardGameFragment = ThreeCardGameFragment()
    }
}
