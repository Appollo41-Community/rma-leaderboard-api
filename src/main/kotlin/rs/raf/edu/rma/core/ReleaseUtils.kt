package rs.raf.edu.rma.core

import java.io.File
import java.util.*

fun readReleaseVersion(): String {
    return try {
        val prop = File("release.properties").inputStream()
        Properties().apply { load(prop) }.getProperty("version") ?: ""
    } catch (error: Exception) {
        println(error)
        ""
    }
}

fun readDeleteSecretToken(): String {
    return try {
        val prop = File("security.properties").inputStream()
        Properties().apply { load(prop) }.getProperty("deleteToken") ?: ""
    } catch (error: Exception) {
        println(error)
        ""
    }
}
