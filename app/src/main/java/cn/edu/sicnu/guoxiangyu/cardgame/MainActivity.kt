package cn.edu.sicnu.guoxiangyu.cardgame

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_STUDENT_ID = "extra_student_id"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_HOBBY = "extra_hobby"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_HIGH_SCORE = "extra_high_score"
    }

    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView
    private lateinit var tvHighScore: TextView

    private var name = ""
    private var gender = ""
    private var studentId = ""
    private var phone = ""
    private var email = ""
    private var hobby = ""
    private var highScore = 0
    private var avatarBitmap: Bitmap? = null

    private val personInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                name = data.getStringExtra(EXTRA_NAME) ?: name
                gender = data.getStringExtra(EXTRA_GENDER) ?: gender
                studentId = data.getStringExtra(EXTRA_STUDENT_ID) ?: studentId
                phone = data.getStringExtra(EXTRA_PHONE) ?: phone
                email = data.getStringExtra(EXTRA_EMAIL) ?: email
                hobby = data.getStringExtra(EXTRA_HOBBY) ?: hobby
                @Suppress("DEPRECATION")
                val bitmap = data.getParcelableExtra<Bitmap>(EXTRA_AVATAR)
                if (bitmap != null) {
                    avatarBitmap = bitmap
                    ivAvatar.setImageBitmap(bitmap)
                }
                updateInfoViews()
            }
        }
    }

    private val cardGameLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val gameScore = result.data?.getIntExtra(EXTRA_HIGH_SCORE, 0) ?: 0
            if (gameScore > highScore) {
                highScore = gameScore
            }
            updateInfoViews()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAvatar = findViewById(R.id.ivAvatar)
        tvName = findViewById(R.id.tvName)
        tvGender = findViewById(R.id.tvGender)
        tvStudentId = findViewById(R.id.tvStudentId)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)
        tvHobby = findViewById(R.id.tvHobby)
        tvHighScore = findViewById(R.id.tvHighScore)

        name = getString(R.string.default_name)
        gender = getString(R.string.default_gender)
        studentId = getString(R.string.default_student_id)
        phone = getString(R.string.default_phone)
        email = getString(R.string.default_email)
        hobby = getString(R.string.default_hobby)

        updateInfoViews()

        findViewById<Button>(R.id.btnEditInfo).setOnClickListener {
            val intent = Intent(this, PersonInfoActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_GENDER, gender)
                putExtra(EXTRA_STUDENT_ID, studentId)
                putExtra(EXTRA_PHONE, phone)
                putExtra(EXTRA_EMAIL, email)
                putExtra(EXTRA_HOBBY, hobby)
                if (avatarBitmap != null) {
                    putExtra(EXTRA_AVATAR, avatarBitmap)
                }
            }
            personInfoLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btnPlayGame).setOnClickListener {
            val intent = Intent(this, CardGameActivity::class.java)
            cardGameLauncher.launch(intent)
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

    private fun updateInfoViews() {
        tvName.text = getString(R.string.info_name_format, name)
        tvGender.text = getString(R.string.info_gender_format, gender)
        tvStudentId.text = getString(R.string.info_student_id_format, studentId)
        tvPhone.text = getString(R.string.info_phone_format, phone)
        tvEmail.text = getString(R.string.info_email_format, email)
        tvHobby.text = getString(R.string.info_hobby_format, hobby)
        tvHighScore.text = getString(R.string.info_high_score_format, highScore)
    }
}
