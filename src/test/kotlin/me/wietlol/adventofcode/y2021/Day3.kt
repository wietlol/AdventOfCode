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
	
	}
	
	data class Output(
		val gammaRate: Int,
		val epsilonRate: Int,
	)
	{
		val consumption get() = gammaRate * epsilonRate
	}
}
