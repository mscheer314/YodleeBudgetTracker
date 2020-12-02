package com.mscheer314.budgettracker.api

data class EventsInfo(
    val EventsInfo: List<EventsInfoX>
)

data class EventsInfoX(
    val `data`: Data,
    val type: String
)

data class Data(
    val action: String,
    val additionalStatus: String,
    val fnToCall: String,
    val providerAccountId: Int,
    val providerId: Int,
    val providerName: String,
    val requestId: String,
    val sites: List<Site>,
    val status: String
)

data class Site(
    val additionalStatus: String,
    val providerAccountId: Int,
    val providerId: Int,
    val providerName: String,
    val requestId: String,
    val status: String
)