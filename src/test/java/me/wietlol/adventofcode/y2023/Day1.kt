package me.wietlol.adventofcode.y2023

import me.wietlol.adventofcode.readInput
import me.wietlol.adventofcode.readSampleInput
import org.junit.Test
import unittest.core.models.TestCase
import unittest.core.models.TestModule
import java.io.InputStreamReader

class Day1 : TestModule
{
	@Test
	fun task1Sample() = unitTest {
		task1(readSampleInput(), 142)
	}
	
	@Test
	fun task1Real() = unitTest {
		task1(readInput(), 55538)
	}
	
	@Test
	fun task2Sample() = unitTest {
		task2(readSampleInput("2"), 281)
	}
	
	@Test
	fun task2Real() = unitTest {
		task2(readInput(), 54875)
	}
	
	private fun TestCase.task1(input: InputStreamReader, expectedAnswer: Int)
	{
		val lines = input
			.readLines()
			.filter { it.isNotEmpty() }
		
		val answer = lines.sumOf { calibrationValue(it) }
		
		assertThat(answer)
			.isEqualTo(expectedAnswer)
	}
	
	private fun TestCase.task2(input: InputStreamReader, expectedAnswer: Int)
	{
		val lines = input
			.readLines()
			.filter { it.isNotEmpty() }
		
		val answer = lines.sumOf { calibrationValue2(it) }
		
		assertThat(answer)
			.isEqualTo(expectedAnswer)
	}
	
	private fun calibrationValue(line: String): Int
	{
		val firstNumber = line.first(Char::isDigit).digitToInt()
		val lastNumber = line.last(Char::isDigit).digitToInt()
		return firstNumber * 10 + lastNumber
	}
	
	private val numbers = listOf(
		"one",
		"two",
		"three",
		"four",
		"five",
		"six",
		"seven",
		"eight",
		"nine",
	)
	
	private val firstRegex = Regex("\\d|${numbers.joinToString("|")}")
	private val lastRegex = Regex("\\d|${numbers.joinToString("|", transform = String::reversed)}")
	private fun calibrationValue2(line: String): Int
	{
		val firstNumber = firstRegex.find(line)!!.value.let(::parseNumber)
		val lastNumber = lastRegex.find(line.reversed())!!.value.reversed().let(::parseNumber)
		return firstNumber * 10 + lastNumber
	}
	
	private fun parseNumber(text: String): Int
	{
		return if (text.length == 1) text[0].digitToInt()
		else numbers.indexOf(text) + 1
	}
}
