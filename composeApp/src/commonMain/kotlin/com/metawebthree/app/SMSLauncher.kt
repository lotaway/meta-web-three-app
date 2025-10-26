package com.metawebthree.app

interface SMSLauncher {
    fun launch(value: String)
}

interface SMSLauncherBuilder {
    fun build(): SMSLauncher
}