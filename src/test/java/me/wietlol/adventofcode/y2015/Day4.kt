package me.wietlol.adventofcode.y2015

import org.junit.Test
import unittest.core.models.TestModule
import java.security.MessageDigest

class Day4 : TestModule
{
	@Test
	fun task1()
	{
		val input = "ckczppom"
		
		generateSequence(0) { it + 1 }
			.map { it to md5(input + it) }
			.filter { (_, hash) -> hash.substring(0, 5) == "00000" }
			.first()
			.first
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		val input = "ckczppom"
		
		generateSequence(0) { it + 1 }
			.map { it to md5(input + it) }
			.filter { (_, hash) -> hash.substring(0, 6) == "000000" }
			.first()
			.first
			.also { println(it) }
	}
	
	private fun md5(text: String): String =
		MessageDigest.getInstance("MD5")
			.digest(text.toByteArray())
			.toHex()
	
	private fun ByteArray.toHex(): String = joinToString("") { eachByte -> "%02x".format(eachByte) }
}
