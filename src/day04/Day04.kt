package day04

import readInput

private const val DAY = "04"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun intervalMapping(input: List<String>) = input.map {
        val sections = it.split(',')
        sections[0].toInterval() to sections[1].toInterval()
    }

    fun part1(input: List<String>): Int {
        return intervalMapping(input).count { (a, b) ->
            (a.left >= b.left && a.right <= b.right) || (b.left >= a.left && b.right <= a.right)
        }
    }

    fun part2(input: List<String>): Int {
        return intervalMapping(input).count { (a, b) ->
            a.left in b.left..b.right ||
                    a.right in b.left..b.right ||
                    b.left in a.left..a.right ||
                    b.right in a.left..a.right
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

private data class Interval(val left: Int, val right: Int)

private fun String.toInterval(): Interval {
    val items = this.split('-')
    return Interval(items[0].toInt(), items[1].toInt())
}