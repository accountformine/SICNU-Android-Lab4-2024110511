package cn.edu.sicnu.guoxiangyu.cardgame

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PersonInfoActivity : AppCompatActivity() {

    private lateinit var ivEditAvatar: ImageView
    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var cbTravel: CheckBox
    private lateinit var cbReading: CheckBox
    private lateinit var cbGaming: CheckBox
    private lateinit var cbShopping: CheckBox

    private var capturedAvatar: Bitmap? = null

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            capturedAvatar = bitmap
            ivEditAvatar.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        ivEditAvatar = findViewById(R.id.ivEditAvatar)
        etName = findViewById(R.id.etName)
        etStudentId = findViewById(R.id.etStudentId)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        cbTravel = findViewById(R.id.cbTravel)
        cbReading = findViewById(R.id.cbReading)
        cbGaming = findViewById(R.id.cbGaming)
        cbShopping = findViewById(R.id.cbShopping)

        etName.setText(intent.getStringExtra(MainActivity.EXTRA_NAME) ?: "")
        etStudentId.setText(intent.getStringExtra(MainActivity.EXTRA_STUDENT_ID) ?: "")
        etPhone.setText(intent.getStringExtra(MainActivity.EXTRA_PHONE) ?: "")
        etEmail.setText(intent.getStringExtra(MainActivity.EXTRA_EMAIL) ?: "")

        val gender = intent.getStringExtra(MainActivity.EXTRA_GENDER) ?: getString(R.string.gender_male)
        if (gender == getString(R.string.gender_female)) {
            rbFemale.isChecked = true
        } else {
            rbMale.isChecked = true
        }

        val hobby = intent.getStringExtra(MainActivity.EXTRA_HOBBY) ?: ""
        cbTravel.isChecked = hobby.contains(getString(R.string.hobby_travel))
        cbReading.isChecked = hobby.contains(getString(R.string.hobby_reading))
        cbGaming.isChecked = hobby.contains(getString(R.string.hobby_gaming))
        cbShopping.isChecked = hobby.contains(getString(R.string.hobby_shopping))

        @Suppress("DEPRECATION")
        val oldAvatar = intent.getParcelableExtra<Bitmap>(MainActivity.EXTRA_AVATAR)
        if (oldAvatar != null) {
            capturedAvatar = oldAvatar
            ivEditAvatar.setImageBitmap(oldAvatar)
        }

        findViewById<ImageButton>(R.id.btnTakePhoto).setOnClickListener {
            takePictureLauncher.launch(null)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val selectedGender = if (rbFemale.isChecked) {
                getString(R.string.gender_female)
            } else {
                getString(R.string.gender_male)
            }

            val hobbies = mutableListOf<String>()
            if (cbTravel.isChecked) hobbies.add(getString(R.string.hobby_travel))
            if (cbReading.isChecked) hobbies.add(getString(R.string.hobby_reading))
            if (cbGaming.isChecked) hobbies.add(getString(R.string.hobby_gaming))
            if (cbShopping.isChecked) hobbies.add(getString(R.string.hobby_shopping))
            val hobbyText = if (hobbies.isEmpty()) {
                getString(R.string.hobby_none)
            } else {
                hobbies.joinToString(getString(R.string.hobby_separator))
            }

            val resultIntent = Intent().apply {
                putExtra(MainActivity.EXTRA_NAME, etName.text.toString().trim())
                putExtra(MainActivity.EXTRA_GENDER, selectedGender)
                putExtra(MainActivity.EXTRA_STUDENT_ID, etStudentId.text.toString().trim())
                putExtra(MainActivity.EXTRA_PHONE, etPhone.text.toString().trim())
                putExtra(MainActivity.EXTRA_EMAIL, etEmail.text.toString().trim())
                putExtra(MainActivity.EXTRA_HOBBY, hobbyText)
                if (capturedAvatar != null) {
                    putExtra(MainActivity.EXTRA_AVATAR, capturedAvatar)
                }
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
