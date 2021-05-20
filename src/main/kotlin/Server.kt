import com.mongodb.connection.ClusterSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import reactor.core.publisher.Mono
import reactor.netty.DisposableServer
import reactor.netty.http.server.HttpServer
import reactor.netty.tcp.TcpServer

object Server {
    init {
        val TEST_DB_NAME = "userhabit"
        val TEST_COLLECTION_NAME = "app"
//        val db by lazy {
//            val client = MongoClients.create("mongodb://localhost:27017")
//            client.getDatabase(TEST_DB_NAME)
//        }
//        println(db.listCollections())
//        db.createCollection("Test_Collection")
        val client = MongoClients.create()
        println(">> ${client}")
        val db = client.getDatabase(TEST_DB_NAME)
        println(">> ${db}")
        val coll = db.getCollection(TEST_COLLECTION_NAME)
        println(">> ${coll}")
        val collog = Mono.just(coll).log()
        println(collog)
        collog.subscribe({
            println(it.toString())
        })

    }
    @JvmStatic
    fun main(args: Array<String>) {

        val server = HttpServer.create()
            .route{
                it
                    .get("/ping") { req, res -> res.sendString(Mono.just("Pong\n")) }
            }
            .port(4500)
            .bindNow()

        print(" > > > Hello")
        server.onDispose()
            .block()
    }
}