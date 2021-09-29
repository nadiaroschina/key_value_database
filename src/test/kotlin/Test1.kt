import org.junit.jupiter.api.assertThrows
import kotlin.test.*

internal class TestQuery {

    @Test
    fun testGetQuery() {
        val args1 = arrayOf("add", "key1", "value1")
        val expected1 = Query(QueryType.Add, arrayOf("key1", "value1"))
        assertEquals(expected1, getQuery(args1))

        val args2 = arrayOf("clear")
        val expected2 = Query(QueryType.Clear, emptyArray())
        assertEquals(expected2, getQuery(args2))

        assertThrows<Exception> { getQuery(emptyArray()) }
        assertThrows<Exception> { getQuery(arrayOf("ruin")) }
        assertThrows<Exception> { getQuery(arrayOf("ruin elem")) }
        assertThrows<Exception> { getQuery(arrayOf("ruin elem1 elem2")) }
    }
}

// testing correctness of queries
internal class SmallTestsDatabase {

    @Test
    fun testClear() {
        val db1 = mutableMapOf<Key, Value>()
        clear(db1)
        assertEquals(emptyMap(), db1)

        val db2 = mutableMapOf<Key, Value>("key" to "val")
        clear(db2)
        assertEquals(emptyMap(), db2)

        val db3 = mutableMapOf<Key, Value>("key1" to "val1", "key1" to "val2", "key3" to "val3")
        clear(db3)
        assertEquals(emptyMap(), db3)
    }

    @Test
    fun testAddSingle() {
        val db = mutableMapOf<Key, Value>()
        val elem1 = Element("key1", "value1")
        val elem1Copy = Element("key1", "value1Copy")
        val elem2 = Element("key2", "value2")
        addSingle(elem1, db)
        assertContains(db, elem1.key)
        assertThrows<Exception> { addSingle(elem1Copy, db) }
        addSingle(elem2, db)
        assertContains(db, elem1.key)
        assertContains(db, elem2.key)
    }

    @Test
    fun testDeleteSingle() {
        val db = mutableMapOf<Key, Value>("key1" to "val1", "key2" to "val2")
        val key1 = "key1"
        val key2 = "key2"
        val key3 = "key3"
        deleteSingle(key1, db)
        assertTrue { !db.containsKey(key1) }
        assertTrue { db.containsKey(key2) }
        assertThrows<Exception> { deleteSingle(key3, db) }
    }

    @Test
    fun testGetSingle() {
        val db = mutableMapOf<Key, Value>("key1" to "val1", "key2" to "val2")
        val dbCopy = db.toMutableMap()
        val elem1 = Element("key1", "val1")
        val elem2 = Element("key2", "val2")
        val elem3 = Element("key3", "val3")
        assertEquals(elem1.value, getSingle(elem1.key, db))
        assertEquals(elem2.value, getSingle(elem2.key, db))
        assertThrows<Exception> { getSingle(elem3.key, db) }
        assertEquals(db, dbCopy)
    }

}

// testing efficiency of queries
internal class BigTestsDatabase {

    @Test
    fun testAdd() {
        TODO()
    }

    fun testDelete() {
        TODO()
    }

    fun testGet() {
        TODO()
    }

}
