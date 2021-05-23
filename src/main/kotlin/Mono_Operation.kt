import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

object Mono_Operation {
    @JvmStatic
    fun main(args: Array<String>) {
        println(">> Map")
        `map test`()
        println(">> FlatMap")
        `flat map test`()
        println(">> Filter")
        `filter test`()
        println(">>  ")
        `filter test`()
        println(">>  ")
        `filter test`()
        println(">>  ")
        `filter test`()
    }
}