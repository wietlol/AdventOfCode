package me.wietlol.adventofcode.y2015

import org.junit.Test
import kotlin.math.max

class Day2
{
	@Test
	fun task1()
	{
		javaClass.getResourceAsStream("day2.txt")!!
			.reader()
			.readLines()
			.map { Present.parse(it) }
			.sumOf { it.wrapping }
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		javaClass.getResourceAsStream("day2.txt")!!
			.reader()
			.readLines()
			.map { Present.parse(it) }
			.sumOf { it.ribbon }
			.also { println(it) }
	}
	
	data class Present(
		val length: Int,
		val width: Int,
		val height: Int,
	)
	{
		val wrapping: Int = 2 * length * width + 2 * width * height + 2 * length * height + minOf(length * width, width * height, length * height)
		
		val ribbon: Int = (length + width + height - maxOf(length, width, height)) * 2 + length * width * height
		
		companion object
		{
			fun parse(text: String): Present =
				text
					.split("x")
					.map { it.toInt() }
					.let { (l, w, h) -> Present(l, w, h) }
		}
	}
}
