class EmptyQuery() :
    Exception("Empty query")

class InvalidArguments(args: List<String>) :
    Exception("Invalid number of arguments: cant separate $args into pairs of key and value")

class UnsupportedQuery(queryName: String) :
    Exception("Query $queryName is not supported")