import org.junit.jupiter.api.assertThrows
import kotlin.test.*

internal class Test1 {

    @Test
    fun testGetQuery() {
        val args1 = arrayOf("add", "key1", "value1")
        val expected1 = Query(QueryType.Add, arrayOf("key1", "value1"))
        assertEquals(expected1, getQuery(args1))

        val args2 = arrayOf("create", "database1")
        val expected2 = Query(QueryType.Create, arrayOf("database1"))
        assertEquals(expected2, getQuery(args2))

        assertThrows<Exception>  { getQuery(emptyArray()) }
        assertThrows<Exception>  { getQuery(arrayOf("delete")) }
        assertThrows<Exception>  { getQuery(arrayOf("edit key1")) }
    }
}
