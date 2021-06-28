import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.Document

object MongoUtil {
        private const val DB_NAME = "userhabit"

        private val db by lazy {

            val client = MongoClients.create("mongodb://localhost:27017")
            client.getDatabase(DB_NAME)
        }
        fun getCollection(collectionName: String): MongoCollection<Document> {
            return db.getCollection(collectionName)
        }
}