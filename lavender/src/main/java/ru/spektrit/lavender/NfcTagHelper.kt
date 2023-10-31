package ru.spektrit.lavender

interface NfcTagHelper {
   fun readConfig() : Map<String, Byte>
   fun auth(pass : ByteArray)
   fun getTagInformationAsString() : String
}