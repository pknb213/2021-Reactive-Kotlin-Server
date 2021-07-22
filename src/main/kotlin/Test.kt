/* SimpleApp2.kt */
@file:JvmName("SimpleApp2")
import org.jetbrains.kotlinx.spark.api.*

fun main() {
    val logFile = "src/main/kotlin/test.txt" // Change to your Spark Home path
    val str = "Happy Hello World"
    println("Happy Hello World with 'p' : ${str.filter { 
        it.toString().contains("p")}.count()
    }\n")
    withSpark {
        spark.read().textFile(logFile).withCached {
            println(">>> ${this.show()}")
            val word = "a"
            val allCnt = this.map {
                it.sumOf { itt ->
                    itt.toString().count()
                }
            }.groupBy().sum()
            val filterCnt = this.map {
                val cnt = it.filter { itt ->
                    itt.toString().contains(word)
                }.count()
//                println(">> Filter: $cnt")
                cnt
            }.groupBy().sum()
            allCnt.show()
            filterCnt.show()
            val allNums = this.count()
            println(">> Line nums: $allNums, Char nums: ${allCnt.head()[0]}, Lines with word: ${filterCnt.first()[0]}")
        }
    }
}
