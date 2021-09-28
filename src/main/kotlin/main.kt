import kotlin.system.exitProcess

typealias Key = String
typealias Value = String

data class Database(var db: MutableMap<Key, Value>) {

}


/*
 * series of commands that the database should be able to perform
 * 1) head commands:
 *    1.1) create database
 *    1.2) open existing db
 *    1.3) delete db
 *    ? close db
 * 2) one-operation commands:
 *    2.1) add value to database
 *    2.3) get value from key
 *    2.2) delete key
 * 3) multi-operation commands:
 *    3.1) series of queries "add"
 *    3.2) series of queries "get"
 *    3.3) series of queries "delete"
 * 4) extra commands:
 *    4.1) clear database (delete all)
 */

enum class QueryType(val str: String) {
    Create("create"), Add("add"), Get("get"), Delete("delete"), Clear("clear")
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

    for (queryType in QueryType.values()) {
        if (queryType.str == queryName) {
            if (queryArgs.isEmpty()) {
                throw Exception("Query $queryName called with no arguments")
            }
            return Query(queryType, queryArgs)
        }
    }
    throw Exception("QueryType $queryName not found")
}

fun main(args: Array<String>) {

    // processing input
    val query = getQuery(args)
    println("Query: ${query.queryType}")
    println("Arguments: ${query.args}")
}




