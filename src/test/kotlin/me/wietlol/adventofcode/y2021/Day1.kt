package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test

class Day1
{
	@Test
	fun task1()
	{
		readInput()
			.readLines()
			.asSequence()
			.map { it.toInt() }
			.windowed(2)
			.filter { (left, right) -> left < right }
			.count()
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		readInput()
			.readLines()
			.asSequence()
			.map { it.toInt() }
			.windowed(3)
			.map { it.sum() }
			.windowed(2)
			.filter { (left, right) -> left < right }
			.count()
			.also { println(it) }
	}
}
