package me.wietlol.adventofcode

fun Any.readInput() =
	readInput(javaClass.simpleName.lowercase())

fun Any.readInput(name: String) =
	javaClass.getResourceAsStream("$name.txt")!!
		.reader()
