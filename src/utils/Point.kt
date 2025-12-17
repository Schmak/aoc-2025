package utils

data class Point(val x: Int, val y: Int) {
    operator fun plus(vector: Vector) = Point(x + vector.x, y + vector.y)
}