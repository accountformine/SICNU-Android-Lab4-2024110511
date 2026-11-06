package cn.edu.sicnu.guoxiangyu.cardgame.model

import java.io.Serializable

class PersonInfo(
    var name: String = "",
    var gender: String = "",
    var studentId: String = "",
    var phone: String = "",
    var email: String = "",
    var hobby: String = "",
    var highScore: Int = 0,
    var avatarBytes: ByteArray? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
