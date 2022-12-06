package day05

import readInput

private const val DAY = "05"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun parseInput(input: List<String>): Pair<List<Stack>, List<Move>> {
        val numberOfStacks = input.first().length / 4 + 1
        val stacks = Array(numberOfStacks) { Stack() }
        var lineIndex = 0
        // parse initial arrangement
        while (!input[lineIndex].startsWith(" 1")) {
            for (j in input[lineIndex].indices step 4) {
                if (input[lineIndex][j] == ' ') continue

                stacks[j / 4].items.add(0, input[lineIndex][j + 1])
            }
            lineIndex++
        }
        // skip stack index and empty line
        lineIndex += 2
        val moves = mutableListOf<Move>()
        while (lineIndex <= input.lastIndex) {
            val parts = input[lineIndex].split(' ')
            moves.add(Move(parts[1].toInt(), parts[3].toInt() - 1, parts[5].toInt() - 1))
            lineIndex++
        }
        return stacks.toList() to moves
    }

    fun part1(input: List<String>): String {
        val (stacks, moves) = parseInput(input)
        moves.forEach { move ->
            repeat(move.itemCount) {
                val item = stacks[move.start].items.removeLastOrNull()
                if (item != null)
                    stacks[move.end].items.add(item)
            }
        }
        return stacks.map { it.items.lastOrNull() ?: " " }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val (stacks, moves) = parseInput(input)
        moves.forEach { move ->
            val items = mutableListOf<Char>()
            repeat(move.itemCount) {
                val item = stacks[move.start].items.removeLastOrNull()
                if (item != null)
                    items.add(0, item)
            }
            stacks[move.end].items.addAll(items)
        }
        return stacks.map { it.items.lastOrNull() ?: " " }.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

data class Stack(val items: MutableList<Char> = mutableListOf())

data class Move(val itemCount: Int, val start: Int, val end: Int)