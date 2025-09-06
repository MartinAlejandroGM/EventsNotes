package com.andro_sk.eventnotes.ui.utils

import java.util.UUID

fun generateRandomUUID(): String {
    val uuid = UUID.randomUUID()
    return uuid.toString()
}