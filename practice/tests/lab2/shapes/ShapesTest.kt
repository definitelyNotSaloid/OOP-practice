package lab2.shapes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ShapesTest {
    @Test
    fun squareTest(){
        assertEquals(100.0, Square(10.0, 0u,0u).area)
    }

    @Test
    fun rectangleTest(){
        assertEquals(50.0, Rectangle(10.0, 5.0, 0u,0u).area)
    }

    @Test
    fun circleTest(){
        assertEquals(314.1593, Circle(10.0, 0u, 0u).area, 0.1)
    }

    @Test
    fun triangleTest(){
        assertEquals(6.0, Triangle(3.0, 4.0, 5.0, 0u, 0u).area, 0.0001)
    }
}