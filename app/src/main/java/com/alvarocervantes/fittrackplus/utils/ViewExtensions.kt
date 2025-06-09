package com.alvarocervantes.fittrackplus.utils

import android.view.View

fun View.setSafeClickListener(delay: Long = 250, action: () -> Unit) {
    this.setOnClickListener {
        isEnabled = false
        postDelayed({
            action()
            isEnabled = true
        }, delay)
    }
}