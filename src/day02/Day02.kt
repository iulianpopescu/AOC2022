package day02

import readInput
import kotlin.IllegalArgumentException
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.String
import kotlin.check
import kotlin.to

private const val DAY = "02"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun part1(input: List<String>) = input.map {
        val shapes = it.split(' ')
        shapes[0].toShape() to shapes[1].toShape()
    }.fold(0) { total, (opponent, player) ->
        total + opponent.match(player).points + player.points
    }

    fun part2(input: List<String>) = input.map {
        val shapes = it.split(' ')
        shapes[0].toShape() to shapes[1].toResult()
    }.fold(0) { total, (opponent, desiredResult) ->
        val options = listOf(Shape.PAPER, Shape.ROCK, Shape.SCISSORS)
        val player = options.find {
            opponent.match(it) == desiredResult
        } ?: throw IllegalStateException("Can't find match that satisfies the result")
        total + desiredResult.points + player.points
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

private sealed class Shape(val points: Int) {
    object ROCK : Shape(1)
    object PAPER : Shape(2)
    object SCISSORS : Shape(3)

    /**
     * Compares two shapes with normal rules of the game
     */
    private fun compareTo(other: Shape): Result {
        return when (this) {
            PAPER -> when (other) {
                PAPER -> Result.DRAW
                ROCK -> Result.WIN
                SCISSORS -> Result.LOSE
            }

            ROCK -> when (other) {
                PAPER -> Result.LOSE
                ROCK -> Result.DRAW
                SCISSORS -> Result.WIN
            }

            SCISSORS -> when (other) {
                PAPER -> Result.WIN
                ROCK -> Result.LOSE
                SCISSORS -> Result.DRAW
            }
        }
    }

    /**
     * "Battles" one shape with another one and returns the result of the match
     */
    fun match(player: Shape) = player.compareTo(this)
}

private enum class Result(val points: Int) {
    WIN(6),
    DRAW(3),
    LOSE(0)
}

private fun String.toResult() = when (this) {
    "X" -> Result.LOSE
    "Y" -> Result.DRAW
    "Z" -> Result.WIN
    else -> throw IllegalArgumentException("Input value not supported")
}

private fun String.toShape() = when (this) {
    "A", "X" -> Shape.ROCK
    "B", "Y" -> Shape.PAPER
    "C", "Z" -> Shape.SCISSORS
    else -> throw IllegalArgumentException("Input value not supported")
}