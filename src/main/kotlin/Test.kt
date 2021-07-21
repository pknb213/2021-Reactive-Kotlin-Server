/* SimpleApp.kt */
@file:JvmName("SimpleApp")
import org.jetbrains.kotlinx.spark.api.*

fun main() {
    val logFile = "src/main/kotlin/test.txt" // Change to your Spark Home path
    withSpark {
        spark.read().textFile(logFile).withCached {
//            val numAs = filter { it.contains("a") }.count()
//            val numBs = filter { it.contains("b") }.count()
            val allNums = this.count()
            println(">>> ${this.show()}")
            println(">> ${this.getRows(5, 1)}")
            val numBs = 2
            println("Lines with a: $allNums, lines with b: $numBs")
        }
    }
}
