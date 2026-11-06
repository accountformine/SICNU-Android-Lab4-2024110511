package cn.edu.sicnu.guoxiangyu.cardgame

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.edu.sicnu.guoxiangyu.cardgame.model.PersonInfo
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class PersonInfoFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView
    private lateinit var tvHighScore: TextView

    lateinit var personInfo: PersonInfo

    private val personInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                personInfo.name = data.getStringExtra(MainActivity.EXTRA_NAME) ?: personInfo.name
                personInfo.gender = data.getStringExtra(MainActivity.EXTRA_GENDER) ?: personInfo.gender
                personInfo.studentId = data.getStringExtra(MainActivity.EXTRA_STUDENT_ID) ?: personInfo.studentId
                personInfo.phone = data.getStringExtra(MainActivity.EXTRA_PHONE) ?: personInfo.phone
                personInfo.email = data.getStringExtra(MainActivity.EXTRA_EMAIL) ?: personInfo.email
                personInfo.hobby = data.getStringExtra(MainActivity.EXTRA_HOBBY) ?: personInfo.hobby
                val avatarBytes = data.getByteArrayExtra(MainActivity.EXTRA_AVATAR_BYTES)
                if (avatarBytes != null) {
                    personInfo.avatarBytes = avatarBytes
                }
                saveData()
                updateUI()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivAvatar = view.findViewById(R.id.ivAvatar)
        tvName = view.findViewById(R.id.tvName)
        tvGender = view.findViewById(R.id.tvGender)
        tvStudentId = view.findViewById(R.id.tvStudentId)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvHobby = view.findViewById(R.id.tvHobby)
        tvHighScore = view.findViewById(R.id.tvHighScore)

        val loadedInfo = loadData()
        if (loadedInfo != null) {
            personInfo = loadedInfo
        } else {
            personInfo = PersonInfo(
                name = getString(R.string.default_name),
                gender = getString(R.string.default_gender),
                studentId = getString(R.string.default_student_id),
                phone = getString(R.string.default_phone),
                email = getString(R.string.default_email),
                hobby = getString(R.string.default_hobby),
                highScore = 0
            )
            saveData()
        }

        updateUI()

        view.findViewById<Button>(R.id.btnEditInfo).setOnClickListener {
            val intent = Intent(requireContext(), PersonInfoActivity::class.java).apply {
                putExtra(MainActivity.EXTRA_NAME, personInfo.name)
                putExtra(MainActivity.EXTRA_GENDER, personInfo.gender)
                putExtra(MainActivity.EXTRA_STUDENT_ID, personInfo.studentId)
                putExtra(MainActivity.EXTRA_PHONE, personInfo.phone)
                putExtra(MainActivity.EXTRA_EMAIL, personInfo.email)
                putExtra(MainActivity.EXTRA_HOBBY, personInfo.hobby)
                if (personInfo.avatarBytes != null) {
                    putExtra(MainActivity.EXTRA_AVATAR_BYTES, personInfo.avatarBytes)
                }
            }
            personInfoLauncher.launch(intent)
        }
    }

    private fun updateUI() {
        tvName.text = getString(R.string.info_name_format, personInfo.name)
        tvGender.text = getString(R.string.info_gender_format, personInfo.gender)
        tvStudentId.text = getString(R.string.info_student_id_format, personInfo.studentId)
        tvPhone.text = getString(R.string.info_phone_format, personInfo.phone)
        tvEmail.text = getString(R.string.info_email_format, personInfo.email)
        tvHobby.text = getString(R.string.info_hobby_format, personInfo.hobby)
        tvHighScore.text = getString(R.string.info_high_score_format, personInfo.highScore)

        val bytes = personInfo.avatarBytes
        if (bytes != null && bytes.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            if (bitmap != null) {
                ivAvatar.setImageBitmap(bitmap)
            }
        } else {
            ivAvatar.setImageResource(R.drawable.sicnu)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun saveData() {
        if (!::personInfo.isInitialized) return
        try {
            val output = activity?.openFileOutput(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
            if (output != null) {
                val objectOutputStream = ObjectOutputStream(output)
                objectOutputStream.writeObject(personInfo)
                objectOutputStream.close()
                output.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadData(): PersonInfo? {
        return try {
            val input = activity?.openFileInput(FILE_NAME) ?: return null
            val objectInputStream = ObjectInputStream(input)
            val info = objectInputStream.readObject() as PersonInfo
            objectInputStream.close()
            input.close()
            info
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val FILE_NAME = "person_data"

        @JvmStatic
        fun newInstance(): PersonInfoFragment = PersonInfoFragment()
    }
}
