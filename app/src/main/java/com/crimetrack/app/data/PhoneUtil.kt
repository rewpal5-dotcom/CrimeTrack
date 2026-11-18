package com.crimetrack.app.data

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.util.Locale

/**
 * Asli number validation aur formatting — Google ke libphonenumber se.
 * Ye offline chalta hai aur number ko country/type ke hisaab se sahi parse karta hai.
 */
object PhoneUtil {

    private val util: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    fun digitsOnly(value: String): String = value.filter { it.isDigit() }.take(10)

    fun parse(number: String): Phonenumber.PhoneNumber? {
        if (number.length != 10) return null
        return try {
            util.parse("+91$number", "IN")
        } catch (e: NumberParseException) {
            null
        }
    }

    fun isValid(number: String): Boolean {
        val parsed = parse(number) ?: return false
        return util.isValidNumber(parsed)
    }

    fun e164(number: String): String = parse(number)?.let { util.format(it, PhoneNumberUtil.PhoneNumberFormat.E164) } ?: "+91$number"

    fun national(number: String): String =
        parse(number)?.let { util.format(it, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) } ?: number

    fun international(number: String): String =
        parse(number)?.let { util.format(it, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL) } ?: "+91 $number"

    fun typeLabel(number: String): String {
        val parsed = parse(number) ?: return "Unknown"
        return when (util.getNumberType(parsed)) {
            PhoneNumberUtil.PhoneNumberType.MOBILE -> "Mobile"
            PhoneNumberUtil.PhoneNumberType.FIXED_LINE -> "Landline"
            PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE -> "Fixed line / Mobile"
            PhoneNumberUtil.PhoneNumberType.TOLL_FREE -> "Toll free"
            PhoneNumberUtil.PhoneNumberType.VOIP -> "VoIP"
            else -> "Unknown"
        }
    }

    fun regionName(number: String): String {
        val parsed = parse(number) ?: return "Unknown"
        val code = util.getRegionCodeForNumber(parsed) ?: return "Unknown"
        val country = Locale("", code).displayCountry
        return if (country.isBlank()) code else country
    }
}
