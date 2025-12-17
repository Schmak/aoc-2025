import Day06.Sample

object Day06 : Task<List<Sample>> {
    override fun parse(input: List<String>): List<Sample> {
        val operators = input.last()
        val indices = operators.indices.filter { operators[it] != ' ' } + (input.maxOf { it.length } + 1)
        return indices.zipWithNext().map { (start, end) ->
            Sample(
                operator = operators[start],
                operands = input.dropLast(1).map { it.substring(start, minOf(it.length, end - 1)) }
            )
        }
    }

    fun part1(input: List<Sample>): Long =
        input.sumOf { calculate(it.operator, it.operands.converted) }

    fun part2(input: List<Sample>): Long =
        input.sumOf { sample ->
            val len = sample.operands.maxOf { it.length }
            val operands = sample.operands.map { it.padEnd(len, ' ') }
            val newOperands = List(len) { i -> operands.map { it[i] }.joinToString("") }
            calculate(sample.operator, newOperands.converted)
        }

    val List<String>.converted
        get() = map { it.trim().toLong() }

    fun calculate(operator: Char, operands: List<Long>): Long = when (operator) {
        '+' -> operands.sum()
        '*' -> operands.fold(1) { acc, op -> acc * op }
        else -> error("Unknown operator $operator")
    }

    fun solve() {
        val testInput = readInput("Day06_test")
        assertThat(part1(testInput)).isEqualTo(4277556)
        assertThat(part2(testInput)).isEqualTo(3263827)

        val input = readInput("Day06")
        assertThat(part1(input)).isEqualTo(6371789547734)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(11419862653216)
        println(part2(input))
    }

    data class Sample(
        val operator: Char,
        val operands: List<String>,
    )
}

fun main() = Day06.solve()
