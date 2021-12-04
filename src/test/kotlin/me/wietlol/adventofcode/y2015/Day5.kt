package me.wietlol.adventofcode.y2015

import org.junit.Test

class Day5
{
	@Test
	fun task1()
	{
		javaClass.getResourceAsStream("day5.txt")!!
			.reader()
			.readLines()
			.filter { it.matches(niceRegex1) }
			.count()
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		javaClass.getResourceAsStream("day5.txt")!!
			.reader()
			.readLines()
			.filter { it.matches(niceRegex2) }
			.count()
			.also { println(it) }
	}
	
	private val niceRegex1 = Regex("^(?=.*[aeiou].*[aeiou].*[aeiou].*)(?=.*(\\w)\\1.*)(?!.*(?:ab|cd|pq|xy).*).*\$")
	private val niceRegex2 = Regex("^(?=.*(\\w).\\1)(?=.*(\\w{2}).*\\2).*\$")
}
