package me.wietlol.adventofcode

fun Any.readInput() =
	javaClass.getResourceAsStream("${javaClass.simpleName.lowercase()}.txt")!!
		.reader()
