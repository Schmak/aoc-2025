import Day10.Input
import java.util.*

object Day10 : Task<List<Input>> {
    override fun parse(input: List<String>) = input.map { line ->
        val tokens = line.split(" ")
        val target = tokens[0].trim('[', ']').replace(".", "0").replace("#", "1").reversed().toInt(2)
        val buttons = tokens.drop(1).dropLast(1).map { token ->
            token.trim('(', ')').split(",").map { it.toInt() }.sumOf { 1.shl(it) }
        }
        Input(target, buttons)
    }


    fun part1(input: List<Input>): Int = input.sumOf { task ->
        (0..(1.shl(task.buttons.size)))
            .map { BitSet.valueOf(longArrayOf(it.toLong())) }
            .filter { bitSet ->
                val result = task.buttons.foldIndexed(0) { buttonIdx, acc, button ->
                    acc xor (if (bitSet.get(buttonIdx)) button else 0)
                }
                result == task.target
            }
            .minOf { it.cardinality() }
    }

    fun solve() {
        val testInput = readInput("Day10_test")
        assertThat(part1(testInput)).isEqualTo(7)

        val input = readInput("Day10")
        assertThat(part1(input)).isEqualTo(578)
        println(part1(input))
    }

    data class Input(
        val target: Int,
        val buttons: List<Int>,
    )
}

fun main() = Day10.solve()
