package me.wietlol.adventofcode.y2015

import org.junit.Test

class Day1
{
	@Test
	fun task1()
	{
		javaClass.getResourceAsStream("day1.txt")!!
			.reader()
			.readLines()
			.single()
			.map { if (it == '(') 1 else -1 }
			.sum()
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		javaClass.getResourceAsStream("day1.txt")!!
			.reader()
			.readLines()
			.single()
			.map { if (it == '(') 1 else -1 }
			.runningFold(0, Int::plus)
			.takeWhile { it >= 0 }
			.count()
			.also { println(it) }
	}
}
