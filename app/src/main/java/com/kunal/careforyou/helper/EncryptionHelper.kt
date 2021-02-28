package com.kunal.careforyou.helper

import android.util.Base64
import java.security.MessageDigest
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Ahsen Saeed on 1/10/2018.
 */
class EncryptionHelper private constructor() {
    private var encryptionKey: String? = null

    companion object {
        private var encryptionHelper: EncryptionHelper? = null
        val instance: EncryptionHelper?
            get() {
                if (encryptionHelper == null) {
                    encryptionHelper = EncryptionHelper()
                }
                return encryptionHelper
            }
    }

    fun getSecreteKey(secretKey: String): SecretKey {
        val md = MessageDigest.getInstance("SHA-1")
        val digestOfPassword = md.digest(secretKey.toByteArray(charset("UTF-8")))
        val keyBytes = Arrays.copyOf(digestOfPassword, 24)
        return SecretKeySpec(keyBytes, "AES")
    }

    fun encryptMsg(): String {
        return Base64.encodeToString(encryptionKey!!.toByteArray(), Base64.DEFAULT)
    }

    fun encryptionString(encryptionKey: String?): EncryptionHelper? {
        this.encryptionKey = encryptionKey
        return encryptionHelper
    }

    fun getDecryptionString(encryptedText: String): String {
        return String(Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT))
    }
}