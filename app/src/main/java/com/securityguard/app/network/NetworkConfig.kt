package com.securityguard.app.network

import com.securityguard.app.BuildConfig

object NetworkConfig {
    /**
     * Backend root URL (scheme + host; trailing slash optional).
     * Override via `buildConfigField` in app/build.gradle.kts or a product flavor.
     */
    var baseUrl: String = BuildConfig.BASE_URL
        private set

    fun setBaseUrl(url: String) {
        baseUrl = url.trim().let { if (it.endsWith("/")) it else "$it/" }
    }

    fun url(path: String): String {
        val base = baseUrl.trimEnd('/')
        val trimmed = path.trim('/')
        return if (trimmed.isEmpty()) "$base/" else "$base/$trimmed/"
    }
}
