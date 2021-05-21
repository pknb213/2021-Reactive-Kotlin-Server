import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.netty.NettyOutbound
import reactor.netty.http.server.HttpServer
import reactor.netty.http.server.HttpServerRequest
import reactor.netty.http.server.HttpServerResponse
import kotlin.reflect.typeOf

object Server {
    init {
        val TEST_DB_NAME = "userhabit"
        val TEST_COLLECTION_NAME = "app"

        val flux = Flux.just(7, 4, 11 ,3)
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .scan { acc, value -> acc + value }
            .collectList()
            .subscribeOn(Schedulers.parallel())
            .block()
    }
    fun get(): Mono<String> {
        println("Hell~")
        val client = MongoClients.create("mongodb://localhost:27017")
        val ls = client.listDatabases()
        val mono = Mono
            .from(ls)
            .map {
                it.map {
                    println(it.toString())
                }
            }
            .toString()
//        val db = client.getDatabase("userhabit")
//        val coll = db.getCollection("app")
//        val flux = Flux
//            .from(coll.find())
//            .collectList()
//            .map {
//                println(it.toString())
//                it.map {
//                    println(it.toString())
//                }
//                it.toString()
//            }

        return Mono.just(mono.toString())
    }
    @JvmStatic
    fun main(args: Array<String>) {
        val server = HttpServer.create()
            .route{
                it
                    .get("/ping") { req, res -> res.sendString(Mono.just("Pong\n")) }
                    .get("/test") { req, res ->
                        val client = MongoClients.create("mongodb://localhost:27017")
                        val ls = client.listDatabases()
//                        val mono = Mono
//                            .from(ls)
//                            .map {
//                                println(it.toString())
//                                it.toString()
//                            }
                        val flux = Flux
                            .just(3,4,8)

                        println(flux)
                        res.sendString(Mono.just(flux.toString()))
                    }
            }
            .port(4500)
            .bindNow()

        println(" > > > Hello")
        server.onDispose()
            .block()
    }
}