package day11

import readInput

private const val DAY = "11"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun List<String>.parseMonkeys() = this
        .chunked(7)
        .map { it.parseMonkey() }

    fun List<Monkey>.computeMostActiveMonkeys(repetitions: Int, modifier: Int = 1): Long {
        val modulo = this.fold(1L) { total, monkey ->
            total * monkey.divisible
        }
        repeat(repetitions) {
            this.forEach { monkey ->
                monkey.items
                    .map { worryLevel ->
                        val newItemWorry = monkey.inspectItem(worryLevel % modulo) / modifier
                        val newPosition = monkey.worryTest(newItemWorry)
                        newItemWorry to newPosition
                    }.forEach { (newWorryLevel, newIndex) ->
                        this[newIndex].items.addLast(newWorryLevel)
                    }
                monkey.inspectedItems += monkey.items.size
                monkey.items.clear()
            }
        }

        return this
            .sortedByDescending { it.inspectedItems }
            .take(2)
            .fold(1L) { total, monkey ->
                total * monkey.inspectedItems
            }
    }

    fun part1(input: List<String>) = input
        .parseMonkeys()
        .computeMostActiveMonkeys(20, 3)

    fun part2(input: List<String>) = input
        .parseMonkeys()
        .computeMostActiveMonkeys(10000)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInput(DAY_INPUT)
    println(part1(input)) // 90882
    println(part2(input)) //30893109657
}

private data class Monkey(
    val items: ArrayDeque<Long>,
    val inspectItem: (Long) -> Long,
    val worryTest: (Long) -> Int,
    val divisible: Int
) {
    var inspectedItems = 0
}

private fun List<String>.parseMonkey(): Monkey {
    val items = this[1]
        .split(' ')
        .drop(4)
        .map { it.removeSuffix(",").toLong() }
    val divisibleValue = this[3]
        .split(' ')
        .last()
        .toInt()
    val successIndex = this[4]
        .split(' ')
        .last()
        .toInt()
    val failureIndex = this[5]
        .split(' ')
        .last()
        .toInt()
    val worryTest: (Long) -> Int = { worryLevel ->
        if (worryLevel % divisibleValue == 0L) successIndex else failureIndex
    }
    val (operator, operand) = this[2]
        .split(' ')
        .takeLast(2)
    val operation: (Long) -> Long = { old ->
        val rightOperand = operand.toLongOrNull() ?: old
        if (operator == "+")
            old + rightOperand
        else
            old * rightOperand
    }
    return Monkey(ArrayDeque(items), operation, worryTest, divisibleValue)
}
