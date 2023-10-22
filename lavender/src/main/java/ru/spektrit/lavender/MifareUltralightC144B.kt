package ru.spektrit.lavender

import android.nfc.tech.MifareUltralight

class MifareUltralightC144B (private val muTag: MifareUltralight) : NfcTagHelper {
   private val read : Byte = 0x30
   private val pwdAuth : Byte = 0x1B

   // Config reader
   override fun readConfig() {
      val readConfigResponse = muTag.transceive(
         byteArrayOf(
            read,
            41
         )
      )

      val mirror     = readConfigResponse[0]
      // pass is invisible thus not read
      val mirrorPage = readConfigResponse[2]
      val auth0      = readConfigResponse[3]
      val access     = readConfigResponse[4]
   }


   /**
    * Tag authentication
    * @param pass tag's 4-bytes long password
    * @param expectedPACK expected PACK value for explicit check
    */
   override fun auth(pass : ByteArray, expectedPACK : ByteArray? ) {
      val packResponse = muTag.transceive(
         byteArrayOf(
            pwdAuth,
            *(pass.sliceArray(0..3))
         )
      )
   }

   override fun getTagInformationAsString() : String
   { return "ID: ${muTag.tag?.id.contentToString()}, TechList: ${muTag.tag?.techList.contentToString()}" }
}