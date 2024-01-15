package com.ivasco.moviestest.common.utils

import android.content.Context

class ResourceProvider(private val context: Context) {
    operator fun invoke() = context
}