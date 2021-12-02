package day02

data class Point2D (val x: Int = 0, val y: Int = 0){
    fun add(other: Point2D) = Point2D(x + other.x, y + other.y)
    fun innerProd() = x * y
}