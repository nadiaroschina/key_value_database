import java.io.File
import QueryType.*

typealias Key = String
typealias Value = String
data class Element(val key: Key, val value: Value) {
    override fun toString(): String {
        return ("$key $value")
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
 *    2.2) series of queries "get"
 *    2.3) series of queries "delete"
 * 3) extra:
 *    3.1) clear database
 */


// adds one element to database
fun addSingle(elem: Element, database: File) {
    for (line in database.readLines()) {
        val (key, _) = line.split(' ')
        if (elem.key == key) {
            throw Exception("Key ${elem.key} already exists in database")
        }
    }
    database.appendText(elem.toString())
    database.appendText("\n")
}

// adds elements to database
fun add(args: Array<String>, database: File) {
    if (args.size % 2 == 1) {
        throw Exception("Invalid arguments for add: $args")
    }
    // parsing arguments
    for (ind in args.indices step 2) {
        val elem = Element(args[ind], args[ind + 1])
        addSingle(elem, database)
    }
}

// deletes elements from database
fun delete(args: Array<String>, database: File) {
    TODO()
}

// gets the values by their keys
fun get(args: Array<String>, database: File) {
    TODO()
}

// deletes all the elements from database
fun clear() {
    TODO()
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

    override fun toString(): String {
        var s = "Query: ${queryType.toString()} \nArguments: "
        for (arg in args) {
            s += "$arg "
        }
        return s
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
fun produceQuery(query: Query, database: File) {
    when (query.queryType) {
        Add -> add(query.args, database)
        Delete -> delete(query.args, database)
        Get -> get(query.args, database)
        Clear -> TODO()
    }
}

fun main(args: Array<String>) {

    val database = File("src/data/database.txt")

    // processing input
    val query = getQuery(args)

    // producing the query
    produceQuery(query, database)
}




