package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test
import kotlin.test.assertEquals

class Day14
{
	@Test
	fun task1()
	{
		val furthestDistance = readInput()
			.readLines()
			.asSequence()
			.map(Reindeer::parse)
			.map(Reindeer::velocitySequence)
			.map { it.runningFold(0, Int::plus) }
			.map { it.drop(2502) }
			.map { it.first() }
			.maxOrNull()
		
		assertEquals(2660, furthestDistance)
	}
	
	@Test
	fun task2()
	{
		val reindeerCount: Int
		val mostPoints = readInput()
			.readLines()
			.map(Reindeer::parse)
			.also { reindeerCount = it.size }
			.map(Reindeer::velocitySequence)
			.map { it.runningFold(0, Int::plus).drop(1) }
			.weirdOperation()
			.take(2503)
			.map { it.maxIndices() }
			.fold(IntArray(reindeerCount)) { acc, i -> acc.also { i.forEach { acc[it]++ } } }
			.maxOrNull()
		
		assertEquals(1256, mostPoints)
	}
	
	private fun List<Int>.maxIndices(): List<Int>
	{
		var max = 0
		val maxIndices = mutableListOf<Int>()
		forEachIndexed { index, value ->
			if (value > max)
			{
				max = value
				maxIndices.clear()
				maxIndices += index
			}
			else if (value == max)
			{
				maxIndices += index
			}
		}
		return maxIndices
	}
	
	private fun <T> List<Sequence<T>>.weirdOperation(): Sequence<List<T>>
	{
		val iterators = map { it.iterator() }
		return generateSequence { iterators.map { it.next() } }
	}
	
	data class Reindeer(
		val name: String,
		val velocity: Int,
		val movementDuration: Int,
		val pauseDuration: Int,
	)
	{
		fun velocitySequence(): Sequence<Int> =
			sequence {
				while (true)
				{
					repeat(movementDuration) { yield(velocity) }
					repeat(pauseDuration) { yield(0) }
				}
			}
		
		companion object
		{
			fun parse(text: String): Reindeer
			{
				// 0     1   2   3  4    5   6 7        8   9    10   11   12  13  14
				// Vixen can fly 19 km/s for 7 seconds, but then must rest for 124 seconds.
				val split = text.split(" ")
				return Reindeer(
					split[0],
					split[3].toInt(),
					split[6].toInt(),
					split[13].toInt(),
				)
			}
		}
	}
}
