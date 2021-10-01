import QueryType.*
import java.io.File


typealias Key = String
typealias Value = String

data class Element(val key: Key, val value: Value) {
    override fun toString(): String {
        return ("$key $value")
    }
}

// binary hash
fun binHash(str: String): String {
    return (kotlin.math.abs(str.hashCode())).toString(2)
}

// returns the directory that should contain given key
fun getFullPath(head: File, key: Key): String {
    val hash = binHash(key)
    val builder = StringBuilder()
    builder.append(head.path)
    builder.append('\\')
    hash.forEach {
        builder.append(it)
        builder.append('\\')
    }
    return builder.toString()
}


/*
out-of-date functions
reads all the elements from file and writes them in database
fun readFile(file: File): Database {
    val db: Database = mutableMapOf()
    file.readLines().forEach {
        val (key, value) = it.split(' ')
        db[key] = value
    }
    return db
}

writes all the elements from database to file
fun writeFile(file: File, db: Database) {
    file.writeText("")
    db.forEach {
        file.appendText("${it.key} ${it.value}\n")
    }
}
*/

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
fun parseArgs(args: Array<String>): List<Element> {
    if (args.size % 2 == 1) {
        throw Exception("Invalid number of arguments")
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

    val path = getFullPath(head, key)
    val dir = File(path)

    dir.mkdirs()

    val file = File(dir, key)
    if (file.exists()) {
        val oldValue = file.readText()
        if (oldValue == value) {
            println("Key $key already stores this value.")
        } else {
            file.writeText(value)
            println("Key $key changed its value to $value. Old value was $oldValue.")
        }
    } else {
        file.createNewFile()
        file.writeText(elem.value)
        println("Key ${elem.key} now has the value ${elem.value}.")
    }

}

// adds elements to database
fun add(args: Array<String>) {
    val elems = parseArgs(args)
    //elems.forEach { addSingle(it) }
}

// deletes one element from database
fun deleteSingle(key: Key) {
    TODO()
}

// deletes elements from database
fun delete(args: Array<String>) {
    val elems = parseArgs(args)
    //elems.forEach { deleteSingle(it.key) }
}

// gets the value by its key
fun getSingle(key: Key): Value {
    TODO()
}

// gets the values by their keys
fun get(args: Array<String>): Array<Value> {
    val res = mutableListOf<Value>()
    //args.forEach { res.add(getSingle(it)) }
    return res.toTypedArray()
}

// deletes all the elements from database
fun clear(head: File) {
    head.deleteRecursively()
}


enum class QueryType(val str: String) {
    Add("add"), Delete("delete"), Get("get"), Clear("clear")
}

data class Query(val queryType: QueryType, val args: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Query

        if (queryType != other.queryType) return false
        if (!args.contentEquals(other.args)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryType.hashCode()
        result = 31 * result + args.contentHashCode()
        return result
    }
}

// function checks the correctness of query type and returns the query
fun getQuery(args: Array<String>, head: File): Query {
    if (args.isEmpty()) {
        throw Exception("Empty query")
    }

    val queryName = args[0]
    val queryArgs = args.copyOfRange(1, args.size)

    for (queryType in values()) {
        if (queryType.str == queryName) {
            return Query(queryType, queryArgs)
        }
    }
    throw Exception("QueryType $queryName not found")
}

// function redirects the query to appropriate function
fun produceQuery() {
    TODO()
}

fun main(args: Array<String>) {

    // opening head directory
    val head = File("src/data/")
    head.mkdirs() // creating directory if it doesn't exist

    addSingle(Element("a", "a"), head)
    clear(head)
    addSingle(Element("b", "b"), head)

    // processing input
    // val query = getQuery(args)

    // producing the query
    // produceQuery(query, head)

}
