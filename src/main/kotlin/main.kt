import java.io.File
import QueryType.*

typealias Key = String
typealias Value = String

data class Element(val key: Key, val value: Value) {
    override fun toString(): String {
        return ("$key $value")
    }
}
typealias Database = MutableMap<Key, Value>


// reads all the elements from file and writes them in database
fun readFile(file: File): Database {
    val db: Database = mutableMapOf()
    file.readLines().forEach {
        val (key, value) = it.split(' ')
        db[key] = value
    }
    return db
}

// writes all the elements from database to file
fun writeFile(file: File, db: Database) {
    file.writeText("")
    db.forEach {
        file.appendText("${it.key} ${it.value}\n")
    }
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
fun addSingle(elem: Element, database: Database) {
    val (key, value) = elem
    if (database.containsKey(key)) {
        throw Exception("Invalid key: $key is already used")
    }
    database[key] = value
}

// adds elements to database
fun add(args: Array<String>, database: Database) {
    val elems = parseArgs(args)
    elems.forEach { addSingle(it, database) }
}

// deletes one element from database
fun deleteSingle(key: Key, database: Database) {
    if (database.containsKey(key)) {
        database.remove(key)
    } else {
        throw Exception("Key $key not found")
    }
}

// deletes elements from database
fun delete(args: Array<String>, database: Database) {
    val elems = parseArgs(args)
    elems.forEach { deleteSingle(it.key, database) }
}

// gets the value by its key
fun getSingle(key: Key, database: Database): Value {
    if (database.containsKey(key)) {
        return database[key]!!
    } else {
        throw Exception("Key $key not found")
    }
}

// gets the values by their keys
fun get(args: Array<String>, database: Database) : Array<Value> {
    val res = mutableListOf<Value>()
    args.forEach { res.add(getSingle(it, database)) }
    return res.toTypedArray()
}

// deletes all the elements from database
fun clear(database: Database) {
    database.clear()
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
fun getQuery(args: Array<String>): Query {
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
fun produceQuery(query: Query, database: Database, file: File) {
    when (query.queryType) {
        Add -> add(query.args, database)
        Delete -> delete(query.args, database)
        Get -> {
            val values = get(query.args, database)
            println(values.joinToString("\n"))
        }
        Clear -> clear(database)
    }

    when (query.queryType) {
        Add, Delete, Clear -> writeFile(file, database)
        Get -> {
        }
    }
}

fun main(args: Array<String>) {

    // extracting file to database
    val file = File("src/data/database.txt")
    val database: Database = readFile(file)

    // processing input
    val query = getQuery(args)

    // producing the query
    produceQuery(query, database, file)

}
