package com.BSLCommunity.onlinefilmstracker.objects

data class WatchLater(
    val date: String,
    val filmLInk: String,
    val name: String,
    val info: String,
    val additionalInfo: String
) {
    var posterPath: String? = null
}