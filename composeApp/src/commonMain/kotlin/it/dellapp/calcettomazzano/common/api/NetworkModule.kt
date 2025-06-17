package it.dellapp.calcettomazzano.common.api

import networking.DevengNetworkingConfig
import networking.DevengNetworkingModule

object NetworkModule {
    fun initialize() {
        // Development
        val devConfig = DevengNetworkingConfig(
            loggingEnabled = true,
            requestTimeoutMillis = 120_000L,
            token = "dev-token"
        )

        // Production
        val prodConfig = DevengNetworkingConfig(
            loggingEnabled = false,
            requestTimeoutMillis = 30_000L,
            customHeaders = mapOf("X-Client-Version" to "2.1.0")
        )

        DevengNetworkingModule.initDevengNetworkingModule(
            restBaseUrl = "https://calcettomazzano.onrender.com",
            config = devConfig
        )
    }
}