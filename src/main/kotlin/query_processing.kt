import java.io.File
import QueryType.*

enum class QueryType(val str: String) {
    Add("add"), Delete("delete"), Get("get"), Clear("clear")
}

data class Query(val queryType: QueryType, val args: List<String>)

// function checks the correctness of query type and returns the query
fun getQuery(args: Array<String>): Query {
    if (args.isEmpty()) {
        throw EmptyQuery()
    }

    val queryName = args[0]
    val queryArgs = (args.copyOfRange(1, args.size)).toList()

    for (queryType in values()) {
        if (queryType.str == queryName) {
            return Query(queryType, queryArgs)
        }
    }
    throw UnsupportedQuery(queryName)
}

// function redirects the query to appropriate function
fun processQuery(query: Query, head: File) {
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