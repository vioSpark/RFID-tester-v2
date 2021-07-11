package com.example.rfid_tester.ui.mifarecalssic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MifareClassicViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome Sp4rk13"
    }
    val text: LiveData<String> = _text
}