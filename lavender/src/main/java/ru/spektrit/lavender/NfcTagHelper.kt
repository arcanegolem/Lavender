package ru.spektrit.lavender

interface NfcTagHelper {
   fun readConfig() : Map<String, Byte>
   fun auth(pwd : ByteArray)
   fun getTagInformationAsString() : String
}