package com.crimetrack.app.data

/**
 * CrimeTrack me data kahan se aata hai — ye ek hi jagah se badalta hai.
 *
 * Abhi app [DemoLookupSource] use karta hai (offline, dummy records).
 *
 * REAL LOOKUP JODNE KE LIYE:
 * 1. [LookupSource] implement karne wali nayi class banao, jaise:
 *
 *      class MyApiLookupSource(private val apiKey: String) : LookupSource {
 *          override suspend fun lookup(number: String): NumberRecord {
 *              // yahan apni legal API ko call karo (Retrofit / HttpURLConnection)
 *              // aur response ko NumberRecord me map karo
 *          }
 *      }
 *
 * 2. [Lookup.current] ko apni class se replace karo (AppViewModel me).
 * 3. AndroidManifest me INTERNET permission pehle se hai.
 *
 * DHYAN: kisi bhi number ka personal data nikalna sirf tab legal hai jab
 * data source ke paas iski permission ho aur aapke paas user ka consent ho.
 * Isliye default source demo data hi hai.
 */
interface LookupSource {
    suspend fun lookup(number: String): NumberRecord
}

class DemoLookupSource : LookupSource {
    override suspend fun lookup(number: String): NumberRecord = DemoData.find(number)
}

object Lookup {
    /** App ka current data source — yahi badalna hai real API ke liye. */
    val current: LookupSource = DemoLookupSource()
}
