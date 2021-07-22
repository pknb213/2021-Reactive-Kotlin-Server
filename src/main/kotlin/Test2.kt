/* SimpleApp.kt */
@file:JvmName("SimpleApp")
import org.jetbrains.kotlinx.spark.api.*

fun main() {
    """
    val wordsDataset = sc.parallelize(Seq("Spark I am your father", "May the spark be with you", "Spark I am your father")).toDS()
    val result = wordsDataset
        .flatMap(_.split(" "))               // Split on whitespace
        .filter(_ != "")                     // Filter empty words
        .map(_.toLowerCase())
        .toDF()                              // Convert to DataFrame to perform aggregation / sorting
        .groupBy($"value")                   // Count number of occurrences of each word
    .agg(count("*") as "numOccurances")
    .orderBy($"numOccurances" desc)      // Show most common words first
    result.show()
    """
    withSpark {
        val wordsDataset = sequenceOf("Spark I am your father", "May the spark be with you", "Spark I am your father")
        println(">> ${wordsDataset.javaClass}")
        wordsDataset.map {
            println(">> ${it.toList()}")
        }
        println(">> $wordsDataset")
        println(">> ${wordsDataset.toList().forEach { println(it) }}")
        println(">>> ${wordsDataset.count()}")
    }
}