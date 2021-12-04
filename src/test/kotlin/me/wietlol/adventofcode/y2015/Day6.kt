package me.wietlol.adventofcode.y2015

import org.junit.Test
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class Day6
{
	val totalSize = 1_000
	
	@Test
	fun task1()
	{
//		val ui = generateUi()
		val ui = { _: Image -> }
		
		val lights = Array(totalSize) { BooleanArray(totalSize) }
		javaClass.getResourceAsStream("day6.txt")!!
			.reader()
			.readLines()
			.map { Operation.parse(it) }
			.forEach {
				lights.apply(it)
				ui(generateImage(lights))
			}
		
		lights.sumOf { it.count { it } }
			.also { println(it) }
	}
	
	@Test
	fun task2()
	{
//		val ui = generateUi()
		val ui = { _: Image -> }
		
		val lights = Array(totalSize) { IntArray(totalSize) }
		javaClass.getResourceAsStream("day6.txt")!!
			.reader()
			.readLines()
			.map { Operation.parse(it) }
			.forEach {
				lights.apply(it)
				ui(generateImage(lights))
			}
		
		lights.sumOf { it.sumOf { it } }
			.also { println(it) }
		
		println(detectedMax)
	}
	
	private fun generateUi(): (Image) -> Unit
	{
		class ImagePanel : JPanel()
		{
			lateinit var image: Image
			
			override fun paintComponent(graphics: Graphics)
			{
				super.paintComponent(graphics)
				graphics.clearRect(0, 0, totalSize, totalSize)
				if (::image.isInitialized)
					graphics.drawImage(image, 0, 0, this)
			}
		}
		
		val frame = JFrame("display")
		val contentPane = ImagePanel()
		val size = Dimension(totalSize,  totalSize)
		contentPane.minimumSize = size
		contentPane.maximumSize = size
		contentPane.preferredSize = size
		frame.contentPane = contentPane
		frame.pack()
		frame.setLocationRelativeTo(null)
		frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
		frame.isVisible = true
		Thread.sleep(1000)
		
		return {
			contentPane.image = it
			frame.invalidate()
			frame.repaint()
			Thread.sleep(40)
		}
	}
	
	private fun generateImage(lights: Array<BooleanArray>): Image
	{
		val image = BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_RGB)
		val white = 255*256*257+255
		for (x in 0 until totalSize)
			for (y in 0 until totalSize)
				image.setRGB(x, y, if (lights[x][y]) white else 0)
		return image
	}
	
	private var detectedMax = 0
	private fun generateImage(lights: Array<IntArray>): Image
	{
		val image = BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_RGB)
		val factor = 5
		for (x in 0 until totalSize)
			for (y in 0 until totalSize)
			{
				detectedMax = maxOf(detectedMax, lights[x][y])
				image.setRGB(x, y, lights[x][y].times(factor).let { Color(it, it, it) }.rgb)
			}
		return image
	}
	
	// task1
	private fun Array<BooleanArray>.apply(operation: Operation)
	{
		when (operation.code)
		{
			"on" -> loop(operation) { true }
			"off" -> loop(operation) { false }
			"toggle" -> loop(operation) { !it }
			else -> TODO("unrecognized operation ${operation.code}")
		}
	}
	
	// task2
	private fun Array<IntArray>.apply(operation: Operation)
	{
		when (operation.code)
		{
			"on" -> loop(operation) { it + 1 }
			"off" -> loop(operation) { maxOf(0, it - 1) }
			"toggle" -> loop(operation) { it + 2 }
			else -> TODO("unrecognized operation ${operation.code}")
		}
	}
	
	private fun Array<BooleanArray>.loop(operation: Operation, action: (Boolean) -> Boolean)
	{
		for (x in operation.startX..operation.endX)
			for (y in operation.startY..operation.endY)
				this[x][y] = action(this[x][y])
	}
	
	private fun Array<IntArray>.loop(operation: Operation, action: (Int) -> Int)
	{
		for (x in operation.startX..operation.endX)
			for (y in operation.startY..operation.endY)
				this[x][y] = action(this[x][y])
	}
	
	class Operation(
		val code: String,
		val startX: Int,
		val startY: Int,
		val endX: Int,
		val endY: Int,
	)
	{
		companion object
		{
			fun parse(text: String): Operation =
				text
					.split(" ")
					.reversed()
					.let { (end, _, start, code) ->
						val (sx, sy) = start.split(",").map { it.toInt() }
						val (ex, ey) = end.split(",").map { it.toInt() }
						Operation(code, sx, sy, ex, ey)
					}
		}
	}
}
