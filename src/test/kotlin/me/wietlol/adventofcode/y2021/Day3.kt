package me.wietlol.adventofcode.y2021

import org.junit.Test

class Day3
{
	@Test
	fun task1()
	{
		javaClass.getResourceAsStream("day3.txt")!!
			.reader()
			.readLines()
			.let { it.fold(IntArray(it.first().length), ::merge) }
			.let { generateOutput(it) }
			.also { println(it.gammaRate) }
			.also { println(it.epsilonRate) }
			.also { println(it.consumption) }
	}
	
	private fun merge(result: IntArray, entry: String): IntArray
	{
		entry.indices.forEach {
			if (entry[it] == '1')
				result[it]++
			else
				result[it]--
		}
		return result
	}
	
	private fun generateOutput(data: IntArray) =
		Output(
			data.joinToString("") { if (it > 0) "1" else "0" }.toInt(2),
			data.joinToString("") { if (it < 0) "1" else "0" }.toInt(2),
		)
	
	@Test
	fun task2()
	{
		val input = javaClass.getResourceAsStream("day3.txt")!!
			.reader()
			.readLines()
		
		val oxygenRating = computeOxygenRating(input)
		val co2ScrubberRating = computeCo2ScrubberRating(input)
		println(oxygenRating)
		println(co2ScrubberRating)
		println(oxygenRating * co2ScrubberRating)
	}
	
	private fun computeOxygenRating(input: List<String>, index: Int = 0): Int
	{
		if (input.size == 1)
			return input.single().toInt(2)
		
		val group = input.groupBy { it[index] }
		val zeroes = group['0']?.toList() ?: emptyList()
		val ones = group['1']?.toList() ?: emptyList()
		
		return if (zeroes.size > ones.size)
			computeOxygenRating(zeroes, index + 1)
		else
			computeOxygenRating(ones, index + 1)
	}
	
	private fun computeCo2ScrubberRating(input: List<String>, index: Int = 0): Int
	{
		if (input.size == 1)
			return input.single().toInt(2)
		
		val group = input.groupBy { it[index] }
		val zeroes = group['0']?.toList() ?: emptyList()
		val ones = group['1']?.toList() ?: emptyList()
		
		return if (zeroes.size <= ones.size)
			computeCo2ScrubberRating(zeroes, index + 1)
		else
			computeCo2ScrubberRating(ones, index + 1)
	}
	
	data class Output(
		val gammaRate: Int,
		val epsilonRate: Int,
	)
	{
		val consumption get() = gammaRate * epsilonRate
	}
	
	data class Output2(
		val oxygenRating: Int,
		val co2ScrubberRating: Int,
	)
	{
		val lifeSupportRating get() = oxygenRating * co2ScrubberRating
	}
}
