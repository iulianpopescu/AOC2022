package day14

import readInput

private const val DAY = "14"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun part1(input: List<String>): Int {
        val lines = input.map { it.toPoints() }
        return Cave(lines).countSandUnits()
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { it.toPoints() }
        return Cave(lines, false).countSandUnits()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput(DAY_INPUT)
    println(part1(input)) // 901
    println(part2(input)) // 24589
}

private class Cave(lines: List<List<Point>>, isInfinite: Boolean = true) {

    private val minY: Int = lines.minOf { line -> line.minOf { it.y } }
    private val maxY: Int = lines.maxOf { line -> line.maxOf { it.y } }
    private val maxX: Int = lines.maxOf { line -> line.maxOf { it.x } }
    private val bottomMargin = if (isInfinite) maxX + 1 else maxX + 3
    private val leftMargin = if (isInfinite) minY else minY - 250
    private val rightMargin = if (isInfinite) maxY else maxY + 250
    private val matrix: Array<Array<Char>> = Array(bottomMargin) { Array(rightMargin - leftMargin + 1) { '.' } }

    init {
        // setting sand source at Point(500, 0), but internal it's stored as (0, 500) reduced to the current bounds
        matrix[0][500.reduceY()] = '+'
        if (!isInfinite) {
            for (i in matrix.last().indices)
                matrix.last()[i] = '#'
        }
        lines.forEach { line ->
            line.windowed(2).forEach { (start, end) ->
                if (start.y == end.y) {
                    for (x in minOf(start.x, end.x)..maxOf(start.x, end.x))
                        matrix[x][start.y.reduceY()] = '#'
                } else {
                    for (y in minOf(start.y, end.y).reduceY()..maxOf(start.y, end.y).reduceY())
                        matrix[start.x][y] = '#'
                }
            }
        }
    }

    fun countSandUnits(): Int {
        var sandUnits = 0
        while (matrix[0][500.reduceY()] == '+' && fall(Point(500.reduceY(), 0))) {
            sandUnits++
        }
        println(this)
        return sandUnits
    }

    private fun nextPosition(point: Point): Point? = when {
        matrix[point.x + 1][point.y] == '.' -> point.copy(x = point.x + 1)
        matrix[point.x + 1][point.y - 1] == '.' -> point.copy(y = point.y - 1, x = point.x + 1)
        matrix[point.x + 1][point.y + 1] == '.' -> point.copy(y = point.y + 1, x = point.x + 1)
        else -> null
    }

    private fun fall(point: Point): Boolean {
        var sandPosition = point
        var next: Point? = point
        try {
            while (next != null) {
                sandPosition = next
                next = nextPosition(next)
            }
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
        matrix[sandPosition.x][sandPosition.y] = 'o'
        return true
    }

    override fun toString(): String {
        return matrix.joinToString(separator = "\n") { it.joinToString(separator = "") }
    }

    private fun Int.reduceY() = this - leftMargin
}

private data class Point(val y: Int, val x: Int)

private fun String.toPoints() = this
    .split(" -> ")
    .map { it.split(",") }
    .map { (x, y) -> Point(x.toInt(), y.toInt()) }
