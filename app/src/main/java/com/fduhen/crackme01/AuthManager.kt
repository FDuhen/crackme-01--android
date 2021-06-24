package com.fduhen.crackme01

import android.content.Context
import android.util.Base64
import android.widget.Toast
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

class AuthManager(private val context: Context) {

    val gson = Gson()

    fun getConnectedUser() : User? {
        val textFile = File(context.getExternalFilesDir(null), "dont_open_hacker/settings.rich")
        if (!textFile.exists() || textFile.totalSpace == 0L) {
            return null
        }

        val allText = textFile.readText()
        val userJson = String(Base64.decode(allText, Base64.DEFAULT))
        return try {
            gson.fromJson(userJson, User::class.java)
        } catch (exception: Exception) {
            null
        }
    }


    fun connect() : User {
        val currentUser = User("regularUser", false)

        val file = File(context.getExternalFilesDir(null), "dont_open_hacker")
        if (!file.exists()) {
            file.mkdir()
        }

        try {

            val gpxfile = File(file, "settings.rich")
            val writer = FileWriter(gpxfile)
            writer.append(Base64.encodeToString(gson.toJson(currentUser).toByteArray(), 1))
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erreur lors du setup du CrackMe. Merci d'installer l'application sur un émulateur recommandé dans le Readme", Toast.LENGTH_LONG).show()
        }

        return currentUser
    }


}