package me.wietlol.adventofcode.y2022

import me.wietlol.adventofcode.readInput
import me.wietlol.adventofcode.readSampleInput
import org.junit.Test
import unittest.core.models.TestCase
import unittest.core.models.TestModule
import java.io.InputStreamReader

/**
 * Class containing the solution for the puzzle of day 8 from year 2022 of Advent of Code.
 * It uses the testing module to verify that the answers are correct.
 *
 * @author  Wietlol
 * @see <a href="https://adventofcode.com/2022/day/8">puzzle on Advent of Code</a>
 * @since   1.0
 */
class Day8 : TestModule
{
	/**
	 * This method executes task 1 for the sample puzzle input.
	 * It tests that the output corresponds to what the output should be according to the puzzle description.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	@Test
	fun task1Sample() = unitTest {
		task1(readSampleInput(), 21)
	}
	
	/**
	 * This method executes task 1 for the user's puzzle input.
	 * It tests that the output corresponds to what the output should be according to the accepted answer.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	@Test
	fun task1Real() = unitTest {
		task1(readInput(), 1851)
	}
	
	/**
	 * This method executes task 2 for the sample puzzle input.
	 * It tests that the output corresponds to what the output should be according to the puzzle description.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	@Test
	fun task2Sample() = unitTest {
		task2(readSampleInput(), 8)
	}
	
	/**
	 * This method executes task 2 for the user's puzzle input.
	 * It tests that the output corresponds to what the output should be according to the accepted answer.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	@Test
	fun task2Real() = unitTest {
		task2(readInput(), 574080)
	}
	
	/**
	 * Executes task 1 to count the number of visible trees in the given forest.
	 *
	 * @param input The input string representing the forest.
	 * @param expected The expected number of visible trees.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun TestCase.task1(input: InputStreamReader, expected: Int)
	{
		val forest = readForest(input)
		
		val walker = ForestWalker()
		val treeCounter = VisibleTreesCounter(walker)
		
		val visibleTrees = treeCounter.countVisibleTreesInForest(forest)
		
		assertThat(visibleTrees.size)
			.isEqualTo(expected)
	}
	
	/**
	 * Executes task 2 to calculate the highest scenic score of all trees in the given forest.
	 *
	 * @param input The input string representing the forest.
	 * @param expected The expected highest scenic score.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun TestCase.task2(input: InputStreamReader, expected: Int)
	{
		val forest = readForest(input)
		
		val scoreCalculator = ScenicScoreCalculator()
		
		val scenicScore = scoreCalculator.computeHighestScenicScore(forest)
		
		assertThat(scenicScore)
			.isEqualTo(expected)
	}
	
	/**
	 * Reads a forest from the given input string and returns a matrix of trees.
	 *
	 * @param input the input string representing the forest
	 * @return a matrix of trees representing the forest
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun readForest(input: InputStreamReader): Matrix<Tree>
	{
		val text = input.readText()
		val parser = MatrixParser { column, row, digit ->
			Tree(column, row, digit.digitToInt())
		}
		return parser.parseValue(text)
	}
}

/**
 * Represents a tree in a two-dimensional grid.
 *
 * @property column The column index of the tree in the grid.
 * @property row The row index of the tree in the grid.
 *
 * @author  Wietlol
 * @since   1.0
 */
data class Tree(
	/**
	 * Represents the index of a column in the forest.
	 *
	 * The `column` variable stores an integer value representing the index of a column in the grid.
	 * It can be used identify the location of this tree in the grid.
	 *
	 * @property column The index of the column.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val column: Int,
	
	/**
	 * Represents the index of a row in the forest.
	 *
	 * The `row` variable stores an integer value representing the index of a column in the grid.
	 * It can be used identify the location of this tree in the grid.
	 *
	 * @property column The index of the row.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val row: Int,
	
	/**
	 * Represents the height of the tree.
	 *
	 * @property height The height value in an unknown unit.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val height: Int,
)

/**
 * A class that computes the scenic score of trees in a given forest matrix.
 *
 * @author  Wietlol
 * @since   1.0
 */
class ScenicScoreCalculator
{
	/**
	 * Computes the highest scenic score of the trees in a given forest matrix.
	 *
	 * @param forest The matrix representing the forest.
	 * @return The highest scenic score.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun computeHighestScenicScore(forest: Matrix<Tree>): Int
	{
		val rowScores = forest.rowIndices.map { row -> computePartialScenicScoreForRow(forest, row) }
		val rowReverseScores = forest.rowIndices.map { row -> computePartialScenicScoreForRowReverse(forest, row) }
		val columnScores = forest.columnIndices.map { column -> computePartialScenicScoreForColumn(forest, column) }
		val columnReverseScores = forest.columnIndices.map { column -> computePartialScenicScoreForColumnReverse(forest, column) }
		
		return forest.columnIndices.maxOf { column ->
			forest.rowIndices.maxOf { row ->
				rowScores[row][column] *
					rowReverseScores[row][column] *
					columnScores[column][row] *
					columnReverseScores[column][row]
			}
		}
	}
	
	/**
	 * Computes the partial scenic score for a row.
	 *
	 * @param forest The matrix representing the forest.
	 * @param row The row index to compute the scenic score for.
	 * @return An array of integers representing the partial scenic score from the front side for each column in the row.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun computePartialScenicScoreForRow(forest: Matrix<Tree>, row: Int): List<Int> =
		computePartialScenicScoreForSequence(forest.row(row))
	
	/**
	 * Computes the partial scenic score for a row in reverse order.
	 *
	 * @param forest The matrix representing the forest.
	 * @param row The row index to compute the scenic score for.
	 * @return An array of integers representing the partial scenic score from the back side for each column in the row.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun computePartialScenicScoreForRowReverse(forest: Matrix<Tree>, row: Int): List<Int> =
		computePartialScenicScoreForSequence(forest.row(row).asReversed()).asReversed()
	
	/**
	 * Computes the partial scenic score for a column.
	 *
	 * @param forest The matrix representing the forest.
	 * @param column The column index to compute the scenic score for.
	 * @return An array of integers representing the partial scenic score from the front side for each row in the column.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun computePartialScenicScoreForColumn(forest: Matrix<Tree>, column: Int): List<Int> =
		computePartialScenicScoreForSequence(forest.column(column))
	
	/**
	 * Computes the partial scenic score for a column in reverse order.
	 *
	 * @param forest The matrix representing the forest.
	 * @param column The column index to compute the scenic score for.
	 * @return An array of integers representing the partial scenic score from the back side for each row in the column.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun computePartialScenicScoreForColumnReverse(forest: Matrix<Tree>, column: Int): List<Int> =
		computePartialScenicScoreForSequence(forest.column(column).asReversed()).asReversed()
	
	/**
	 * Computes the partial scenic score for a given sequence of trees.
	 *
	 * @param trees The list of trees to compute the partial scenic score for.
	 * @return A list of integers representing the partial scenic scores for each tree in the sequence.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun computePartialScenicScoreForSequence(trees: List<Tree>): List<Int>
	{
		val scoring = Array(10) { 0 }
		
		return trees.map { tree ->
			val score = scoring[tree.height]
			
			scoring.indices.forEach {
				scoring[it] = if (it <= tree.height)
					1
				else
					scoring[it] + 1
			}
			
			score
		}
	}
}

/**
 * A class that counts the number of visible trees in a forest matrix.
 *
 * @param forestWalker The instance of ForestWalker used to traverse the forest matrix.
 *
 * @author  Wietlol
 * @since   1.0
 */
class VisibleTreesCounter(
	/**
	 * This variable represents a forest walker.
	 *
	 * A forest walker is an object used to traverse through a forest data structure.
	 * It allows to navigate through the trees of the forest, in row and column order.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private val forestWalker: ForestWalker
)
{
	/**
	 * Counts the number of visible trees in a forest matrix and returns a list of distinct tree objects.
	 *
	 * @param forest The matrix representation of the forest where each cell contains an integer value representing the height of a tree.
	 * @return A list of visible tree objects found in the forest.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun countVisibleTreesInForest(forest: Matrix<Tree>): List<Tree>
	{
		val rowViews = forest.rowIndices.flatMap { row ->
			forestWalker.getVisibleTreesInRow(forest, row) +
				forestWalker.getVisibleTreesInRowReverse(forest, row)
		}
		
		val columnViews = forest.columnIndices.flatMap { column ->
			forestWalker.getVisibleTreesInColumn(forest, column) +
				forestWalker.getVisibleTreesInColumnReverse(forest, column)
		}
		
		return (rowViews + columnViews)
			.distinct()
	}
}

/**
 * Utility class for walking through a forest and finding visible trees.
 *
 * @author  Wietlol
 * @since   1.0
 */
class ForestWalker
{
	/**
	 * Returns a list of visible trees in the specified row of the given forest.
	 *
	 * @param forest the matrix representing the forest with tree heights
	 * @param row the row index of the forest matrix to search for trees
	 * @return a list of Tree objects representing the visible trees in the specified row
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun getVisibleTreesInRow(forest: Matrix<Tree>, row: Int): List<Tree>
	{
		val sequence = forest
			.row(row)
		return getVisibleTreesInSequence(sequence)
	}
	
	/**
	 * Returns a list of visible trees in the specified row of the given forest in reverse order.
	 *
	 * @param forest the matrix representing the forest with tree heights
	 * @param row the row index of the forest matrix to search for trees
	 * @return A list of Tree objects representing the visible trees in the given row from the back side.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun getVisibleTreesInRowReverse(forest: Matrix<Tree>, row: Int): List<Tree>
	{
		val sequence = forest
			.row(row)
			.asReversed()
		return getVisibleTreesInSequence(sequence)
	}
	
	/**
	 * Returns a list of visible trees in the specified column of the given forest.
	 *
	 * @param forest the matrix representing the forest with tree heights
	 * @param column the column index of the forest matrix to search for trees
	 * @return a list of Tree objects representing the visible trees in the specified column
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun getVisibleTreesInColumn(forest: Matrix<Tree>, column: Int): List<Tree>
	{
		val sequence = forest
			.column(column)
		return getVisibleTreesInSequence(sequence)
	}
	
	/**
	 * Returns a list of visible trees in the specified column of the given forest in reverse order.
	 *
	 * @param forest the matrix representing the forest with tree heights
	 * @param column the column index of the forest matrix to search for trees
	 * @return A list of Tree objects representing the visible trees in the given column from the back side.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun getVisibleTreesInColumnReverse(forest: Matrix<Tree>, column: Int): List<Tree>
	{
		val sequence = forest
			.column(column)
			.asReversed()
		return getVisibleTreesInSequence(sequence)
	}
	
	/**
	 * Returns a list of visible trees in the given sequence.
	 *
	 * @param trees The list of trees.
	 * @return The list of trees that are visible in the given sequence based on their height.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private fun getVisibleTreesInSequence(trees: List<Tree>): List<Tree>
	{
		var currentHeight = -1
		return trees
			.filter { tree ->
				if (tree.height > currentHeight)
				{
					currentHeight = tree.height
					true
				}
				else
					false
			}
			.toList()
	}
}

/**
 * Class responsible for parsing a matrix from a string representation.
 *
 * @param valueParser The parser used to convert individual values within the matrix.
 * @param T The generic type of the values in the matrix.
 *
 * @author  Wietlol
 * @since   1.0
 */
class MatrixParser<T>(
	/**
	 * A value parser for parsing values of each cell.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private val valueParser: (Int, Int, Char) -> T
)
{
	/**
	 * Parses the given text into a Matrix object.
	 *
	 * @param text The text to be parsed.
	 * @return A Matrix object containing the parsed values.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun parseValue(text: String): Matrix<T>
	{
		val data = text
			.lines()
			.filter { it.isNotBlank() }
			.mapIndexed { row, line ->
				line.mapIndexed { column, char -> valueParser(column, row, char) }
			}
		return Matrix(data)
	}
}

/**
 * Represents a matrix of elements of type T.
 *
 * @param data The 2-dimensional list containing the elements of the matrix.
 * @property height The height of the matrix.
 * @property width The width of the matrix.
 *
 * @author  Wietlol
 * @since   1.0
 */
class Matrix<T>(
	/**
	 * Represents the original collection of items in the matrix in a nested list
	 *
	 * @param T the type of elements in the list
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	private val data: List<List<T>>
)
{
	/**
	 * Represents the height of the matrix.
	 *
	 * The height is determined by the size of the initial data collection.
	 *
	 * @property height The height of the matrix.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val height = data.size
	
	/**
	 * Represents the width of the matrix.
	 *
	 * The width is determined by the size of the entries of the initial data collection.
	 *
	 * @property width The width of the matrix.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val width = data.first().size
	
	/**
	 * Represents the indices of the rows in the matrix.
	 *
	 * @property rowIndices The range of indices for the rows.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val rowIndices: IntRange
		get() = 0 until height
	
	/**
	 * Represents the indices of the columns in the matrix.
	 *
	 * @property columnIndices The range of indices for the columns.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	val columnIndices: IntRange
		get() = 0 until width
	
	/**
	 * Retrieves the element at the specified coordinates from the matrix.
	 *
	 * @param column The column index of the element.
	 * @param row The row index of the element.
	 * @return The element at the specified coordinates.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	operator fun get(column: Int, row: Int): T =
		data[row][column]
	
	/**
	 * Retrieves the values in the specified row of the data grid.
	 *
	 * @param row The index of the row to be retrieved.
	 * @return A list containing the values in the specified row.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun row(row: Int): List<T> =
		data[row]
	
	/**
	 * Retrieves the values in the specified column of the data grid.
	 *
	 * @param column The index of the column to be retrieved.
	 * @return A list containing the values in the specified column.
	 *
	 * @author  Wietlol
	 * @since   1.0
	 */
	fun column(column: Int): List<T> =
		sequence {
			rowIndices.forEach {
				yield(data[it][column])
			}
		}.toList()
}
