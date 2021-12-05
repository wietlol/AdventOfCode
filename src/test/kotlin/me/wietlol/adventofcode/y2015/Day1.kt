package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test

class Day1
{
	@Test
	fun task1()
	{
		readInput()
			.readLines()
			.single()
			.map { if (it == '(') 1 else -1 }
			.sum()
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		readInput()
			.readLines()
			.single()
			.map { if (it == '(') 1 else -1 }
			.runningFold(0, Int::plus)
			.takeWhile { it >= 0 }
			.count()
			.also { println(it) }
	}
}
