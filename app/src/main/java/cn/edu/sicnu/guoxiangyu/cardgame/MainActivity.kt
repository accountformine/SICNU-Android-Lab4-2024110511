package cn.edu.sicnu.guoxiangyu.cardgame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.guoxiangyu.cardgame.model.PersonInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_STUDENT_ID = "extra_student_id"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_HOBBY = "extra_hobby"
        const val EXTRA_AVATAR_BYTES = "extra_avatar_bytes"
    }

    val personInfoFragment = PersonInfoFragment.newInstance()
    val twoCardGameFragment = TwoCardGameFragment.newInstance()
    val threeCardGameFragment = ThreeCardGameFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, personInfoFragment)
                .commit()
        }

        val bottomNV = findViewById<BottomNavigationView>(R.id.bottomNV)
        bottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_person -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, personInfoFragment)
                        .commit()
                    true
                }
                R.id.nav_game_two -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, twoCardGameFragment)
                        .commit()
                    true
                }
                R.id.nav_game_three -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, threeCardGameFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    fun updateHighScore(newScore: Int) {
        if (newScore <= 0) return
        try {
            val info = try {
                val input = openFileInput(PersonInfoFragment.FILE_NAME)
                val ois = ObjectInputStream(input)
                val loaded = ois.readObject() as PersonInfo
                ois.close()
                input.close()
                loaded
            } catch (e: Exception) {
                PersonInfo(
                    name = getString(R.string.default_name),
                    gender = getString(R.string.default_gender),
                    studentId = getString(R.string.default_student_id),
                    phone = getString(R.string.default_phone),
                    email = getString(R.string.default_email),
                    hobby = getString(R.string.default_hobby),
                    highScore = 0
                )
            }
            if (newScore > info.highScore) {
                info.highScore = newScore
                val output = openFileOutput(PersonInfoFragment.FILE_NAME, MODE_PRIVATE)
                val oos = ObjectOutputStream(output)
                oos.writeObject(info)
                oos.close()
                output.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_about) {
            AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_message)
                .setPositiveButton(R.string.btn_ok, null)
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
