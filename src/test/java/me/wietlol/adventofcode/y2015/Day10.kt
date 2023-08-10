package me.wietlol.adventofcode.y2015

import org.junit.Test
import unittest.core.models.TestModule
import kotlin.test.assertEquals

class Day10 : TestModule
{
	@Test
	fun task1()
	{
		val answer = generateSequence("1113222113") { it.lookAndSay() }
			.take(41)
			.last()
			.length
		
		assertEquals(252594, answer)
	}
	
	@Test
	fun task2()
	{
		val answer = generateSequence("1113222113") { it.lookAndSay() }
			.take(51)
			.last()
			.length
		
		assertEquals(3579328, answer)
	}
	
	fun String.lookAndSay(): String =
		replace(Regex("(\\d)\\1*")) { it.value.length.toString() + it.value.first() }
}
