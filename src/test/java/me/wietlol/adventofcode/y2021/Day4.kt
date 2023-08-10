package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule
import kotlin.test.assertEquals

class Day4 : TestModule
{
	@Test
	fun task1()
	{
		val input = readInput()
			.readLines()
		
		val draws = input.first().split(",").map { it.toInt() }
		
		val boards = input
			.drop(2)
			.windowed(5, 6)
			.map { Board.parse(it) }
		
		var winningNumber: Int? = null
		draws
			.asSequence()
			.takeWhile { boards.none { it.hasWon } }
			.forEach { number ->
				winningNumber = number
				boards.forEach { it.crossNumber(number) }
			}
		
		val winners = boards
			.filter { it.hasWon }
			.map { it to it.computeScore(winningNumber!!) }
		
		val winner = winners
			.maxByOrNull { it.second }!!
		
		val score = winner.second
		
		assertEquals(22680, score)
	}
	
	
	@Test
	fun task2()
	{
		val input = readInput()
			.readLines()
		
		val draws = input.first().split(",").map { it.toInt() }
		
		var boards = input
			.drop(2)
			.windowed(5, 6)
			.map { Board.parse(it) }
		
		var winningNumber: Int? = null
		draws
			.asSequence()
			.takeWhile { boards.any { it.hasWon.not() } }
			.forEach { number ->
				winningNumber = number
				boards.forEach { it.crossNumber(number) }
				if (boards.size > 1)
					boards = boards.filter { it.hasWon.not() }
			}
		
		val loser = boards
			.single()
			.let { it to it.computeScore(winningNumber!!) }
		
		val score = loser.second
		
		assertEquals(16168, score)
	}
	
	private fun printBoard(board: Board)
	{
		board.board.forEach {
			it.forEach {
				if (it.isCrossed)
					print("X")
				else
					print(" ")
				print(it.number.toString().padStart(2, '0'))
			}
			println()
		}
	}
	
	class Board(
		val board: Array<Array<Cell>>,
	)
	{
		var hasWon: Boolean = false
			private set
		
		fun crossNumber(number: Int)
		{
			board.forEach { row ->
				row.indices.forEach { index ->
					val cell = row[index]
					if (cell.number == number)
					{
						cell.isCrossed = true
						if (row.all { it.isCrossed }) // won by row
							hasWon = true
						else if (board.all { it[index].isCrossed }) // won by column
							hasWon = true
					}
				}
			}
		}
		
		fun computeScore(winningNumber: Int): Int =
			board
				.flatMap { it.asSequence() }
				.filter { it.isCrossed.not() }
				.sumOf { it.number }
				.times(winningNumber)
		
		companion object
		{
			fun parse(lines: List<String>): Board
			{
				return Board(
					lines
						.map {
							it
								.trim()
								.split(Regex("\\s+"))
								.map { Cell(it.toInt()) }
								.toTypedArray()
						}
						.toTypedArray()
				)
			}
		}
	}
	
	class Cell(
		val number: Int,
		var isCrossed: Boolean = false,
	)
}
