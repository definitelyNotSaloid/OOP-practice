package lab4.misc

import java.util.Calendar
import kotlin.random.Random

object Rand {
    private val rng = Random(Calendar.getInstance().time.hashCode())

    fun next(): Double {
        return rng.nextDouble()
    }

    fun randomCellInRect(
        upperLeft: Vector2dInt,
        lowerRight: Vector2dInt
    ): Vector2dInt {
        require(upperLeft.x < lowerRight.x && upperLeft.y < lowerRight.y)
        { "given range contains no elements" }

        return Vector2dInt(rng.nextInt(upperLeft.x, lowerRight.x), rng.nextInt(upperLeft.y, lowerRight.y))
    }
}