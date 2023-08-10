package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule

class Day2 : TestModule
{
	@Test
	fun task1()
	{
		readInput()
			.readLines()
			.map { it.split(" ") }
			.fold(Position(0, 0)) { position, (command, weight) ->
				when (command)
				{
					"forward" -> position.copy(offset = position.offset + weight.toInt())
					"up" -> position.copy(depth = position.depth - weight.toInt())
					"down" -> position.copy(depth = position.depth + weight.toInt())
					else -> TODO("unknown command '$command'")
				}
			}
			.also { println(it) }
			.also { println(it.offset * it.depth) }
	}
	
	@Test
	fun task2()
	{
		readInput()
			.readLines()
			.map { it.split(" ") }
			.fold(Orientation(0, 0, 0)) { position, (command, weight) ->
				when (command)
				{
					"forward" -> position.copy(offset = position.offset + weight.toInt(), depth = position.depth + position.aim * weight.toInt())
					"up" -> position.copy(aim = position.aim - weight.toInt())
					"down" -> position.copy(aim = position.aim + weight.toInt())
					else -> TODO("unknown command '$command'")
				}
			}
			.also { println(it) }
			.also { println(it.offset * it.depth) }
	}
	
	data class Position(
		val offset: Int,
		val depth: Int,
	)
	
	data class Orientation(
		val offset: Int,
		val depth: Int,
		val aim: Int,
	)
}
