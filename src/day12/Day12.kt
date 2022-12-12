package day12

import readInput
import kotlin.math.min

private const val DAY = "12"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

    fun findShortestPaths(
        input: List<String>,
        startPosition: Pair<Int, Int>,
        climbCheck: (Int, Int, Int, Int) -> Boolean
    ): Array<Array<Int>> {
        val steps = Array(input.size) { Array(input.first().length) { Int.MAX_VALUE } }
        steps[startPosition.first][startPosition.second] = 0
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(startPosition)
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            directions
                .map { (dx, dy) -> x + dx to y + dy }
                .filter { (newX, newY) -> newX in input.indices && newY in input[newX].indices }
                .filter { (newX, newY) -> climbCheck(x, y, newX, newY) && steps[newX][newY] > steps[x][y] + 1 }
                .forEach { (newX, newY) ->
                    steps[newX][newY] = steps[x][y] + 1
                    queue.add(newX to newY)
                }
        }
        return steps
    }

    fun part1(input: List<String>): Int {
        var startPosition: Pair<Int, Int>? = null
        var endPosition: Pair<Int, Int>? = null
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'S')
                    startPosition = i to j
                else if (input[i][j] == 'E')
                    endPosition = i to j
            }
        }
        val climbCheck = { x: Int, y: Int, newX: Int, newY: Int ->
            input[x][y].value >= input[newX][newY].value || input[x][y].value + 1 == input[newX][newY].value
        }
        return findShortestPaths(input, startPosition!!, climbCheck)[endPosition!!.first][endPosition.second]
    }

    fun part2(input: List<String>): Int {
        var startPosition: Pair<Int, Int>? = null
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'E')
                    startPosition = i to j
            }
        }
        val climbCheck = { x: Int, y: Int, newX: Int, newY: Int ->
            input[x][y].value <= input[newX][newY].value || input[x][y].value - 1 == input[newX][newY].value
        }
        val steps = findShortestPaths(input, startPosition!!, climbCheck)
        var minSteps = Int.MAX_VALUE
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'a')
                    minSteps = min(minSteps, steps[i][j])
            }
        }
        return minSteps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput(DAY_INPUT)
    println(part1(input)) // 370
    println(part2(input)) // 363
}

private val Char.value: Char
    get() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }
