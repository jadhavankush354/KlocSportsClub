package com.firstapplication.file.userAuthentication.util

import android.content.Context

fun readCsvFromAssets(context: Context): String {
        return context.assets.open("frequentlyaskedquestions.csv").bufferedReader().use { it.readText() }
    }

