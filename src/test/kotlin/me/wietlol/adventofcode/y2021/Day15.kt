package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

class Day15
{
	// matrix[100][100]
	// expected N: 10000
	// actual N:   17266
	// ratio N:    172%
	@Test
	fun task1()
	{
		val distanceMap: Array<IntArray> = readInput()
			.readLines()
			.map { it.toCharArray().map { it.toString().toInt() }.toIntArray() }
			.toTypedArray()
		
		val shortestDistance = findShortestPath(distanceMap)
		
		assertEquals(410, shortestDistance)
	}
	
	// matrix[500][500]
	// expected N: 250000
	// actual N:   654515
	// ratio N:    261%
	@Test
	fun task2()
	{
		val distanceMap: Array<IntArray> = readInput()
			.readLines()
			.map { it.toCharArray().map { it.toString().toInt() }.toIntArray() }
			.toTypedArray()
			.unfold(5)
		
		val shortestDistance = findShortestPath(distanceMap)
		
		assertEquals(2809, shortestDistance)
	}
	
	private fun Array<IntArray>.unfold(times: Int): Array<IntArray>
	{
		val rows: List<List<Int>> = map { row ->
			(0 until times)
				.flatMap { t -> row.map { it.i(t) } }
		}
		
		val fullMap = (0 until times)
			.flatMap { t ->
				rows.map { it.map { it.i(t) }.toIntArray() }
			}
		
		return fullMap
			.toTypedArray()
	}
	
	private fun Int.i(times: Int): Int =
		plus(times)
			.let {
				var a = it
				while (a > 9)
					a -= 9
				a
			}
	
	private fun findShortestPath(distanceMap: Array<IntArray>): Int
	{
		val height = distanceMap.size
		val width = distanceMap.first().size
		val n = AtomicInteger(0)
		
		val pathMap = Array(height) { IntArray(width) }
		
		(0 until height).forEach { y ->
			(0 until width).forEach { x ->
				processCell(distanceMap, pathMap, x, y, width, height, n)
			}
		}
		
		println("matrix[$width][$height] expected N: ${width * height} actual N: $n")
		return pathMap.last().last() - pathMap[0][0]
	}
	
	private fun processCell(distanceMap: Array<IntArray>, pathMap: Array<IntArray>, x: Int, y: Int, width: Int, height: Int, n: AtomicInteger)
	{
		val neighbours = sequenceOf(
			Pair(x - 1, y),
			Pair(x + 1, y),
			Pair(x, y - 1),
			Pair(x, y + 1),
		)
			.filter { it.first in 0 until width && it.second in 0 until height }
			.toList()
		
		val head = neighbours
			.map { pathMap[it.second][it.first] }
			.filter { it > 0 }
			.minOrNull()
			?: 0
		
		pathMap[y][x] = distanceMap[y][x] + head
		n.incrementAndGet()
		
		neighbours
			.filter { pathMap[it.second][it.first] > pathMap[y][x] + distanceMap[it.second][it.first] }
			.forEach { processCell(distanceMap, pathMap, it.first, it.second, width, height, n) }
	}
}
