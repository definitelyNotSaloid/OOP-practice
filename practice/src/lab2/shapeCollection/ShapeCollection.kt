package lab2.shapeCollection

import lab2.shapes.*

interface IShapeCollection {
    val count: Int

    fun add(item: IShape2d)

    fun minByArea(): IShape2d?
    fun maxByArea(): IShape2d?
    fun totalArea(): Double

    fun all(): List<IShape2d>
    fun allMatching(predicate: (IShape2d) -> Boolean): List<IShape2d>

    fun groupByFillColor(): Map<UInt, List<IDrawableShape2d>>
    fun groupByBorderColor(): Map<UInt, List<IDrawableShape2d>>
}

class ShapeCollection : IShapeCollection {

    private val listOfShapes = mutableListOf<IShape2d>()

    override val count: Int
        get() = listOfShapes.size

    override fun add(item: IShape2d) {
        listOfShapes.add(item)
    }

    override fun all(): List<IShape2d> {
        return ArrayList(listOfShapes)
    }

    override fun allMatching(predicate: (IShape2d) -> Boolean): List<IShape2d> {
        return listOfShapes.filter(predicate)
    }

    override fun groupByBorderColor(): Map<UInt, List<IDrawableShape2d>> {
        val res = mutableMapOf<UInt, MutableList<IDrawableShape2d>>()
        listOfShapes
            .filterIsInstance<IDrawableShape2d>()
            .forEach {
                if (res.containsKey(it.borderColor))
                    res[it.borderColor]!!.add(it)
                else {
                    res[it.borderColor] = mutableListOf(it)
                }
            }

        return res
    }

    override fun groupByFillColor(): Map<UInt, List<IDrawableShape2d>> {
        val res = mutableMapOf<UInt, MutableList<IDrawableShape2d>>()
        listOfShapes
            .filterIsInstance<IDrawableShape2d>()
            .forEach {
                if (res.containsKey(it.fillColor))
                    res[it.fillColor]!!.add(it)
                else {
                    res[it.fillColor] = mutableListOf(it)
                }
            }

        return res
    }

    override fun maxByArea(): IShape2d? {
        return listOfShapes.maxByOrNull { it.area }
    }

    override fun minByArea(): IShape2d? {
        return listOfShapes.minByOrNull { it.area }
    }

    override fun totalArea(): Double {
        return listOfShapes.sumOf { it.area }
    }
}