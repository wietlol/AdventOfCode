package me.wietlol.adventofcode.y2021

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule
import kotlin.test.assertEquals

class Day10 : TestModule
{
	@Test
	fun task1()
	{
		val points = readInput()
			.readLines()
			.mapNotNull { findIllegalCharacter(it) }
			.sumOf { scoreFor(it) }
		
		assertEquals(319233, points)
	}
	
	@Test
	fun task2()
	{
		val points = readInput()
			.readLines()
			.filter { findIllegalCharacter(it) == null }
			.map { findMissingCharacters(it) }
			.map { computeScore(it) }
			.sorted()
			.median()
		
		assertEquals(1118976874, points)
	}
	
	private fun findIllegalCharacter(line: String): Char? =
		findIllegalCharacter(line.iterator().peekable())
	
	private fun findIllegalCharacter(chars: PeekableIterator<Char>): Char?
	{
		while (true)
		{
			if (!chars.hasNext())
				return null
			
			val char = chars.peek()
			if (isClosing(char))
				return null
			val open = chars.next()
			val illegal = findIllegalCharacter(chars)
			if (illegal != null)
				return illegal
			
			if (chars.hasNext().not())
				return null
			if (isClosing(chars.peek()).not())
				continue
			
			val close = chars.next()
			if (matches(open, close))
				continue
			return close
		}
	}
	
	private fun findMissingCharacters(line: String): String =
		findMissingCharacters(line.iterator().peekable())
	
	private fun findMissingCharacters(chars: PeekableIterator<Char>): String
	{
		while (true)
		{
			if (!chars.hasNext())
				return ""
			
			val char = chars.peek()
			if (isClosing(char))
				return ""
			val open = chars.next()
			val missing = findMissingCharacters(chars)
			
			if (chars.hasNext().not())
				return missing + closingFor(open)
			if (isClosing(chars.peek()).not())
				continue
			
			val close = chars.next()
			if (matches(open, close))
				continue
			TODO("illegal")
		}
	}
	
	private fun matches(open: Char, close: Char): Boolean =
		when (open)
		{
			'(' -> close == ')'
			'[' -> close == ']'
			'{' -> close == '}'
			'<' -> close == '>'
			else -> TODO()
		}
	
	private fun closingFor(open: Char): Char =
		when (open)
		{
			'(' -> ')'
			'[' -> ']'
			'{' -> '}'
			'<' -> '>'
			else -> TODO()
		}
	
	private fun isClosing(char: Char): Boolean =
		when (char)
		{
			')' -> true
			']' -> true
			'}' -> true
			'>' -> true
			else -> false
		}
	
	private fun scoreFor(char: Char): Int =
		when (char)
		{
			')' -> 3
			']' -> 57
			'}' -> 1197
			'>' -> 25137
			else -> TODO()
		}
	
	private fun computeScore(text: String): Long =
		text
			.asSequence()
			.fold(0) { acc, i -> acc * 5 + scoreValueFor(i) }
	
	private fun scoreValueFor(char: Char): Long =
		when (char)
		{
			')' -> 1
			']' -> 2
			'}' -> 3
			'>' -> 4
			else -> TODO()
		}
	
	private fun List<Long>.median(): Long =
		this[size / 2]
	
	private fun <T> Iterator<T>.peekable(): PeekableIterator<T> =
		PeekableIterator(this)
	
	class PeekableIterator<T>(
		val iterator: Iterator<T>
	) : Iterator<T>
	{
		private var peeked: T? = null
		
		fun peek(): T
		{
			if (peeked != null)
				return peeked!!
			peeked = iterator.next()
			return peeked!!
		}
		
		override fun hasNext(): Boolean =
			peeked != null || iterator.hasNext()
		
		override fun next(): T
		{
			val value = peeked
			if (value != null)
			{
				peeked = null
				return value
			}
			return iterator.next()
		}
	}
}
