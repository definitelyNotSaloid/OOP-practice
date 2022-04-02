package lab3.noteCollection

import lab3.log.Logger
import lab3.note.INote

interface INoteCollection {

    fun <T> add(item: T) where T : INote<*>

    fun remove(predicate: (INote<*>) -> Boolean)
    fun removeAll(predicate: (INote<*>) -> Boolean)
    // to get specific type of notes you can use a predicate like
    // { it is SomeNote }

    fun findFirst(predicate: (INote<*>) -> Boolean): INote<*>?
    fun findAll(predicate: (INote<*>) -> Boolean): List<INote<*>>

    fun <T> sortedBy(comparedValue: (INote<*>) -> T): List<INote<*>> where T : Comparable<T>
    // to sort by date (well, its already sorted by it to be honest) you can use { it.created }
    // same logic for sorting by title
}

class NoteCollection : INoteCollection {
    private val notes: MutableList<INote<*>> = mutableListOf()

    override fun findFirst(predicate: (INote<*>) -> Boolean): INote<*>? {
        for (note in notes) {
            if (predicate(note)) {
                Logger.message("Single element requested. Found")
                return note
            }
        }
        Logger.message("Single element requested. Not found")
        return null
    }

    override fun findAll(predicate: (INote<*>) -> Boolean): List<INote<*>> {
        val res = mutableListOf<INote<*>>()
        for (note in notes) {
            if (predicate(note)) {
                res.add(note)
            }
        }
        Logger.message("Multiple element requested. Found ${res.size} instances")

        return res
    }

    override fun <T : INote<*>> add(item: T) {
        Logger.message("Added new note")
        notes.add(item)
    }

    override fun remove(predicate: (INote<*>) -> Boolean) {
        for (i in notes.indices) {
            if (predicate(notes[i])) {
                notes.removeAt(i)
                Logger.message("Single element removal requested. Removed at index $i")
                return
            }
        }

        Logger.message("Single element removal requested. Not found")
    }

    override fun removeAll(predicate: (INote<*>) -> Boolean) {
        val before = notes.size
        notes.removeAll(predicate)
        val after = notes.size
        Logger.message("Multiple element removal requested. Removed ${before - after} elements")
    }

    override fun <T : Comparable<T>> sortedBy(comparedValue: (INote<*>) -> T): List<INote<*>> {
        val comparator = Comparator<INote<*>> { left, right ->
            val leftVal = comparedValue(left)
            val rightVal = comparedValue(right)

            return@Comparator leftVal.compareTo(rightVal)
        }
        Logger.message("Sorted note list requested")
        return notes.sortedWith(comparator)
    }
}