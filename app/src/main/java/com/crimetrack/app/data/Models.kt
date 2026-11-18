package com.crimetrack.app.data

/** Ek user report — yahi app ka asli data hai. */
data class Report(
    val id: String,
    val number: String,
    val category: String,
    val comment: String,
    val reporter: String,
    val time: Long
)

/** Number ki poori info: asli validation + asli reports. */
data class NumberInfo(
    val number: String,
    val valid: Boolean,
    val e164: String,
    val national: String,
    val international: String,
    val type: String,
    val region: String,
    val reports: List<Report>
) {
    val reportCount: Int get() = reports.size

    val riskScore: Int get() = Risk.score(reports)

    val riskLabel: String get() = Risk.label(riskScore, reportCount)

    val lastReport: Long? get() = reports.maxOfOrNull { it.time }

    val topCategory: String get() = Risk.topCategory(reports)

    val knownName: String
        get() = reports.firstOrNull { it.reporter.isNotBlank() }?.reporter ?: ""

    val moreInfo: List<MoreInfoItem>
        get() = listOf(
            MoreInfoItem("Number Type", type, "hash"),
            MoreInfoItem("Region", region, "place"),
            MoreInfoItem("National Format", national, "share"),
            MoreInfoItem("International Format", international, "groups"),
            MoreInfoItem("Community Reports", "$reportCount reports", "business"),
            MoreInfoItem("Risk Score", "$riskScore / 100 · $riskLabel", "school"),
            MoreInfoItem("Top Category", topCategory, "car"),
            MoreInfoItem("Last Report", formatDate(lastReport), "vote"),
            MoreInfoItem("Validation", if (valid) "Valid mobile number" else "Not a valid number", "fingerprint")
        )
}

data class MoreInfoItem(val title: String, val subtitle: String, val icon: String)

data class HistoryItem(
    val number: String,
    val time: Long,
    val saved: Boolean = false
)

/** Risk score — poori tarah reports se calculate hota hai. */
object Risk {

    val categories = listOf("Fraud", "Spam", "Harassment", "Wrong Number", "Safe")

    fun score(reports: List<Report>): Int {
        if (reports.isEmpty()) return 0
        var total = 0
        reports.forEach { report ->
            total += when (report.category) {
                "Fraud" -> 22
                "Harassment" -> 16
                "Spam" -> 12
                "Wrong Number" -> 4
                "Safe" -> -8
                else -> 6
            }
        }
        return total.coerceIn(0, 100)
    }

    fun label(score: Int, count: Int): String = when {
        count == 0 -> "No reports"
        score < 25 -> "Low Risk"
        score < 60 -> "Medium Risk"
        else -> "High Risk"
    }

    fun topCategory(reports: List<Report>): String {
        if (reports.isEmpty()) return "—"
        return reports.groupingBy { it.category }.eachCount().maxByOrNull { it.value }?.key ?: "—"
    }
}

fun formatDate(millis: Long?): String {
    if (millis == null) return "—"
    return try {
        java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault())
            .format(java.util.Date(millis))
    } catch (e: Exception) {
        "—"
    }
}
