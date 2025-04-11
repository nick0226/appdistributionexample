package com.appdistributionexample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform