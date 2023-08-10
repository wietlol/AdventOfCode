package me.wietlol.adventofcode.y2015

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule
import kotlin.test.assertEquals

class Day12 : TestModule
{
	@Test
	fun task1()
	{
		val sum = readInput()
			.readText()
			.let { Regex("-?\\d+").findAll(it) }
			.map { it.value.toInt() }
			.sum()
		
		assertEquals(111754, sum)
	}
	
	@Test
	fun task2()
	{
		val sum = readInput()
			.readText()
			.let { JsonMapper().readTree(it) }
			.sum()
		
		assertEquals(65402, sum)
	}
	
	private fun JsonNode.sum(): Int =
		when (this)
		{
			is ObjectNode ->
				if (contains(TextNode("red")))
					0
				else
					sumOf { it.sum() }
			is ArrayNode -> sumOf { it.sum() }
			is IntNode -> intValue()
			is TextNode -> 0
			else -> 0
		}
}
