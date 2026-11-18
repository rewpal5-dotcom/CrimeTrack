package com.crimetrack.app.data

/** Ek mobile number ki poori record. Abhi demo database se aati hai. */
data class NumberRecord(
    val number: String,
    val name: String,
    val verified: Boolean,
    val operator: String,
    val circle: String,
    val location: String,
    val email: String,
    val whatsapp: String,
    val riskScore: String,
    val callerId: String,
    val reports: Int,
    val lastSeen: String,
    val status: String,
    val alternateNumbers: String,
    val socialProfiles: String,
    val address: String,
    val familyMembers: String,
    val businessInfo: String,
    val education: String,
    val vehicleInfo: String,
    val voterId: String,
    val aadhaar: String
) {
    /** More Info screen ke 9 row — title, subtitle aur icon key. */
    val moreInfo: List<MoreInfoItem>
        get() = listOf(
            MoreInfoItem("Alternate Numbers", alternateNumbers, "hash"),
            MoreInfoItem("Social Profiles", socialProfiles, "share"),
            MoreInfoItem("Address", address, "place"),
            MoreInfoItem("Family Members", familyMembers, "groups"),
            MoreInfoItem("Business Info", businessInfo, "business"),
            MoreInfoItem("Education", education, "school"),
            MoreInfoItem("Vehicle Info", vehicleInfo, "car"),
            MoreInfoItem("Voter ID", voterId, "vote"),
            MoreInfoItem("Aadhaar Verification", aadhaar, "fingerprint")
        )
}

data class MoreInfoItem(val title: String, val subtitle: String, val icon: String)

/** History ki ek entry — SharedPreferences me JSON ke roop me save hoti hai. */
data class HistoryItem(
    val number: String,
    val name: String,
    val time: Long,
    val saved: Boolean = false
)

/** Activity tab ke liye demo events. */
data class ActivityItem(val title: String, val detail: String)
