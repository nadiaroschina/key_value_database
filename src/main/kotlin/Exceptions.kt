class EmptyQuery() :
    Exception("Empty query")

class InvalidArguments(args: List<String>) :
    Exception("cant separate $args into pairs of key and value")

class UnsupportedQuery(queryName: String) :
    Exception("\"$queryName\" is not supported")