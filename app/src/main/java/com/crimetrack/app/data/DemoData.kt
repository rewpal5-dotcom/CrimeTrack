package com.crimetrack.app.data

/**
 * CrimeTrack ka offline demo database.
 *
 * Yahan sirf kalpanik (dummy) records hain — koi asli vyakti ka data nahi.
 * Aap apne records yahan add kar sakte hain, ya data/Lookup.kt me real API
 * source jod kar ise replace kar sakte hain.
 */
object DemoData {

    val records: Map<String, NumberRecord> = listOf(
        NumberRecord(
            number = "9876543210",
            name = "Amit Kumar",
            verified = true,
            operator = "Jio",
            circle = "UP (East)",
            location = "Lucknow, Uttar Pradesh",
            email = "amitk***@gmail.com",
            whatsapp = "Available",
            riskScore = "Low Risk",
            callerId = "Amit Kumar",
            reports = 12,
            lastSeen = "Today",
            status = "Active",
            alternateNumbers = "2 Numbers Found",
            socialProfiles = "3 Profiles Found",
            address = "Lucknow, Uttar Pradesh",
            familyMembers = "1 Member Found",
            businessInfo = "No Business Info",
            education = "Information Not Available",
            vehicleInfo = "UP 32 AB 1234",
            voterId = "Information Not Available",
            aadhaar = "Not Verified"
        ),
        NumberRecord(
            number = "9123456789",
            name = "Rahul Singh",
            verified = true,
            operator = "Airtel",
            circle = "Delhi NCR",
            location = "New Delhi, Delhi",
            email = "rahul***@outlook.com",
            whatsapp = "Available",
            riskScore = "Low Risk",
            callerId = "Rahul Singh",
            reports = 3,
            lastSeen = "Today",
            status = "Active",
            alternateNumbers = "1 Number Found",
            socialProfiles = "2 Profiles Found",
            address = "New Delhi, Delhi",
            familyMembers = "2 Members Found",
            businessInfo = "Trading Firm",
            education = "Graduate",
            vehicleInfo = "DL 8C AB 4521",
            voterId = "Available",
            aadhaar = "Verified"
        ),
        NumberRecord(
            number = "9988776655",
            name = "Neha Sharma",
            verified = false,
            operator = "Vi",
            circle = "Maharashtra",
            location = "Mumbai, Maharashtra",
            email = "neha***@yahoo.com",
            whatsapp = "Available",
            riskScore = "Medium Risk",
            callerId = "Neha S.",
            reports = 6,
            lastSeen = "Yesterday",
            status = "Active",
            alternateNumbers = "Not Available",
            socialProfiles = "1 Profile Found",
            address = "Mumbai, Maharashtra",
            familyMembers = "Not Available",
            businessInfo = "No Business Info",
            education = "Information Not Available",
            vehicleInfo = "MH 02 CD 8891",
            voterId = "Information Not Available",
            aadhaar = "Not Verified"
        ),
        NumberRecord(
            number = "9000011111",
            name = "Vikram Verma",
            verified = true,
            operator = "BSNL",
            circle = "Madhya Pradesh",
            location = "Bhopal, Madhya Pradesh",
            email = "vikram***@gmail.com",
            whatsapp = "Not Available",
            riskScore = "Low Risk",
            callerId = "Vikram Verma",
            reports = 1,
            lastSeen = "Yesterday",
            status = "Active",
            alternateNumbers = "1 Number Found",
            socialProfiles = "2 Profiles Found",
            address = "Bhopal, Madhya Pradesh",
            familyMembers = "3 Members Found",
            businessInfo = "No Business Info",
            education = "Post Graduate",
            vehicleInfo = "MP 04 EF 1122",
            voterId = "Available",
            aadhaar = "Verified"
        ),
        NumberRecord(
            number = "7825965412",
            name = "Unknown",
            verified = false,
            operator = "Not available",
            circle = "Not available",
            location = "Not in demo database",
            email = "Not available",
            whatsapp = "Not available",
            riskScore = "Unrated",
            callerId = "Unknown",
            reports = 0,
            lastSeen = "20 May 2024",
            status = "Unverified",
            alternateNumbers = "Not Available",
            socialProfiles = "Not Available",
            address = "Not Available",
            familyMembers = "Not Available",
            businessInfo = "No Business Info",
            education = "Information Not Available",
            vehicleInfo = "Information Not Available",
            voterId = "Information Not Available",
            aadhaar = "Not Verified"
        )
    ).associateBy { it.number }

    /** Demo database me number dhoondo, warna ek "unknown" record banao. */
    fun find(number: String): NumberRecord = records[number] ?: unknown(number)

    private fun unknown(number: String) = NumberRecord(
        number = number,
        name = "Unknown",
        verified = false,
        operator = "Not available",
        circle = "Not available",
        location = "Not in demo database",
        email = "Not available",
        whatsapp = "Not available",
        riskScore = "Unrated",
        callerId = "Unknown",
        reports = 0,
        lastSeen = "No record",
        status = "Unverified",
        alternateNumbers = "Not Available",
        socialProfiles = "Not Available",
        address = "Not Available",
        familyMembers = "Not Available",
        businessInfo = "No Business Info",
        education = "Information Not Available",
        vehicleInfo = "Information Not Available",
        voterId = "Information Not Available",
        aadhaar = "Not Verified"
    )

    val activity: List<ActivityItem> = listOf(
        ActivityItem("Number searched", "Search kiya gaya — demo mode"),
        ActivityItem("Risk score check", "Low Risk report generate hui"),
        ActivityItem("WhatsApp status", "Available bataya gaya"),
        ActivityItem("Saved to contacts", "Record aapke contacts me add hua"),
        ActivityItem("12 user reports", "Community reports dikhaye gaye")
    )

    /** Home screen ke quick search chips. */
    val suggestions: List<String> = listOf("9876543210", "9123456789", "9988776655", "9000011111")
}
