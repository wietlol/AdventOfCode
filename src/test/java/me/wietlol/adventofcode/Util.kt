package me.wietlol.adventofcode

import unittest.core.models.TestModule

fun TestModule.readInput() =
	readInput(javaClass.simpleName.lowercase())

fun TestModule.readSampleInput(suffix: String = "") =
	readInput(javaClass.simpleName.lowercase() + "-sample" + suffix)

fun TestModule.readInput(name: String) =
	javaClass.getResourceAsStream("$name.txt")!!
		.reader()
