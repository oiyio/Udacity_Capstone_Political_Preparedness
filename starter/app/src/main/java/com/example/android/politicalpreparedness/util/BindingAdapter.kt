package com.example.android.politicalpreparedness.util

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("date")
fun bindDate(textView: TextView, date: Date) {
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
    textView.text = dateFormat.format(date)
}


@BindingAdapter("urlTextViewClickListener")
fun urlTextViewClickListener(textView: TextView, url: String?) {
    url?.let {
        textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            textView.context.startActivity(intent)
        }
    }
}