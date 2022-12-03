package day03

import readInput

private const val DAY = "03"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun countCommonItems(value: String): Int {
        val halfIndex = value.length / 2
        val left = value.substring(0, halfIndex).toSet()
        val right = value.substring(halfIndex).toSet()
        return left.sumOf {
            if (right.contains(it)) it.priority else 0
        }
    }

    fun countCommonItems(a: String, b: String, c: String): Int {
        val aSet = a.toSet()
        val bSet = b.toSet()
        val cSet = c.toSet()
        return aSet.sumOf {
            if (bSet.contains(it) && cSet.contains(it)) it.priority else 0
        }
    }

    fun part1(input: List<String>) = input.sumOf {
        countCommonItems(it)
    }

    fun part2(input: List<String>) = input
        .windowed(3, 3)
        .sumOf { (a, b, c) ->
            countCommonItems(a, b, c)
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

private val Char.priority: Int
    get() {
        return if (this.isLowerCase()) {
            this.code - 'a'.code + 1
        } else {
            this.code - 'A'.code + 27
        }
    }