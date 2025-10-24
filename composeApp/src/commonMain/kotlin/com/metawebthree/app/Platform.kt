package com.metawebthree.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform