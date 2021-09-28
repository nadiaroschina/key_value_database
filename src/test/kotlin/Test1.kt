import org.junit.jupiter.api.assertThrows
import kotlin.test.*

internal class Test1 {

    @Test
    fun testGetQuery() {
        val args1 = arrayOf("add", "key1", "value1")
        val expected1 = Query(QueryType.Add, arrayOf("key1", "value1"))
        assertEquals(expected1, getQuery(args1))

        val args2 = arrayOf("clear")
        val expected2 = Query(QueryType.Clear, emptyArray())
        assertEquals(expected2, getQuery(args2))

        assertThrows<Exception>  { getQuery(emptyArray()) }
        assertThrows<Exception>  { getQuery(arrayOf("ruin")) }
        assertThrows<Exception>  { getQuery(arrayOf("ruin elem")) }
        assertThrows<Exception>  { getQuery(arrayOf("ruin elem1 elem2")) }

    }
}
