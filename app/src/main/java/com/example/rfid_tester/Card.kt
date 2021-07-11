package com.example.rfid_tester

data class Card(var id: ByteArray) {
    //this effectively treats the cards with the same ID the same.. this gonna need a fix
    var name = ""
    var type="" //TODO
    var messages = List<ByteArray>(0) { byteArrayOf(0) }
}