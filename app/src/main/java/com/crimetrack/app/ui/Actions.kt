package com.crimetrack.app.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

/** Asli device actions — dialer, WhatsApp, contacts, share, clipboard. */
object Actions {

    fun dial(context: Context, e164: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + e164))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        start(context, intent)
    }

    fun whatsapp(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/91" + number))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        start(context, intent)
    }

    fun sms(context: Context, e164: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + e164))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        start(context, intent)
    }

    fun addContact(context: Context, name: String, e164: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, e164)
            if (name.isNotBlank()) putExtra(ContactsContract.Intents.Insert.NAME, name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        start(context, intent)
    }

    fun share(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        start(context, Intent.createChooser(intent, "Share"))
    }

    fun copy(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    private fun start(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // koi app na mile to chup-chaap ignore
        }
    }
}
