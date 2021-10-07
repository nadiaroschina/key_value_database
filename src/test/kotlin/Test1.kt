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
    // TODO
}

// checking effectiveness
internal class BigTestsDatabase {
    // TODO
}