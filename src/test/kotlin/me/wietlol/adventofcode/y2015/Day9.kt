package me.wietlol.adventofcode.y2015

import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class Day9
{
	@OptIn(ExperimentalTime::class)
	@Test
	fun performance()
	{
		task1()
		task2()
		measureTime { task1() }.also { println("task1 completed in $it") }
		measureTime { task2() }.also { println("task2 completed in $it") }
	}
	
	@Test
	fun task1()
	{
		val routes = javaClass.getResourceAsStream("day9.txt")!!
			.reader()
			.readLines()
			.map(Route::parse)
		
		val locations: List<String> = routes
			.flatMap { listOf(it.start, it.destination) }
			.distinct()
			.sorted()
		
		val distances = routes
			.associate { key(it.start, it.destination) to it.distance }
		
		computeShortestPath(locations, distances)
			.sortedBy { it.distance }
			.first()
			.also { println(it) }
			.also { println(it.distance) }
	}
	
	@Test
	fun task2()
	{
		val routes = javaClass.getResourceAsStream("day9.txt")!!
			.reader()
			.readLines()
			.map(Route::parse)
		
		val locations: List<String> = routes
			.flatMap { listOf(it.start, it.destination) }
			.distinct()
			.sorted()
		
		val distances = routes
			.associate { key(it.start, it.destination) to it.distance }
		
		computeShortestPath(locations, distances)
			.sortedByDescending { it.distance }
			.first()
			.also { println(it) }
			.also { println(it.distance) }
	}
	
	private fun computeShortestPath(locations: List<String>, distances: Map<String, Int>): Sequence<Path> =
		locations
			.asSequence()
			.flatMap { continuePath(Path(listOf(it), 0), locations.minus(it), distances) }
	
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
	}
	
	data class Path(
		val route: List<String>,
		val distance: Int,
	)
	{
		fun append(destination: String, distances: Map<String, Int>): Path =
			Path(route.plus(destination), distance + distances.getValue(key(route.last(), destination)))
	}
	
	data class Route(
		val start: String,
		val destination: String,
		val distance: Int,
	)
	{
		companion object
		{
			// AlphaCentauri to Snowdin = 66
			fun parse(text: String): Route =
				text
					.split(" ")
					.let { (s, _, d, _, l) -> Route(s, d, l.toInt()) }
		}
	}
}
