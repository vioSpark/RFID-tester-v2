package com.example.rfid_tester

import android.nfc.cardemulation.HostApduService
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.KITKAT)
class HCEService : HostApduService() {
    private val loggingName = "HCE Service"
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d(loggingName, "function processCommandApdu called")
        return byteArrayOf(0)
    }

    override fun onDeactivated(reason: Int) {
        Log.d(loggingName, "function onDeactivated called")
    }


}
