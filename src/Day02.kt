object Day02 : Task<List<LongRange>> {
    override fun parse(input: List<String>): List<LongRange> =
        input.first().split(",")
            .map { range -> range.split("-").map { it.toLong() }.let { LongRange(it.first(), it.last()) } }

    fun solve(input: List<LongRange>, limit: Int? = null): Long =
        input.sumOf { range -> range.sumOf { if (it.isValid(limit)) 0L else it } }

    fun part1(input: List<LongRange>): Long = solve(input, 2)

    fun part2(input: List<LongRange>): Long = solve(input)

    fun solve() {
        val testInput = readInput("Day02_test")
        assertThat(part1(testInput)).isEqualTo(1227775554)
        assertThat(part2(testInput)).isEqualTo(4174379265)

        val input = readInput("Day02")
        assertThat(part1(input)).isEqualTo(31210613313)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(41823587546)
        println(part2(input))
    }
}

private fun Long.isValid(chunksLimit: Int?): Boolean {
    val str = this.toString()
    val length = str.length
    val limit = chunksLimit ?: length
    for (i in 2..limit) {
        if (length % i != 0) continue
        val chunks = str.chunked(length / i)
        if (chunks.all { chunk -> chunks[0] == chunk }) return false
    }
    return true
}

fun main() = Day02.solve()
