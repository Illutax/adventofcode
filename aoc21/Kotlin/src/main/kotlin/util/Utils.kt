import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.useLines

/**
 * Reads lines from the given input txt file.
 */
fun readInputRaw(name: String) = File("src", "$name.txt").readLines()

/**
 * Reads lines from the given input txt file
 */
fun <T> readInput(name: String, block: (Sequence<String>) -> T) = Path("../input", "day$name.txt").useLines(block = block)

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
