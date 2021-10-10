import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.*

internal class InternalFunctionsTest {

    @Test
    fun testGetQuery() {
        val args1 = arrayOf("add", "key1", "value1")
        val expected1 = Query(QueryType.Add, listOf("key1", "value1"))
        assertEquals(expected1, getQuery(args1))

        val args2 = arrayOf("clear")
        val expected2 = Query(QueryType.Clear, emptyList())
        assertEquals(expected2, getQuery(args2))

        assertThrows<IllegalArgumentException> { getQuery(emptyArray()) }
        assertThrows<IllegalArgumentException> { getQuery(arrayOf("ruin")) }
        assertThrows<IllegalArgumentException> { getQuery(arrayOf("ruin elem")) }
        assertThrows<IllegalArgumentException> { getQuery(arrayOf("ruin elem1 elem2")) }
    }

    @Test
    fun testBinHash() {
        val milk1 = "milk"
        val milk2 = "milk"
        assertEquals(binHash(milk1), binHash(milk1))
        assertEquals(binHash(milk1), binHash(milk2))

        val Milk = "Milk"
        val milka = "milka"
        assertNotEquals(binHash(milk1), binHash(Milk))
        assertNotEquals(binHash(milk1), binHash(milka))

        val keyEn = "coconut_milk"
        val keyRus = "кокосовое-молоко!"
        assertNotNull(binHash(keyEn))
        assertNotNull(binHash(keyRus))
    }

    @Test
    fun testParseArgs() {
        val args1 = listOf("1", "2", "3", "4", "5")
        assertThrows<IllegalArgumentException> { parseArgs(args1) }

        val args2 = listOf("a", "b", "c", "d", "e", "f")
        val expected = listOf(Element("a", "b"), Element("c", "d"), Element("e", "f"))
        assertEquals(parseArgs(args2), expected)
    }

    @Test
    fun testGetFullDir() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val dirmilk1 = getFullDir(head, "milk")
        val dirmilk2 = getFullDir(head, "milk")
        println(dirmilk1)
        assertTrue { dirmilk1.startsWith("src\\test\\testdata") or dirmilk1.startsWith("src/test/testdata") }

        val dirMilk = getFullDir(head, "Milk")
        assertEquals(dirmilk1, dirmilk2)
        assertNotEquals(dirmilk1, dirMilk)

        assertDoesNotThrow {
            getFullDir(head, "almond_milk")
            getFullDir(head, "-миндальное-молоко-")
            getFullDir(head, "*(*!@972")
        }
        head.deleteRecursively()
    }
}

// checking correctness
internal class SmallTestsDatabase {
    @Test
    fun testAddSingle() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val cake = Element("cake", "flour,eggs,milk")
        val cake2 = Element("cake", "flour,sugar,cream")
        val soup = Element("soup", "water,meat,potatoes")

        addSingle(cake, head)
        assertEquals(getSingle("cake", head), "flour,eggs,milk")
        assertEquals(getSingle("soup", head), null)

        addSingle(soup, head)
        assertEquals(getSingle("cake", head), "flour,eggs,milk")
        assertEquals(getSingle("soup", head), "water,meat,potatoes")

        addSingle(cake2, head)
        assertEquals(getSingle("cake", head), "flour,sugar,cream")
        assertEquals(getSingle("soup", head), "water,meat,potatoes")


        head.deleteRecursively()
    }

    @Test
    fun testDeleteSingle() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val cake = Element("cake", "flour,eggs,milk")
        val porridge = Element("porridge", "oats,milk")

        assertEquals(getSingle("cake", head), null)
        assertEquals(getSingle("porridge", head), null)

        addSingle(cake, head)
        addSingle(porridge, head)
        assertEquals(getSingle("cake", head), "flour,eggs,milk")
        assertEquals(getSingle("porridge", head), "oats,milk")

        deleteSingle("porridge", head)
        assertEquals(getSingle("porridge", head), null)
        assertEquals(getSingle("cake", head), "flour,eggs,milk")

        deleteSingle("marmalade", head)
        assertEquals(getSingle("porridge", head), null)
        assertEquals(getSingle("cake", head), "flour,eggs,milk")

        deleteSingle("cake", head)
        assertEquals(getSingle("cake", head), null)
        assertEquals(getSingle("porridge", head), null)

        head.deleteRecursively()
    }

    @Test
    fun testSingles() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val marmalade = Element("marmalade", "berries,sugar")
        val marmalade2 = Element("marmalade", "juice,jelly")
        val porridge = Element("porridge", "oats,milk")

        assertEquals(getSingle("cake", head), null)
        assertEquals(getSingle("porridge", head), null)

        addSingle(marmalade, head)
        addSingle(porridge, head)

        clear(head)

        assertEquals(getSingle("cake", head), null)
        assertEquals(getSingle("porridge", head), null)

        addSingle(marmalade, head)
        assertEquals(getSingle("marmalade", head), "berries,sugar")
        assertEquals(getSingle("porridge", head), null)

        addSingle(marmalade2, head)
        assertEquals(getSingle("marmalade", head), "juice,jelly")

        clear(head)
        assertEquals(getSingle("marmalade", head), null)

        head.deleteRecursively()
    }

}

// checking effectiveness
internal class BigTestsDatabase {
    @Test
    // around 10 sec
    fun testAdd() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val elemList1 = List(1000) { "a${it / 2}" }
        val keys1 = List(500) { "a$it" }
        val elemList2 = List(2000) { "b${it / 2}" }
        val keys2 = List(1000) { "b$it" }

        add(elemList1, head)
        assertEquals(get(keys1, head), keys1)

        add(elemList1, head)
        assertEquals(get(keys1, head), keys1)

        add(elemList2, head)
        assertEquals(get(keys1, head), keys1)
        assertEquals(get(keys2, head), keys2)

        head.deleteRecursively()
    }

    @Test
    // around 25 sec
    fun testDelete() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val elemList1 = List(1000) { "a${it / 2}" }
        val keys1 = List(500) { "a$it" }
        val elemList2 = List(2000) { "b${it / 2}" }
        val keys2 = List(1000) { "b$it" }

        delete(keys1, head)
        assertEquals(get(keys1, head), List(500) { null })

        add(elemList1, head)
        delete(keys1, head)
        add(elemList2, head)
        assertEquals(get(keys1, head), List(500) { null })
        assertEquals(get(keys2, head), keys2)

        delete(keys2, head)
        assertEquals(get(keys2, head), List(1000) { null })

        head.deleteRecursively()
    }

    @Test
    // around 20 sec
    fun bigTest() {
        val head = File("src/test/testdata")
        head.mkdirs()

        val elemList = List(2000) { "${it / 2}" }
        val keys = List(1000) { "$it" }

        add(elemList, head)
        delete(keys, head)

        head.deleteRecursively()
    }
}
