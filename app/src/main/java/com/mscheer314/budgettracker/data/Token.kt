package com.mscheer314.budgettracker.data

data class Token(
        val token: TokenX
)

data class TokenX(
        val accessToken: String,
        val expiresIn: Int,
        val issuedAt: String
)