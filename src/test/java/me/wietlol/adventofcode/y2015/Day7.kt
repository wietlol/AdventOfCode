package me.wietlol.adventofcode.y2015

import me.wietlol.adventofcode.readInput
import org.junit.Test
import unittest.core.models.TestModule

class Day7 : TestModule
{
	@Test
	fun task1()
	{
		val operations: Map<String, Operation> = readInput()
			.readLines()
			.map { Operation.parse(it) }
			.associateBy { it.stream }
		
		val processor = Processor(operations)
		processor.getValue("a")
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
		val b = 16076
		val operations: Map<String, Operation> = readInput()
			.readLines()
			.map { Operation.parse(it) }
			.associateBy { it.stream }
		
		val processor = Processor(operations, mutableMapOf("b" to b))
		processor.getValue("a")
			.also { println(it) }
	}
	
	class Processor(
		val operations: Map<String, Operation>,
		data: Map<String, Int> = emptyMap(),
	)
	{
		val data: MutableMap<String, Int> = data.toMutableMap()
		fun getValue(stream: String): Int =
			stream.toIntOrNull()
				?: if (data.containsKey(stream))
					data.getValue(stream)
				else
					when (val operation = operations.getValue(stream))
					{
						is ConstantOperation -> getValue(operation.value)
						is UnaryOperation -> getValue(operation.source).inv()
						is BinaryOperation -> when (operation.operator)
						{
							"AND" -> getValue(operation.left) and getValue(operation.right)
							"OR" -> getValue(operation.left) or getValue(operation.right)
							"LSHIFT" -> getValue(operation.left) shl getValue(operation.right)
							"RSHIFT" -> getValue(operation.left) shr getValue(operation.right)
							else -> TODO("unknown operator '${operation.operator}'")
						}
					}.also { data[stream] = it }
	}
	
	sealed interface Operation
	{
		val stream: String
		
		companion object
		{
			fun parse(text: String): Operation
			{
				val split = text.split(" ")
				
				when (split.size)
				{
					3 ->
					{
						// constant
						val (value, _, stream) = split
						return ConstantOperation(stream, value)
					}
					4 ->
					{
						// unary (always NOT)
						val (_, input, _, stream) = split
						return UnaryOperation(stream, "NOT", input)
					}
					5 ->
					{
						// binary
						val (left, oper, right, _, stream) = split
						return BinaryOperation(stream, oper, left, right)
					}
					else -> TODO("invalid operation '$text'")
				}
			}
		}
	}
	
	class ConstantOperation(
		override val stream: String,
		val value: String,
	) : Operation
	
	class UnaryOperation(
		override val stream: String,
		val operator: String,
		val source: String,
	) : Operation
	
	class BinaryOperation(
		override val stream: String,
		val operator: String,
		val left: String,
		val right: String,
	) : Operation
}
