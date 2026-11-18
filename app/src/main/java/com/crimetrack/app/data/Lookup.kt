package com.crimetrack.app.data

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Number ki info kaise banti hai.
 *
 * Asli hisse:
 *  - [PhoneUtil] se real validation, type, region aur formatting
 *  - Aapke device par saved real reports (aur unse nikala gaya risk score)
 *  - Optional: [RemoteLookup] — agar aap Settings me apna API URL daalein to
 *    wahan se bhi reports aati hain (contract README me likha hai)
 *
 * Note: kisi bhi number ka personal data (naam, address, email) sirf kisi
 * licensed data provider se milta hai — isliye app khud se aisa data nahi banata.
 */
object Lookup {

    fun build(number: String, reports: List<Report>): NumberInfo {
        val valid = PhoneUtil.isValid(number)
        return NumberInfo(
            number = number,
            valid = valid,
            e164 = PhoneUtil.e164(number),
            national = PhoneUtil.national(number),
            international = PhoneUtil.international(number),
            type = PhoneUtil.typeLabel(number),
            region = PhoneUtil.regionName(number),
            reports = reports
        )
    }
}

/**
 * Optional remote source. Settings me URL daalne par ye chalta hai.
 *
 * Expected response (JSON):
 * {
 *   "reports": [
 *     {"id":"r1","category":"Fraud","comment":"OTP fraud call","reporter":"Ravi","time":1730000000000}
 *   ]
 * }
 */
object RemoteLookup {

    fun fetch(serverUrl: String, apiKey: String, number: String): List<Report> {
        if (serverUrl.isBlank()) return emptyList()
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(serverUrl + "?number=" + URLEncoder.encode(number, "UTF-8"))
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8000
                readTimeout = 8000
                setRequestProperty("Accept", "application/json")
                if (apiKey.isNotBlank()) setRequestProperty("Authorization", "Bearer " + apiKey)
            }
            if (connection.responseCode !in 200..299) return emptyList()
            val body = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
            parseReports(body, number)
        } catch (e: Exception) {
            emptyList()
        } finally {
            connection?.disconnect()
        }
    }

    private fun parseReports(body: String, number: String): List<Report> {
        return try {
            val root = JSONObject(body)
            val arr = root.optJSONArray("reports") ?: return emptyList()
            (0 until arr.length()).mapNotNull { i ->
                val o = arr.optJSONObject(i) ?: return@mapNotNull null
                Report(
                    id = o.optString("id", "remote-" + i),
                    number = number,
                    category = o.optString("category", "Spam"),
                    comment = o.optString("comment", ""),
                    reporter = o.optString("reporter", ""),
                    time = o.optLong("time", System.currentTimeMillis())
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
