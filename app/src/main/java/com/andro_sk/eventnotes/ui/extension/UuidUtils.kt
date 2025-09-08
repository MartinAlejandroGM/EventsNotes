package com.andro_sk.eventnotes.ui.extension

import java.util.UUID

fun generateRandomUUID(): String {
    val uuid = UUID.randomUUID()
    return uuid.toString()
}