package com.example.rfid_tester

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception


fun getCardNumber(tag: Tag): String {
    val id = tag.id
    var text = ""
    for (byte in id) {
        text += String.format("%02x", byte) + " "
    }
    return text
}

fun getNdefMessages(intent: Intent): List<ByteArray>? {
    val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
    val messages = rawMessages?.map { it as NdefMessage }
    val stuff =messages?.map { message -> message.records?.map { it.payload} }

    return stuff?.flatMap { it?.asIterable()!! }?.toList()

}
