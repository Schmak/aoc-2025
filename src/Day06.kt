import Day06.Input
import Day06.Operator.ADD
import Day06.Operator.MUL

object Day06 : Task<Input> {
    val ws = "\\s+".toRegex()
    override fun parse(input: List<String>): Input =
        Input(
            operands = input.dropLast(1).map { line -> line.trim().split(ws).map { it.toLong() } },
            operators = input.last().trim().split(ws).map {
                when (it) {
                    "+" -> ADD
                    "*" -> MUL
                    else -> error("Unknown operator $it")
                }
            }
        )

    fun part1(input: Input): Long =
        input.operators.mapIndexed { idx, operator ->
            when (operator) {
                ADD -> input.operands.sumOf { it[idx] }
                MUL -> input.operands.fold(1L) { acc, op -> acc * op[idx] }
            }
        }.sum()


    fun solve() {
        val testInput = readInput("Day06_test")
        assertThat(part1(testInput)).isEqualTo(4277556)

        val input = readInput("Day06")
//        assertThat(part1(input)).isEqualTo(598)
        println(part1(input))
//        assertThat(part2(input)).isEqualTo(360341832208407)
//        println(part2(input))
    }

    data class Input(
        val operands: List<List<Long>>,
        val operators: List<Operator>
    )

    enum class Operator { ADD, MUL }
}

fun main() = Day06.solve()
