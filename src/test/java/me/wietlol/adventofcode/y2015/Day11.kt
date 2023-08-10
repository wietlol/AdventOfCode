package me.wietlol.adventofcode.y2015

import org.junit.Test
import unittest.core.models.TestModule
import java.lang.StringBuilder
import kotlin.test.assertEquals

class Day11 : TestModule
{
	@Test
	fun task1()
	{
		val nextPassword = generateSequence("vzbxkghb".toLong()) { it + 1 }
			.map { it.format() }
			.filter { it.isValidPassword() }
			.first()
		
		assertEquals("vzbxxyzz", nextPassword)
	}
	
	@Test
	fun task2()
	{
		val nextPassword = generateSequence("vzbxkghb".toLong()) { it + 1 }
			.map { it.format() }
			.filter { it.isValidPassword() }
			.drop(1)
			.first()
		
		assertEquals("vzcaabcc", nextPassword)
	}
	
	private val validationRegex = Regex("^(?=.*(\\w)\\1.*(?!\\1)(\\w)\\2)(?!.*[iol]).*\$")
	private fun String.isValidPassword(): Boolean
	{
		if (validationRegex.matches(this).not())
			return false
		
		if (digits.windowed(3).none { contains(it) })
			return false
		
		return true
	}
	
	@Test
	fun `assert that number formatting and parsing works`()
	{
		assertEquals("aaaaaaaa", 0L.format())
		assertEquals("aaaaaaab", 1L.format())
		assertEquals("aaaaaaba", 26L.format())
		assertEquals("aaaaabcd", (26L * 26L * 1 + 26L * 2 + 3L).format())
		assertEquals("zzzzzzzz", 208827064575L.format())
		
		assertEquals(0L, "aaaaaaaa".toLong())
		assertEquals(1L, "aaaaaaab".toLong())
		assertEquals(26L, "aaaaaaba".toLong())
		assertEquals(26L * 26L * 1 + 26L * 2 + 3L, "aaaaabcd".toLong())
		assertEquals(208827064575L, "zzzzzzzz".toLong())
	}
	
	private val digits = "abcdefghijklmnopqrstuvwxyz"
	private fun String.toLong(): Long =
		reversed()
			.foldIndexed(0L) { index, result, value -> result + digits.indexOf(value) * digits.length.pow(index) }
	
	private fun Int.pow(power: Int): Long
	{
		var result = 1L
		for (i in 1..power)
			result *= this
		return result
	}
	
	private fun Long.format(): String
	{
		var self = this
		val text = StringBuilder()
		while (self > 0)
		{
			text.append(digits[self.rem(digits.length).toInt()])
			self /= digits.length
		}
		
		return text.reverse().toString().padStart(8, digits.first())
	}
}
