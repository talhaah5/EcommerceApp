package com.example.ecommerceapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.ecommerceapplication.models.User
import com.google.gson.Gson
import com.example.ecommerceapplication.global.Global
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.GeneralSecurityException

@RequiresApi(Build.VERSION_CODES.M)
class SharedPrefs(context: Context) {


    private val MY_PREFS_NAME = "com.example.ecommerce-21-10-2021"

    private var sharedPreferences: SharedPreferences? = null
    private var masterKeyAlias: String? = null

    init {

        run {
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                MY_PREFS_NAME, masterKeyAlias!!, context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    fun getStrings(key: String?): String? {
        return sharedPreferences!!.getString(key, null)
    }

    fun putString(
        key: String?,
        value: String?
    ) {
        sharedPreferences!!.edit().putString(key, value).apply()
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun putBoolean(
        key: String?,
        value: Boolean
    ) {
        sharedPreferences!!.edit().putBoolean(key, value).apply()
    }

    fun putUser(user: User?) {
        val gson = Gson()
        val json = gson.toJson(user)
        sharedPreferences!!.edit().putString(Global.user, json).apply()
    }


    fun getUser(): User? {
        val gson = Gson()
        val json = sharedPreferences!!.getString(Global.user, null)
        return gson.fromJson(json, User::class.java)
    }

    fun putJsonObject(key: String?, jsonObject: JSONObject) {
        sharedPreferences!!.edit().putString(key, jsonObject.toString()).apply()
    }

    fun getJsonObject(key: String?): JSONObject? {

        try {
            val value: String = sharedPreferences?.getString(key, null).toString()
            return if (value.isNotEmpty() && value != "null")
                JSONObject(value)
            else
                null

        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getJsonObject: ", e)
        }

        return null
    }
    fun updateCart(cart:JSONArray){
        sharedPreferences!!.edit().putString(Global.cart, cart.toString()).apply()
    }
    fun getCart(): JSONArray? {
        try {

            val value: String = sharedPreferences?.getString(Global.cart, null).toString()
            return if (value.isNotEmpty() && value != "null")
                JSONArray(value)
            else
                null
        } catch (e: Exception) {
            Log.e(TAG, "getCart: ", e)
        }

        return null;
    }



    fun clear() {
        sharedPreferences!!.edit().remove("user").apply()
    }

    fun clearSpecificKey(key: String?) {
        sharedPreferences!!.edit().remove(key).apply()
    }


    fun registerPrefListener(listener: OnSharedPreferenceChangeListener?) {
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unRegisterPrefListener(listener: OnSharedPreferenceChangeListener?) {
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val TAG = "SharedPrefs"
    }

}