package com.nacarseven.feelings

object TestUtil {
    fun loadTextFile(name: String) : String = this::class.java.classLoader.getResource(name).readText()
}