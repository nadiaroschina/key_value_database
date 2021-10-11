import java.io.File


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


// adds one element to database
fun addSingle(elem: Element, head: File) {

    val (key, value) = elem

    if (!checkKey(key)) {
        println("Key $key is invalid")
    } else {
        // to avoid file names such as "0" or "1"
        val filename = "key_$key"

        val dir = File(getFullDir(head, key))
        dir.mkdirs()

        val file = File(dir, filename)
        if (file.exists()) {
            val oldValue = file.readText()
            if (oldValue == value) {
                println("Key $key already stores value $value")
            } else {
                file.writeText(value)
                println("Key $key changed its value to $value. Old value was $oldValue")
            }
        } else {
            file.createNewFile()
            file.writeText(elem.value)
            println("Key ${elem.key} now has the value ${elem.value}")
        }

    }
}

// adds elements to database
fun add(elems: List<Element>, head: File) {
    elems.forEach { addSingle(it, head) }
}


// deletes one element from database
fun deleteSingle(key: Key, head: File) {

    if (!checkKey(key)) {
        println("Key $key is invalid")
    } else {

        val dirPath = getFullDir(head, key)
        val filename = "key_$key"
        var file = File(dirPath, filename)

        if (file.exists()) {
            val value = file.readText()
            file.delete()
            // deleting empty head directories
            while (!file.parentFile.path.endsWith("data") && file.parentFile.length() == 0L) {
                file = file.parentFile
                file.delete()
            }
            println("Key $key deleted; its value was $value")
        } else {
            println("Key $key not found in database")
        }
    }
}

// deletes elements from database
fun delete(keys: List<Key>, head: File) {
    keys.forEach { deleteSingle(it, head) }
}


// gets the value by its key
fun getSingle(key: Key, head: File): Value? {
    return if (!checkKey(key)) {
        println("Key $key is invalid")
        null
    } else {
        val filename = "key_$key"
        val dirPath = getFullDir(head, key)
        val file = File(dirPath, filename)
        if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
}

// gets the values by their keys
fun get(args: List<String>, head: File): List<Value?> {
    val res = mutableListOf<Value?>()
    args.forEach { res.add(getSingle(it, head)) }
    return res.toList()
}


// deletes all the elements from database
fun clear(head: File) {
    head.deleteRecursively()
}