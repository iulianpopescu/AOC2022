package day13

import readFile

private const val DAY = "13"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun parseInput(input: String): List<Pair<Node, Node>> {
        val lists = input.split("\n\n")
        val pairs = lists.map {
            val (first, second) = it.split('\n')
            first.let {
                index = 0
                parseItem(first)
            } to second.let {
                index = 0
                parseItem(second)
            }
        }
        return pairs
    }

    fun part1(input: String) = parseInput(input).foldIndexed(0) { index, total, (first, second) ->
        if (first.compareTo(second) <= 0) total + index + 1 else total
    }

    fun part2(input: String) = parseInput(input).flatMap {
        listOf(it.first, it.second)
    }.let {
        it + Node.doubleNode(2) + Node.doubleNode(6)
    }.sortedWith { t1, t2 ->
        t1.compareTo(t2)
    }.foldIndexed(1) { index, total, item ->
        val factor = if (item == Node.doubleNode(2) || item == Node.doubleNode(6)) index + 1 else 1
        total * factor
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readFile(DAY_TEST)
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readFile(DAY_INPUT)
    println(part1(input)) // 5198
    println(part2(input)) // 22344
}

private sealed class Node {
    data class Item(val value: Int) : Node()

    data class Sublist(val list: List<Node>) : Node()

    fun compareTo(other: Node): Int = when {
        this is Item && other is Item -> this.value.compareTo(other.value)
        this is Item && other is Sublist -> Sublist(listOf(this)).compareTo(other)
        this is Sublist && other is Item -> this.compareTo(Sublist(listOf(other)))
        else -> {
            val thisList = (this as Sublist).list
            val otherList = (other as Sublist).list
            var comparison: Int? = null
            for (i in 0..minOf(thisList.lastIndex, otherList.lastIndex)) {
                val itemComparison = thisList[i].compareTo(otherList[i])
                if (itemComparison == 0)
                    continue
                comparison = itemComparison
                break
            }
            comparison ?: this.list.size.compareTo(other.list.size)
        }
    }

    companion object {
        fun doubleNode(value: Int) = Sublist(listOf(Sublist(listOf(Item(value)))))
    }
}

private var index = 0
private fun parseItem(input: String): Node = when (input[index]) {
    '[' -> Node.Sublist(buildList { // start a new sublist
        index++
        while (index < input.length && input[index] != ']') { // we reached the end of the current sublist
            add(parseItem(input))
        }
        index++
    })

    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> Node.Item(buildString {
        while (input[index].isDigit()) {
            this.append(input[index])
            index++
        }
    }.toInt())

    else -> {
        // handle comma
        index++
        parseItem(input)
    }
}