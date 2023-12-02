package me.wietlol.adventofcode.y2023

import me.wietlol.adventofcode.readInput
import me.wietlol.adventofcode.readSampleInput
import org.junit.Test
import unittest.core.models.TestCase
import unittest.core.models.TestModule
import java.io.InputStreamReader
import kotlin.math.max

class Day2 : TestModule
{
	@Test
	fun task1Sample() = unitTest {
		task1(readSampleInput(), 8)
	}
	
	@Test
	fun task1Real() = unitTest {
		task1(readInput(), 2278)
	}
	
	@Test
	fun task2Sample() = unitTest {
		task2(readSampleInput(), 2286)
	}
	
	@Test
	fun task2Real() = unitTest {
		task2(readInput(), 67953)
	}
	
	private fun TestCase.task1(input: InputStreamReader, expectedAnswer: Int)
	{
		val games = input
			.readLines()
			.filter { it.isNotEmpty() }
			.map { parseGame(it) }
		
		val answer = games
			.filter {
				it.reveals.none {
					it.cubeSets.any {
						it.color == "red" && it.amount > 12
							|| it.color == "green" && it.amount > 13
							|| it.color == "blue" && it.amount > 14
					}
				}
			}
			.sumOf { it.id }
		
		assertThat(answer)
			.isEqualTo(expectedAnswer)
	}
	
	private fun TestCase.task2(input: InputStreamReader, expectedAnswer: Int)
	{
		val games = input
			.readLines()
			.filter { it.isNotEmpty() }
			.map { parseGame(it) }
		
		val answer = computePowerSet(games)

		assertThat(answer)
			.isEqualTo(expectedAnswer)
	}
	
	private fun computePowerSet(games: List<Game>): Int =
		games.sumOf { computePowerSet(it) }
	
	private fun computePowerSet(game: Game): Int
	{
		val highestNumbers = mutableMapOf<String, Int>()
		
		game.reveals.forEach {
			it.cubeSets.forEach {
				highestNumbers.compute(it.color) { _, a ->
					max(a ?: 0, it.amount)
				}
			}
		}
		
		return highestNumbers.values.reduce(Int::times)
	}
	
	private fun parseGame(line: String): Game
	{
		val (description, data) = line.split(": ")
		val id = description.split(" ").last().toInt()
		
		val reveals = data.split("; ")
			.map { parseReveal(it) }
		
		return Game(id, reveals)
	}
	
	private fun parseReveal(text: String): Reveal
	{
		val cubeSets = text.split(", ")
			.map { parseCubeSet(it) }
		
		return Reveal(cubeSets)
	}
	
	private fun parseCubeSet(text: String): CubeSet
	{
		val (amount, name) = text.split(" ")
		return CubeSet(amount.toInt(), name)
	}
	
	private class Game(
		val id: Int,
		val reveals: List<Reveal>,
	)
	
	private class Reveal(
		val cubeSets: List<CubeSet>,
	)
	
	private class CubeSet(
		val amount: Int,
		val color: String,
	)
}
