package lab2.shapes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ShapesBasicTest {
    @Test
    fun squareTest() {
        assertEquals(100.0, Square(10.0, 0u, 0u).area)
    }

    @Test
    fun rectangleTest() {
        assertEquals(50.0, Rectangle(10.0, 5.0, 0u, 0u).area)
    }

    @Test
    fun circleTest() {
        assertEquals(314.1593, Circle(10.0, 0u, 0u).area, 0.1)
    }

    @Test
    fun triangleTest() {
        assertEquals(6.0, Triangle(3.0, 4.0, 5.0, 0u, 0u).area, 0.0001)
    }
}


internal class ShapesExceptionTest {
    @Test
    fun squareTest() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Square(-1.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Square(0.0, 0u, 0u)
        }

    }

    @Test
    fun rectangleTest() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Rectangle(0.0, 0.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Rectangle(1.0, 0.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Rectangle(-1.0, 2.0, 0u, 0u)
        }
    }

    @Test
    fun circleTest() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Circle(0.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Circle(-1.0, 0u, 0u)
        }
    }

    @Test
    fun triangleTest() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Triangle(0.0, 10.0, 10.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Triangle(-10.0, 10.0, 10.0, 0u, 0u)
        }

        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Triangle(2.0, 2.0, 10.0, 0u, 0u)
        }
    }
}