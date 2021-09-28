import java.io.File
import QueryType.*

typealias Key = String
typealias Value = String

val database = File("../data/database")

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
fun add(query: Query) {
    TODO()
}

// deletes one element from database
fun delete(query: Query) {
    TODO()
}

// gets the element by the key
fun get(query: Query) {
    TODO()
}

// deletes all the elements from database
fun clear(query: Query) {
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
        return "Query: ${queryType.toString()} \n Arguments: $args"
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
fun produceQuery(query: Query) {
    when (query.queryType) {
        Add -> add(query)
        Delete -> delete(query)
        Get -> get(query)
        Clear -> TODO()
    }
}

fun main(args: Array<String>) {

    // processing input
    val query = getQuery(args)
    println(query)

    // producing the query
    produceQuery(query)
}




