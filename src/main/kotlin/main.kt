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

// parses arguments into elements with key and value
fun parseArgs(args: List<String>): List<Element> {
    if (args.size % 2 == 1) {
        throw InvalidArguments(args)
    }
    val elements = mutableListOf<Element>()
    for (ind in args.indices step 2) {
        elements.add(Element(args[ind], args[ind + 1]))
    }
    return elements
}


fun main(args: Array<String>) {

    // opening head directory
    val head = File("src/data/")
    head.mkdirs() // creating directory if it doesn't exist

    // processing input
    val query = getQuery(args)

    // producing the query
    processQuery(query, head)

}
