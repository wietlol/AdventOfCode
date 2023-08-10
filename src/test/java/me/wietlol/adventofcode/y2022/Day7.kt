package me.wietlol.adventofcode.y2022

import me.wietlol.adventofcode.readInput
import me.wietlol.adventofcode.readSampleInput
import org.junit.Test
import unittest.core.models.TestCase
import unittest.core.models.TestModule
import java.io.InputStreamReader

class Day7 : TestModule
{
	@Test
	fun task1Sample() = unitTest {
		task1(readSampleInput(), 95437)
	}
	
	@Test
	fun task1Real() = unitTest {
		task1(readInput(), 1423358)
	}
	
	@Test
	fun task2Sample() = unitTest {
		task2(readSampleInput(), 24933642)
	}
	
	@Test
	fun task2Real() = unitTest {
		task2(readInput(), 545729)
	}
	
	private fun TestCase.task1(input: InputStreamReader, expectedAnswer: Int)
	{
		val sizes = FileSystemAnalyzer().analyzeFileSystem(input)
		
		val total = sizes
			.values
			.filter { it <= 100_000 }
			.sum()
		
		assertThat(total)
			.isEqualTo(expectedAnswer)
	}
	
	private fun TestCase.task2(input: InputStreamReader, expectedAnswer: Int)
	{
		val sizes = FileSystemAnalyzer().analyzeFileSystem(input)
		
		val maxSpace = 70_000_000
		val requiredSpace = 30_000_000
		val usedSpace = sizes.getValue("")
		val availableSpace = maxSpace - usedSpace
		val minimumSpaceToDelete = requiredSpace - availableSpace
		val answer = sizes
			.values
			.filter { it >= minimumSpaceToDelete }
			.min()
		
		assertThat(answer)
			.isEqualTo(expectedAnswer)
	}
}

class Dir(
	val path: String,
	val files: MutableMap<String, Int> = mutableMapOf(),
	val dirs: MutableMap<String, Dir> = DefaultValueMap(::Dir),
)

class DefaultValueMap<Key, Value>(
	val defaultValue: (Key) -> Value,
	val innerMap: MutableMap<Key, Value> = mutableMapOf()
) : MutableMap<Key, Value> by innerMap
{
	override operator fun get(key: Key): Value =
		innerMap.computeIfAbsent(key, defaultValue)
}

class FileSystemAnalyzer
{
	fun analyzeFileSystem(input: InputStreamReader): Map<String, Int>
	{
		val root = Dir("")
		val directories = mutableMapOf(root.path to root)
		
		// keep track of the current dir for `ls` purposes
		var currentDir = root
		input.readLines().forEach { line ->
			val split = line.split(" ")
			if (split[0] == "$")
			{
				if (split[1] == "cd")
				{
					when (val name = split[2])
					{
						"/" ->
						{
							currentDir = root
						}
						".." ->
						{
							val currentFullPath = currentDir.path
								.split("/")
								.dropLast(1)
								.joinToString("/")
							currentDir = directories.getValue(currentFullPath)
						}
						else ->
						{
							val currentFullPath = "${currentDir.path}/$name"
							// todo potentially account for cd before ls
							// if a cd is done before we know (via ls) that a certain folder exists,
							// then this folder wont be available yet... however, we probably dont have to
							// because the "user" would only know what to cd into after doing ls
							// if the puzzle input simulates a user, cd should always happen after ls
							currentDir = currentDir.dirs.getValue(currentFullPath)
						}
					}
				}
				else // split[1] == "ls"
				{
					// nothing to do
					// the code below will handle the output of the ls on further iterations of the input
				}
			}
			else
			{
				if (split[0] == "dir")
				{
					val name = split[1]
					val fullPath = "${currentDir.path}/$name"
					directories[fullPath] = currentDir.dirs.getValue(fullPath)
				}
				else
				{
					val (size, name) = split
					currentDir.files[name] = size.toInt()
				}
			}
		}
		
		val sizes = mutableMapOf<String, Int>()
		associateTotalSize(root, sizes)
		return sizes
	}
	
	/**
	 * recursively associates the sizes of all directories into the collector map
	 */
	private fun associateTotalSize(root: Dir, collector: MutableMap<String, Int>): Int
	{
		val directSize = root.files.values.sum()
		val indirectSize = root.dirs.values.sumOf { associateTotalSize(it, collector) }
		val totalSize = directSize + indirectSize
		collector[root.path] = totalSize
		return totalSize
	}
}
