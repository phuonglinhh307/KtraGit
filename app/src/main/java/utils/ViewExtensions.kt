package com.example.bai13_thlttd.utils
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
fun View.show() {
    visibility = View.VISIBLE
}
fun View.gone() {
    visibility = View.GONE
}
fun View.invisible() {
    visibility = View.INVISIBLE
}
fun Context.toast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}
fun EditText.trimmedText(): String {
    return text
        ?.toString()
        ?.trim()
        .orEmpty()
}
fun Double.toAcademicRanking(): String {
    return when {
        this >= 3.6 -> "Xuất sắc"
        this >= 3.2 -> "Giỏi"
        this >= 2.5 -> "Khá"
        this >= 2.0 -> "Trung bình"
        this >= 1.0 -> "Yếu"
        else -> "Kém"
    }
}