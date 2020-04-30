package com.duck.stayawakeadb

import com.duck.flexilogger.FlexiLog

/**
 * Created by Bradley Duck on 2019/03/04.
 */
class Logger : FlexiLog() {
    override fun canLogToConsole(type: Int): Boolean {
        return true
    }

    override fun mustReport(type: Int): Boolean {
        return false //no reporting
    }

    override fun report(
        type: Int,
        tag: String,
        msg: String
    ) { //no reporting
    }

    override fun report(
        type: Int,
        tag: String,
        msg: String,
        tr: Throwable
    ) { //no reporting
    }
}