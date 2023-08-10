package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule

class Day8 : TestModule
{
	@Test
	fun task1()
	{
		val encodedStrings = readInput()
			.readLines()
		
		val decodedStrings = encodedStrings
			.map { decode(it) }
		
		println(encodedStrings.sumOf { it.length } - decodedStrings.sumOf { it.length })
	}
	
	@Test
	fun task2()
	{
		val decodedStrings = readInput()
			.readLines()
		
		val encodedStrings = decodedStrings
			.map { encode(it) }
		
		println(encodedStrings.sumOf { it.length } - decodedStrings.sumOf { it.length })
	}
	
	private val decodeRegex = Regex("""\\(?:[\\"]|x[a-f0-9]{2})""")
	private fun decode(string: String): String =
		string.substring(1, string.length - 1)
			.replace(decodeRegex) {
				when (it.value[1])
				{
					'\\' -> "\\"
					'"' -> "\""
					'x' -> it.value.substring(2).toInt(16).toChar().toString()
					else -> TODO("unknown escaping '${it.value}'")
				}
			}
	
	private val encodeRegex = Regex("""[\\"]""")
	private fun encode(string: String): String =
		string.replace(encodeRegex) { "\\${it.value}" }
			.let { "\"$it\"" }
}
