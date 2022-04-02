package lab2.shapes

import kotlin.math.sqrt

// made implementations data classes here. too lazy to override equals and hashCode myself


interface IShape2d {
    val area: Double
}

interface IDrawableShape2d : IShape2d {
    val borderColor: UInt           // RGBA
    val fillColor: UInt             // RGBA
}

interface ICircle : IDrawableShape2d {
    val radius: Double
}

data class Circle(
    override val radius: Double,
    override val fillColor: UInt,
    override val borderColor: UInt
) : ICircle {

    private val _area = Math.PI * radius * radius

    override val area: Double
        get() = _area

    init {
        require(radius > 0.0)
        { "radius must be positive" }
    }
}


interface IRectangle : IDrawableShape2d {
    val height: Double
    val width: Double
}

data class Rectangle(
    override val height: Double,
    override val width: Double,
    override val fillColor: UInt,
    override val borderColor: UInt
) : IRectangle {

    private val _area = height * width

    override val area: Double
        get() = _area

    init {
        require(height > 0.0 && width > 0.0)
        { "both rectangle sides must be positive" }
    }
}

data class Square(
    private val side: Double,
    override val fillColor: UInt,
    override val borderColor: UInt
) : IRectangle {

    private val _area = side * side


    override val height: Double
        get() = side

    override val width: Double
        get() = side

    override val area: Double
        get() = _area


    init {
        require(side > 0.0)
        { "square side must be positive" }
    }
}


interface ITriangle : IDrawableShape2d {
    val sideA: Double
    val sideB: Double
    val sideC: Double

    // you can come up with a ton of other triangle props, but just 3 sides is enough imho
}

data class Triangle(
    override val sideA: Double,
    override val sideB: Double,
    override val sideC: Double,
    override val borderColor: UInt,
    override val fillColor: UInt
) : ITriangle {

    private val _area: Double

    init {
        require(sideA > 0.0 && sideB > 0.0 && sideC > 0.0)
        { "triangle sides must be positive" }

        require(
            (sideA + sideB) > sideC
                    && (sideA + sideC) > sideB
                    && (sideB + sideC) > sideA
        )
        { "triangle with such sides does not exists" }

        val p = (sideA + sideB + sideC) * 0.5                 // half-perimeter
        _area = sqrt(p * (p - sideA) * (p - sideB) * (p - sideC))
    }

    override val area: Double
        get() = _area
}



