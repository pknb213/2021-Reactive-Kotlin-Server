import com.mongodb.reactivestreams.client.MongoClients
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.netty.http.server.HttpServer
import MongoUtil
import org.bson.Document
import reactor.util.Loggers.getLogger
import reactor.util.function.Tuples
import java.lang.Math.random
import java.time.Duration
import java.util.*
import kotlin.concurrent.timer
import org.jetbrains.kotlinx.spark.api.*
import org.jetbrains.kotlinx.spark.api.SparkLogLevel.*


object Server {
    init {
        val TEST_DB_NAME = "userhabit"
        val TEST_COLLECTION_NAME = "app"

//        val flux = Flux.just(7, 4, 11 ,3)
//            .map { it + 1; println("- ${it+1}"); it + 1 }
//            .filter { it % 2 == 0 }
//            .scan { acc, value -> acc + value }
//            .map {
//                println("-- $it")
//                it
//            }
//            .collectList()
//            .subscribeOn(Schedulers.parallel())
//            .subscribe{
//                println(">> $it")
//            }
    }
    fun get(): String {
        val client = MongoClients.create("mongodb://localhost:27017")
        val db = client.getDatabase("userhabit")
        val coll_ls = db.listCollections()
        val coll = db.getCollection("app")
        val doc = coll.find()
        val ls = client.listDatabases()
        val mono = Flux
            .from(doc)
            .map {
                println(">> $it")
                it
            }
            .subscribe()
        return mono.toString()
    }
    @JvmStatic
    fun main(args: Array<String>) {
        println("<< Welcome >>")
//        val log = Loggers.getLogger(this.javaClass)
        val server = HttpServer.create()
            .route{
                it
                    .get("/ping") { req, res -> res.sendString(Mono.just("Pong\n")) }
                    .get("/mongo") { req, res -> res.sendString(Mono.just(get())) }
                    .get("/test") { req, res -> res.sendString(Mono.just("1")) }
            }
            .port(4500)
            .bindNow()

        // Spark Test
//        val logFile = "/Users/cheon-youngjo/Downloads/spark-3.1.2-bin-hadoop3.2/README.md"
//        withSpark {
//            spark.read().textFile(logFile).withCached {
//                val numAs = filter {it.contains("a")}.count()
//                val numBs = filter {it.contains("b")}.count()
//                println("Lines with a: $numAs, lines with b: $numBs")
//            }
//        }

        val spark = SparkSession
            .builder()
            .master("local[2]")
            .appName("Simple App").orCreate
        spark.sparkContext.setLogLevel(ERROR)

        spark.toDS(listOf("a" to 1, "b" to 2)).showDS()


        server.onDispose().block()
    }
}