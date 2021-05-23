import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

fun `map test`() {
    Mono.just("hello")
        .map {
            "$it world"
        }.subscribe {
            println(it)
        }
}
fun `flat map test`() {
    Mono.just(listOf("Hello", "World"))
        .flatMap {
            val res = it + "!!!!"
            Mono.just("$res")
        }.subscribe {
            println(it)
        }
}
fun `filter test`() {
    Mono.just("filter")
        .filter {
            it == "filter"
        }.subscribe {
            println("$it True!")
        }
}
fun `zip when test`() {
    Mono.just("hello")
        .zipWhen {
            Mono.just("$it world")
        }.subscribe { it: Tuple2<String, String> ->
            println("${it.t1}, ${it.t2}")
        }
}
fun `zip with test`() {

    Mono.just("hello")
        .zipWith(Mono.just("world"))
        .subscribe { it: Tuple2<String, String> ->
            println("${it.t1}, ${it.t2}")
        }
}
fun `concat with test`() {
    Mono.just("hello")
        .concatWith(Mono.just("world"))
        .subscribe {
            println(it)
        }
}
object Mono_Operation {
    @JvmStatic
    fun main(args: Array<String>) {
        println(">> map")
        `map test`()
        println(">> flatMap")
        `flat map test`()
        println(">> filter")
        `filter test`()
        println(">> zipWhen")
        `zip when test`()
        println(">> zipWith")
        `zip with test`()
        println(">> concatWith")
        `concat with test`()
    }
}