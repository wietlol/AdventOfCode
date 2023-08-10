package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule
import kotlin.test.assertEquals

class Day13 : TestModule
{
	@Test
	fun task1()
	{
		val biDirectionalEdges = readInput()
			.readLines()
			.map { Edge.parse(it) }
			.associate { (it.personA to it.personB) to it.happinessGain }
		
		val connections = biDirectionalEdges
			.keys
			.map { pair(it.first, it.second) }
			.distinct()
		
		val people: List<String> = biDirectionalEdges
			.keys
			.map { it.first }
			.distinct()
		
		val edges = connections.associate { key(it.first, it.second) to biDirectionalEdges.getValue(it) + biDirectionalEdges.getValue(it.reversed()) }
		
		val bestPath = computePaths(people, edges)
			.sortedBy { it.totalHappiness }
			.last()
		
		assertEquals(618, bestPath.totalHappiness)
	}
	
	@Test
	fun task2()
	{
		val biDirectionalEdgesExcludingYourself = readInput()
			.readLines()
			.map { Edge.parse(it) }
			.associate { (it.personA to it.personB) to it.happinessGain }
		
		val yourself = "Yourself"
		
		val peopleExcludingYourself: List<String> = biDirectionalEdgesExcludingYourself
			.keys
			.map { it.first }
			.distinct()
		
		val biDirectionalEdges = biDirectionalEdgesExcludingYourself
			.plus(peopleExcludingYourself
				.flatMap {
					listOf(
						(yourself to it) to 0,
						(it to yourself) to 0,
					)
				})
		
		val people = peopleExcludingYourself + yourself
		
		val connections = biDirectionalEdges
			.keys
			.map { pair(it.first, it.second) }
			.distinct()
		
		val edges = connections.associate { key(it.first, it.second) to biDirectionalEdges.getValue(it) + biDirectionalEdges.getValue(it.reversed()) }
		
		val bestPath = computePaths(people, edges)
			.sortedBy { it.totalHappiness }
			.last()
		
		assertEquals(601, bestPath.totalHappiness)
	}
	
	fun <T> Pair<T, T>.reversed(): Pair<T, T> = second to first
	
	private fun computePaths(locations: List<String>, distances: Map<String, Int>): Sequence<Path> =
		locations
			.asSequence()
			.flatMap { continuePath(Path(listOf(it), 0), locations.minus(it), distances) }
			.map { it.close(distances) }
	
	private fun continuePath(head: Path, locations: List<String>, distances: Map<String, Int>): Sequence<Path> =
		if (locations.isEmpty())
			sequenceOf(head)
		else
			locations
				.asSequence()
				.flatMap { continuePath(head.append(it, distances), locations.minus(it), distances) }
	
	companion object
	{
		private fun key(k1: String, k2: String): String =
			if (k1 < k2)
				k1 + k2
			else
				k2 + k1
		
		private fun pair(k1: String, k2: String): Pair<String, String> =
			if (k1 < k2)
				k1 to k2
			else
				k2 to k1
	}
	
	data class Path(
		val route: List<String>,
		val totalHappiness: Int,
	)
	{
		fun append(destination: String, distances: Map<String, Int>): Path =
			Path(route.plus(destination), totalHappiness + distances.getValue(key(route.last(), destination)))
		
		fun close(distances: Map<String, Int>): Path =
			append(route.first(), distances)
	}
	
	data class Edge(
		val personA: String,
		val personB: String,
		val happinessGain: Int,
	)
	{
		companion object
		{
			// Bob would gain 27 happiness units by sitting next to George.
			fun parse(text: String): Edge =
				text
					.trim('.')
					.split(" ")
					.let { Edge(it[0], it[10], if (it[2] == "gain") it[3].toInt() else it[3].toInt().times(-1)) }
		}
	}
}
