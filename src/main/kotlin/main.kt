import QueryType.*
import java.io.File


typealias Key = String
typealias Value = String

data class Element(val key: Key, val value: Value) {
    override fun toString(): String {
        return ("<$key, $value>")
    }
}

// binary hash. returns the same binary string
fun binHash(str: String): String {
    return (kotlin.math.abs(str.hashCode())).toString(2)
}

// returns the directory that should contain given key
fun getFullDir(headDir: File, key: Key): String {
    val hash = binHash(key)
    var currDir = headDir
    hash.forEach {
        val childDir = File(currDir, it.toString())
        childDir.mkdirs()
        currDir = childDir
    }
    return currDir.path
}

// checks that the key doesn't contain '/' or '\' symbols
fun checkKey(key: Key): Boolean {
    return !(key.contains("/") || key.contains("\\"))
}

/*
 * types of queries that the database should be able to perform
 * 1) one-operation:
 *    1.1) add value to database
 *    1.2) delete key
 *    1.3) get value from key
 * 2) multi-operation:
 *    2.1) series of queries "add"
 *    2.2) series of queries "delete"
 *    2.3) series of queries "get"
 * 3) extra:
 *    3.1) clear database
 */


// parses arguments into elements with key and value
fun parseArgs(args: List<String>): List<Element> {
    if (args.size % 2 == 1) {
        throw IllegalArgumentException("Invalid number of arguments")
    }
    val elements = mutableListOf<Element>()
    for (ind in args.indices step 2) {
        elements.add(Element(args[ind], args[ind + 1]))
    }
    return elements
}

// adds one element to database
fun addSingle(elem: Element, head: File) {

    val (key, value) = elem

    if (!checkKey(key)) {
        println("Key $key is invalid")
    } else {
        // to avoid file names such as "0" or "1"
        val filename = "key_$key"

        val dir = File(getFullDir(head, key))
        dir.mkdirs()

        val file = File(dir, filename)
        if (file.exists()) {
            val oldValue = file.readText()
            if (oldValue == value) {
                println("Key $key already stores value $value")
            } else {
                file.writeText(value)
                println("Key $key changed its value to $value. Old value was $oldValue")
            }
        } else {
            file.createNewFile()
            file.writeText(elem.value)
            println("Key ${elem.key} now has the value ${elem.value}")
        }

    }
}

// adds elements to database
fun add(elems: List<Element>, head: File) {
    elems.forEach { addSingle(it, head) }
}

// deletes one element from database
fun deleteSingle(key: Key, head: File) {

    if (!checkKey(key)) {
        println("Key $key is invalid")
    } else {

        val dirPath = getFullDir(head, key)
        val filename = "key_$key"
        var file = File(dirPath, filename)

        if (file.exists()) {
            val value = file.readText()
            file.delete()
            // deleting empty head directories
            while (!file.parentFile.path.endsWith("data") && file.parentFile.length() == 0L) {
                file = file.parentFile
                file.delete()
            }
            println("Key $key deleted; its value was $value")
        } else {
            println("Key $key not found in database")
        }
    }
}

// deletes elements from database
fun delete(args: List<String>, head: File) {
    args.forEach { deleteSingle(it, head) }
}

// gets the value by its key
fun getSingle(key: Key, head: File): Value? {
    return if (!checkKey(key)) {
        println("Key $key is invalid")
        null
    } else {
        val filename = "key_$key"
        val dirPath = getFullDir(head, key)
        val file = File(dirPath, filename)
        if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
}

// gets the values by their keys
fun get(args: List<String>, head: File): List<Value?> {
    val res = mutableListOf<Value?>()
    args.forEach { res.add(getSingle(it, head)) }
    return res.toList()
}

// deletes all the elements from database
fun clear(head: File) {
    head.deleteRecursively()
}


enum class QueryType(val str: String) {
    Add("add"), Delete("delete"), Get("get"), Clear("clear")
}

data class Query(val queryType: QueryType, val args: List<String>)

// function checks the correctness of query type and returns the query
fun getQuery(args: Array<String>): Query {
    if (args.isEmpty()) {
        // TODO remove exception throw
        throw IllegalArgumentException("Empty query")
    }

    val queryName = args[0]
    val queryArgs = (args.copyOfRange(1, args.size)).toList()

    for (queryType in values()) {
        if (queryType.str == queryName) {
            return Query(queryType, queryArgs)
        }
    }
    // TODO remove exception throw
    throw IllegalArgumentException("Unsupported query: $queryName")
}

// function redirects the query to appropriate function
fun produceQuery(query: Query, head: File) {
    when (query.queryType) {
        Add -> {
            val elements = parseArgs(query.args)
            add(elements, head)
        }
        Delete -> {
            delete(query.args, head)
        }
        Get -> {
            val values = get(query.args, head)
            println(values.joinToString("\n"))
        }
        Clear -> clear(head)
    }
}

fun main(args: Array<String>) {

    // opening head directory
    val head = File("src/data/")
    head.mkdirs() // creating directory if it doesn't exist

    // processing input
    val query = getQuery(args)

    // producing the query
    produceQuery(query, head)

}
