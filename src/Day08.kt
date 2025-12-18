import Day08.Point
import utils.UnionFind

object Day08 : Task<List<Point>> {
    override fun parse(input: List<String>) =
        input.map { line -> line.split(",").map(String::toInt).let { Point(it[0], it[1], it[2]) } }

    fun part1(input: List<Point>, connections: Int): Long {
        val uf = UnionFind(input.size)
        calculateDistance(input)
            .take(connections)
            .forEach { uf.union(it.idx1, it.idx2) }
        val roots = input.indices.mapTo(mutableSetOf()) { idx -> uf.find(idx) }
        val clusters = roots.map { uf.size[it] }.sortedDescending()
        return clusters.take(3).fold(1L) { acc, i -> acc * i }

    }

    fun part2(input: List<Point>): Long {
        val uf = UnionFind(input.size)
        calculateDistance(input)
            .forEach {
                if (uf.union(it.idx1, it.idx2)) {
                    if (uf.size.max() == input.size)
                        return input[it.idx1].x.toLong() * input[it.idx2].x
                }
            }
        error("No solution found")
    }

    private fun calculateDistance(input: List<Point>): List<PointPair> =
        buildList {
            input.forEachIndexed { idx1, point1 ->
                input.forEachIndexed { idx2, point2 ->
                    if (idx1 < idx2)
                        add(PointPair(idx1, idx2, point1.distanceTo(point2)))
                }
            }
        }.sortedBy { it.distance }

    fun solve() {
        val testInput = readInput("Day08_test")
        assertThat(part1(testInput, 10)).isEqualTo(40)
        assertThat(part2(testInput)).isEqualTo(25272)

        val input = readInput("Day08")
        assertThat(part1(input, 1000)).isEqualTo(175500)
        println(part1(input, 1000))
        assertThat(part2(input)).isEqualTo(6934702555)
        println(part2(input))
    }

    data class Point(val x: Int, val y: Int, val z: Int) {
        fun distanceTo(other: Point): Long {
            val dx = (x - other.x).toLong()
            val dy = (y - other.y).toLong()
            val dz = (z - other.z).toLong()
            return dx * dx + dy * dy + dz * dz
        }
    }

    data class PointPair(
        val idx1: Int,
        val idx2: Int,
        val distance: Long,
    )
}

fun main() = Day08.solve()
