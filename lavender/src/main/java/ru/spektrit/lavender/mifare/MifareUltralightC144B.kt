package ru.spektrit.lavender.mifare

import android.nfc.tech.MifareUltralight
import ru.spektrit.lavender.NfcTagHelper
import ru.spektrit.lavender.exceptions.WrongPackSizeException
import ru.spektrit.lavender.exceptions.WrongPwdSizeException
import ru.spektrit.lavender.tagtypes.TYPE_ULTRALIGHT_C
import java.lang.RuntimeException
import kotlin.jvm.Throws

/**
 * Wrapper class specified for interaction with MifareUltralight NTAG213 NFC tags
 *
 * @param mifareTag [MifareUltralight] object to perform operations on
 */
class MifareUltralightC144B (private val mifareTag: MifareUltralight) : NfcTagHelper {
   private val singlePageRead : Byte = 0x30
   private val pwdAuth        : Byte = 0x1B

   init {
      if ( mifareTag.type != TYPE_ULTRALIGHT_C ) { throw RuntimeException("Wrong tag type for ${this::class.simpleName}!") }
   }

   // Config reader
   override fun readConfig() : Map<String, Byte> {
      val readConfigResponse = mifareTag.transceive(
         byteArrayOf(
            singlePageRead,
            41
         )
      )

      val mirror     = readConfigResponse[0]
      // pass is invisible thus not read
      val mirrorPage = readConfigResponse[2]
      val auth0      = readConfigResponse[3]
      val access     = readConfigResponse[4]

      return mapOf(
         "Mirror" to mirror,
         "MirrorPage" to mirrorPage,
         "Auth0" to auth0,
         "Access" to access
      )
   }


   /**
    * Tag PACK setter
    *
    * NOTE: If password was already set, requires to perform [auth] first
    * @param pack 2-byte long password acknowledgement code
    * @throws WrongPackSizeException when provided PACK is not exactly 2 bytes long
    */
   @Throws(WrongPackSizeException::class)
   fun setPACK(pack : ByteArray) {
      if (pack.size != 2) { throw WrongPackSizeException() }
      mifareTag.writePage(44, byteArrayOf(*(pack), 0x00.toByte(), 0x00.toByte()))
      // NOT THOROUGHLY TESTED
   }


   /**
    * Tag PWD setter
    *
    * NOTE: If password was already set, requires to perform [auth] first
    * @param pwd 4-byte long password
    * @throws WrongPwdSizeException when provided PWD is not exactly 4 bytes long
    */
   @Throws(WrongPwdSizeException::class)
   fun setPWD(pwd : ByteArray){
      if (pwd.size != 4) { throw WrongPwdSizeException() }
      mifareTag.writePage(43, pwd)
      // NOT THOROUGHLY TESTED
   }


   /**
    * Tag authentication
    * @param pwd 4-byte long password
    *
    * @return PACK as [ByteArray]
    * @throws WrongPwdSizeException when provided PWD is not exactly 4 bytes long
    */
   @Throws(WrongPwdSizeException::class)
   override fun auth(pwd: ByteArray) {
      if (pwd.size != 4) { throw WrongPwdSizeException() }
      mifareTag.transceive(
         byteArrayOf(
            pwdAuth,
            *(pwd.sliceArray(0..3))
         )
      )
      // NOT THOROUGHLY TESTED
   }

   override fun getTagInformationAsString() : String
   { return "ID: ${mifareTag.tag?.id.contentToString()}, TechList: ${mifareTag.tag?.techList.contentToString()}" }
}