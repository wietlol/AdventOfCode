package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test

class Day3
{
	@Test
	fun task1()
	{
		val deliveredPresents = mutableMapOf<Location, Int>()
		
		val visit = { location: Location -> deliveredPresents.compute(location) { _, value -> (value ?: 0) + 1 } }
		visit(Location(0, 0))
		
		readInput()
			.readText()
			.trim()
			.fold(Location(0, 0)) { location, direction ->
				location.move(direction).also { visit(it) }
			}
		
		println(deliveredPresents.size)
	}
	
	@Test
	fun task2()
	{
		val deliveredPresents = mutableMapOf<Location, Int>()
		
		val visit = { location: Location -> deliveredPresents.compute(location) { _, value -> (value ?: 0) + 1 } }
		visit(Location(0, 0))
		
		readInput()
			.readText()
			.trim()
			.foldIndexed(Location(0, 0).let { Pair(it, it) }) { index, location, direction ->
				if (index and 1 == 0)
					location.first.move(direction).also { visit(it) } to location.second
				else
					location.first to location.second.move(direction).also { visit(it) }
			}
		
		println(deliveredPresents.size)
	}
	
	data class Location(
		val x: Int,
		val y: Int,
	)
	{
		fun move(direction: Char): Location =
			when (direction)
			{
				'^' -> Location(x, y + 1)
				'v' -> Location(x, y - 1)
				'<' -> Location(x - 1, y)
				'>' -> Location(x + 1, y)
				else -> TODO("unknown direction $direction")
			}
	}
}
