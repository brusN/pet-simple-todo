package ru.nsu.brusn.smpltodo.api

class APIHeadersProvider {
    companion object {
        const val AUTHORIZATION = "Authorization"

        fun getBearer(jwt: String?) = "Bearer $jwt"
    }
}