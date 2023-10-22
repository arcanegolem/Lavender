package ru.spektrit.lavender

interface NfcTagHelper {
   fun readConfig()
   fun auth(pass : ByteArray, expectedPACK : ByteArray? = null)
   fun getTagInformationAsString() : String
}