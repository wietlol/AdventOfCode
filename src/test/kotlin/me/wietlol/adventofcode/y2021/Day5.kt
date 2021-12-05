package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import kotlin.test.assertEquals

class Day5
{
	@Test
	fun task1()
	{
		val board = readInput()
			.readLines()
			.map { Pipe.parse(it) }
			.filter { it.isStraight() }
			.let { Board.fromPipes(it) }
		
		val howMany = board
			.points()
			.filter { it > 1 }
			.count()
		
		assertEquals(7269, howMany)
	}
	
	@Test
	fun task2()
	{
		val board = readInput()
			.readLines()
			.map { Pipe.parse(it) }
			.let { Board.fromPipes(it) }
		
		val howMany = board
			.points()
			.filter { it > 1 }
			.count()
		
		assertEquals(21140, howMany)
	}
	
	data class Point(
		val x: Int,
		val y: Int,
	)
	
	data class Pipe(
		val start: Point,
		val end: Point,
	)
	{
		fun points(): Sequence<Point>
		{
			val x1 = start.x
			val y1 = start.y
			val x2 = end.x
			val y2 = end.y
			
			val x = if (x1 == x2) generateSequence { x1 } else if (x1 > x2) (x1 downTo x2).asSequence() else (x1..x2).asSequence()
			val y = if (y1 == y2) generateSequence { y1 } else if (y1 > y2) (y1 downTo y2).asSequence() else (y1..y2).asSequence()
			
			val xy = x.zip(y).map { (x, y) -> Point(x, y) }

			return xy
		}
		
		fun isDiagonal(): Boolean =
			isStraight().not()
		
		fun isStraight(): Boolean =
			start.x == end.x || start.y == end.y
		
		companion object
		{
			fun parse(text: String): Pipe =
				text
					.split(" -> ")
					.flatMap { it.split(",") }
					.map { it.toInt() }
					.let { (x1, y1, x2, y2) -> Pipe(Point(x1, y1), Point(x2, y2)) }
		}
	}
	
	data class Board(
		val pipes: Array<IntArray>,
	)
	{
		fun points(): Sequence<Int> =
			pipes
				.asSequence()
				.flatMap { it.asSequence() }
		
		fun println(take: Int)
		{
			pipes.take(take).forEach {
				it.take(take).forEach {
					if (it > 0)
						print(it)
					else
						print('.')
				}
				println("")
			}
		}
		
		companion object
		{
			fun fromPipes(pipes: List<Pipe>): Board
			{
				val size = 1_000
				
				val floor = Array(size) { IntArray(size) }
				
				pipes
					.flatMap { it.points() }
					.forEach { floor[it.y][it.x]++ }
				
				return Board(floor)
			}
		}
	}
}
