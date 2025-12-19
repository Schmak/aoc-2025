import utils.Point
import kotlin.math.abs

object Day09 : Task<Polygon> {
    override fun parse(input: List<String>) =
        Polygon(input.map { line -> line.split(",").map(String::toInt).let { Point(it[0], it[1]) } })

    fun part1(input: Polygon): Long =
        input.points.pairs().maxOf { (p1, p2) -> area(p1, p2) }

    fun part2(input: Polygon): Long =
        input.points.pairs()
            .filter { (p1, p2) -> Rectangle.create(p1, p2).isInside(input) }
            .maxOf { (p1, p2) -> area(p1, p2) }

    private fun area(p1: Point, p2: Point): Long {
        val dx = abs(p1.x - p2.x).toLong() + 1
        val dy = abs(p1.y - p2.y).toLong() + 1
        return dx * dy
    }

    fun List<Point>.pairs() =
        this.asSequence().flatMapIndexed { i, p1 -> this.asSequence().drop(i).map { p2 -> p1 to p2 } }

    fun solve() {
        val testInput = readInput("Day09_test")
        assertThat(part1(testInput)).isEqualTo(50)
        assertThat(part2(testInput)).isEqualTo(24)

        val input = readInput("Day09")
        assertThat(part1(input)).isEqualTo(4776487744)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1560299548)
        println(part2(input))
    }
}

data class Polygon(val points: List<Point>) {
    val segments by lazy { (points + points[0]).zipWithNext().map { (p1, p2) -> Segment.create(p1, p2) } }
    val verticalSegments by lazy { segments.filterIsInstance<VerticalSegment>().sortedBy { it.x } }
    val horizontalSegments by lazy { segments.filterIsInstance<HorizontalSegment>().sortedBy { it.y } }
}

sealed interface Segment {
    companion object {
        fun create(p1: Point, p2: Point): Segment =
            if (p1.y == p2.y) {
                HorizontalSegment(y = p1.y, x1 = minOf(p1.x, p2.x), x2 = maxOf(p1.x, p2.x))
            } else {
                VerticalSegment(x = p1.x, y1 = minOf(p1.y, p2.y), y2 = maxOf(p1.y, p2.y))
            }
    }

    fun isInside(polygon: Polygon): Boolean
}

data class Rectangle(val a: Point, val b: Point, val c: Point, val d: Point) {
    val edges = listOf(a, b, c, d, a).zipWithNext { p1, p2 -> Segment.create(p1, p2) }

    fun isInside(polygon: Polygon): Boolean = edges.all { it.isInside(polygon) }

    companion object {
        fun create(p1: Point, p2: Point): Rectangle {
            val xMax = maxOf(p1.x, p2.x)
            val xMin = minOf(p1.x, p2.x)
            val yMax = maxOf(p1.y, p2.y)
            val yMin = minOf(p1.y, p2.y)

            return Rectangle(
                a = Point(xMin, yMin),
                b = Point(xMax, yMin),
                c = Point(xMax, yMax),
                d = Point(xMin, yMax),
            )
        }
    }
}

data class HorizontalSegment(val y: Int, val x1: Int, val x2: Int) : Segment {
    override fun isInside(polygon: Polygon): Boolean {
        var state: HS = HS.Outside
        var startX: Int? = null
        val segments = mutableListOf<HorizontalSegment>()
        polygon.verticalSegments.forEach { verticalSegment ->
            if (y !in verticalSegment.y1..verticalSegment.y2) return@forEach
            val newState = when (y) {
                verticalSegment.y1 -> {
                    when (state) {
                        HS.Inside -> HS.AlongBottomEdge
                        HS.Outside -> HS.AlongTopEdge
                        HS.AlongTopEdge -> HS.Outside
                        HS.AlongBottomEdge -> HS.Inside
                    }
                }

                verticalSegment.y2 -> {
                    when (state) {
                        HS.Inside -> HS.AlongTopEdge
                        HS.Outside -> HS.AlongBottomEdge
                        HS.AlongTopEdge -> HS.Inside
                        HS.AlongBottomEdge -> HS.Outside
                    }
                }

                else -> {
                    when (state) {
                        HS.Outside -> HS.Inside
                        HS.Inside -> HS.Outside
                        HS.AlongTopEdge, HS.AlongBottomEdge -> error("Impossible y1, y2 -> Inside")
                    }
                }
            }
            if (!state.inside && newState.inside) {
                startX = verticalSegment.x
            } else if (state.inside && !newState.inside) {
                segments += HorizontalSegment(y, startX!!, verticalSegment.x)
            }
            state = newState
        }
        return segments.any { this.isInside(it) }
    }

    fun isInside(other: HorizontalSegment): Boolean =
        x1 in other.x1..other.x2 && x2 in other.x1..other.x2

    enum class HS(val inside: Boolean) {
        Outside(false),
        Inside(true),
        AlongTopEdge(true),
        AlongBottomEdge(true),
    }
}

data class VerticalSegment(val x: Int, val y1: Int, val y2: Int) : Segment {
    override fun isInside(polygon: Polygon): Boolean {
        var state: VS = VS.Outside
        var startY: Int? = null
        val segments = mutableListOf<VerticalSegment>()
        polygon.horizontalSegments.forEach { horizontalSegment ->
            if (x !in horizontalSegment.x1..horizontalSegment.x2) return@forEach
            val newState = when (x) {
                horizontalSegment.x1 -> {
                    when (state) {
                        VS.Inside -> VS.AlongRightEdge
                        VS.Outside -> VS.AlongLeftEdge
                        VS.AlongLeftEdge -> VS.Outside
                        VS.AlongRightEdge -> VS.Inside
                    }
                }

                horizontalSegment.x2 -> {
                    when (state) {
                        VS.Inside -> VS.AlongLeftEdge
                        VS.Outside -> VS.AlongRightEdge
                        VS.AlongLeftEdge -> VS.Inside
                        VS.AlongRightEdge -> VS.Outside
                    }
                }

                else -> {
                    when (state) {
                        VS.Outside -> VS.Inside
                        VS.Inside -> VS.Outside
                        VS.AlongLeftEdge, VS.AlongRightEdge -> error("Impossible x1, x2 -> Inside")
                    }
                }
            }
            if (!state.inside && newState.inside) {
                startY = horizontalSegment.y
            } else if (state.inside && !newState.inside) {
                segments += VerticalSegment(x, startY!!, horizontalSegment.y)
            }
            state = newState
        }
        return segments.any { this.isInside(it) }
    }

    fun VerticalSegment.isInside(other: VerticalSegment): Boolean =
        y1 in other.y1..other.y2 && y2 in other.y1..other.y2

    enum class VS(val inside: Boolean) {
        Outside(false),
        Inside(true),
        AlongLeftEdge(true),
        AlongRightEdge(true),
    }
}

fun main() = Day09.solve()
