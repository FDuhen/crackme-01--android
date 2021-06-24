package com.fduhen.crackme01

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var authManager: AuthManager
    private var currentUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authManager = AuthManager(this)
        currentUser = authManager.getConnectedUser()

        checkConnectDisplay()

        findViewById<Button>(R.id.connect).setOnClickListener {
            currentUser = authManager.connect()
            checkConnectDisplay()
        }

        findViewById<AppCompatImageView>(R.id.imageView).setOnClickListener {
            giveDaMoney()
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            Toast.makeText(this, "Restez appuyé longtemps pour afficher un indice", Toast.LENGTH_SHORT).show()
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnLongClickListener {
            Toast.makeText(this, "Certaines applications stockent des données sur la carte SD du téléphone. Jetez un coup d'oeil dans /sdcard/Android/data/", Toast.LENGTH_LONG).show()
            true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE), 1)
        }
    }

    private fun giveDaMoney() {
        if (currentUser != null && currentUser!!.isPremium) {
            val i = Intent(this, PremiumActivity::class.java)
            startActivity(i)
        } else {
            Toast.makeText(this, "Il faut payer la cotisation Premium pour pouvoir gagner de l'argent :(", Toast.LENGTH_LONG).show()
        }
    }


    private fun checkConnectDisplay() {
        if (currentUser != null) {
            findViewById<Button>(R.id.connect).visibility = View.GONE
            Toast.makeText(this, "Bienvenue " + currentUser!!.name, Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    authManager.getConnectedUser()
                } else {
                    finish()
                }
                return
            }
            else -> {
            }
        }
    }

}
