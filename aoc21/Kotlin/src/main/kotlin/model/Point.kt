package model

data class Point (val x: Int = 0, val y: Int = 0){
    fun add(other: Point) = Point(x + other.x, y + other.y)
    fun innerProd() = x * y
}