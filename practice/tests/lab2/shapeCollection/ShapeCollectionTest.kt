package lab2.shapeCollection

import lab2.shapes.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


// not much to test here actually. SC is a wrapper for standard list
internal class ShapeCollectionTest {

    private val triangle = Triangle(3.0, 4.0, 5.0, 0xFFFFFFFFu, 0x00000000u)
    private val rectangle = Rectangle(10.0, 5.0, 0xFFFF0000u, 0x00000000u)
    private val circle = Circle(10.0, 0xFFFFFFFFu, 0x0000FFFFu)
    private val square = Square(10.0, 0xFFFF0000u, 0x0000FFFFu)

    private var testSubject : IShapeCollection = ShapeCollection()

    @BeforeEach
    fun initSubj() {
        testSubject = ShapeCollection()

        testSubject.add(square)
        testSubject.add(triangle)
        testSubject.add(rectangle)
        testSubject.add(circle)
    }

    @Test
    fun allMatching() {
        val request1 = testSubject.allMatching { it is IRectangle }
        assertTrue{
            request1.all{ it is IRectangle } && request1.size == 2
        }

        val request2 = testSubject.allMatching { it is IDrawableShape2d && it.borderColor==0x0000FFFFu }
        assertTrue{
            request2.all { it is IDrawableShape2d && it.borderColor==0x0000FFFFu } && request2.size == 2
        }
    }

    @Test
    fun groupByBorderColor() {
        val res = testSubject.groupByBorderColor()

        assertTrue(res.all{  pair ->
            pair.value.all{it.borderColor==pair.key}
        })
    }

    @Test
    fun groupByFillColor() {
        val res = testSubject.groupByFillColor()

        assertTrue(res.all{  pair ->
            pair.value.all{it.fillColor==pair.key}
        })
    }

    @Test
    fun maxByArea() {
        assertEquals(circle, testSubject.maxByArea())
    }

    @Test
    fun minByArea() {
        assertEquals(triangle, testSubject.minByArea())
    }

    @Test
    fun totalArea() {
        assertEquals(470.0, testSubject.totalArea(), 2.0)
    }
}

