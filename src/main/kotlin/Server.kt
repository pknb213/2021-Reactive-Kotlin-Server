import com.mongodb.reactivestreams.client.MongoClients
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.netty.http.server.HttpServer
import reactor.util.Loggers
import org.slf4j.Logger
import MongoUtil
import org.bson.Document
import org.slf4j.LoggerFactory
import reactor.util.function.Tuples
import java.time.Duration
import java.util.*
import kotlin.concurrent.timer

object Server {
    private val log = Loggers.getLogger(this.javaClass)
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
        val server = HttpServer.create()
            .route{
                it
                    .get("/ping") { req, res -> res.sendString(Mono.just("Pong\n")) }
                    .get("/mongo") { req, res -> res.sendString(Mono.just(get())) }
                    .get("/test") { req, res ->

                        res.sendString(Mono.just("1"))
                    }
            }
            .port(4500)
            .bindNow()
        """ Test Aggregation """
        var qry = listOf(
            Document("\$match", "")
        )

        Flux
            .from(MongoUtil.getCollection("app")
                .aggregate(qry)
            )
            .map {
                println(">>> $it")
            }
            .subscribe()

        // Test
        Flux.range(1, 20)
            .window(6)
            .subscribe {
                it.collectList()
                    .subscribe{ res -> println(res) }
            }

        server.onDispose().block()
    }
}