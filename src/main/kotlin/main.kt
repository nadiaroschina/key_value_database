import java.io.File
import QueryType.*

typealias Key = String
typealias Value = String

data class Database(val name: String, val file: File)


/*
 * types of queries that the database should be able to perform
 * 1) head:
 *    1.1) create database
 *    1.2) destroy database
 *    ? open existing db
 *    ? close current db
 * 2) one-operation:
 *    2.1) add value to database
 *    2.2) delete key
 *    2.3) get value from key
 * 3) multi-operation:
 *    ? series of queries "add"
 *    ? series of queries "get"
 *    ? series of queries "delete"
 * 4) extra:
 *    ? clear database (delete all)
 */


// creates an empty file which will store the elements of the database
fun create(query: Query): Database {
    if (query.args.size != 1) {
        throw Exception("Invalid database name: ${query.args}")
    }
    val name = query.args[0]
    val path = "../src/data/$name"
    if (File(path).exists()) {
        throw Exception("File $path already exists")
    }
    val file = File(path)
    return Database(name, file)
}

// deletes the file storing database with all its elements
fun destroy(query: Query) {
    TODO()
}

// adds one element to the database
fun add(query: Query) {
    TODO()
}

// deletes one element from the database
fun delete(query: Query) {
    TODO()
}

// gets the element by the key
fun get(query: Query) {
    TODO()
}

// removes the element stores in the key
fun remove(query: Query) {
    TODO()
}


enum class QueryType(val str: String) {
    Create("create"), Destroy("destroy"), Add("add"), Delete("delete"), Get("get"), Clear("clear")
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
            if (queryArgs.isEmpty()) {
                throw Exception("Query $queryName called with no arguments")
            }
            return Query(queryType, queryArgs)
        }
    }
    throw Exception("QueryType $queryName not found")
}

// function redirects the query to appropriate function
fun produceQuery(query: Query) {
    when (query.queryType) {
        Create -> create(query)
        Destroy -> destroy(query)
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




