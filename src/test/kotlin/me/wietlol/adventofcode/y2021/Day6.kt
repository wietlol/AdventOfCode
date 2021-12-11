package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class Day6
{
	@Test
	fun task1()
	{
		val input = readInput()
			.readText()
			.trim()
			.split(",")
			.map { it.toInt() }
			.let { list ->
				IntArray(9).also { arr ->
					list.forEach { arr[it]++ }
				}
			}
		
		val totalFish = generateSequence(input) { nextGen(it) }
			.take(80 + 1)
			.last()
			.sum()
		
		assertEquals(359344, totalFish)
	}
	
	@Test
	fun task2()
	{
		val input = readInput()
			.readText()
			.trim()
			.split(",")
			.map { it.toInt() }
			.let { list ->
				LongArray(9).also { arr ->
					list.forEach { arr[it]++ }
				}
			}
		
		val totalFish = generateSequence(input) { nextGen(it) }
			.take(256 + 1)
			.last()
			.sum()
		
		assertEquals(1629570219571, totalFish)
	}
	
	fun nextGen(input: IntArray): IntArray
	{
		val zeroes = input[0]
		input[0] = input[1]
		input[1] = input[2]
		input[2] = input[3]
		input[3] = input[4]
		input[4] = input[5]
		input[5] = input[6]
		input[6] = input[7] + zeroes
		input[7] = input[8]
		input[8] = zeroes
		return input
	}
	
	fun nextGen(input: LongArray): LongArray
	{
		val zeroes = input[0]
		input[0] = input[1]
		input[1] = input[2]
		input[2] = input[3]
		input[3] = input[4]
		input[4] = input[5]
		input[5] = input[6]
		input[6] = input[7] + zeroes
		input[7] = input[8]
		input[8] = zeroes
		return input
	}
	
	fun println(arr: IntArray) =
		println(arr.contentToString())
}
