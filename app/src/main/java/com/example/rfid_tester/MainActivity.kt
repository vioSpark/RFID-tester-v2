package com.example.rfid_tester

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.rfid_tester.databinding.ActivityMainBinding

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.MifareClassic
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //TODO: do an empty list here
    private var cards: MutableList<Card> = mutableListOf(Card(ByteArray(0)))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Under development - Scan a tag to start", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            analyzeCard(intent = this.intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun analyzeCard(intent: Intent) {
        val loggingName = "analyze_card"
        Log.d(loggingName, "RFID intent started")

        val tag = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
        val id = tag.tag.id
        //TODO: should check for duplicates here
        val card = Card(id)

        cards.add(card)

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val ndefMessages = getNdefMessages(intent)
            card.messages = ndefMessages!!
//
//            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
//                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
//                // Process the messages array.
//                Log.d(loggingName, "NDEF")
//                Log.d(loggingName, messages[0].toString())
//                Toast.makeText(this, messages[0].toString(), Toast.LENGTH_SHORT).show()
//            }


            val tag = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            val snackText = getCardNumber(tag.tag)
            Snackbar.make(findViewById(R.id.fab), snackText, Snackbar.LENGTH_INDEFINITE).show()

            tag.connect()

            fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
            fun Byte.toPositiveInt() = toInt() and 0xFF

            MainScope().launch(Dispatchers.IO) {
                try {
                    tag.authenticateSectorWithKeyB(
                        0,
                        ubyteArrayOf(0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu).toByteArray()
                    )
                    var result = tag.transceive(byteArrayOf(0x30, 0))
                    var sresult = result.toHexString()
                    Log.e(loggingName, "result: $sresult")
                    result = tag.transceive(byteArrayOf(0x30, 0))
                    sresult = result.toHexString()
                    Log.e(loggingName, "result: $sresult")
                } catch (e: Exception) {
                    Log.e(loggingName, "caught: $e")
                }
            }


        }

        //TODO: get tags info
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            Log.d(loggingName, "TECH")
            //var tagFromIntent: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            Toast.makeText(this, "RFID_TECH found", Toast.LENGTH_SHORT).show()
            val tag = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            /*thread(start = true) {
                Log.d("THREAD", "${Thread.currentThread()} has run.")
            }*/
            val snackText = getCardNumber(tag.tag)
            Snackbar.make(findViewById(R.id.fab), snackText, Snackbar.LENGTH_INDEFINITE).show()

            //ViewModelScope.launch {}

            //coroutine try 2
            // Dispatchers.Main
            suspend fun fetchRFID(tag: MifareClassic) {
                // Dispatchers.Main
                val result = tag.transceive("0000".toByteArray())
                val tmp: String
                tmp = result.toString()
            }
            // look at this in the next section
            //suspend fun fetchRFID(tag: tag) = withContext(Dispatchers.IO){/*...*/}


            //var data = tag.authenticateSectorWithKeyA(0, MifareClassic.KEY_DEFAULT)
            //Log.d(TAG, messages[0].toString())
            //Toast.makeText(this, messages[0].toString(), Toast.LENGTH_SHORT).show()
        }
    }

    /*
    fun readTag(tag: Tag): String? {
        return MifareClassic.get(tag)?.use { mifare ->
            mifare.connect()
            val payload = mifare.readBlock(0)
            String(payload, Charset.forName("US-ASCII"))
        }
    }
     */


}