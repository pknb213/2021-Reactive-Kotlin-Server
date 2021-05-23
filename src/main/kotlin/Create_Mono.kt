import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.NullPointerException
import java.time.Duration
import java.util.concurrent.CountDownLatch

private fun testMethod1(): Mono<String> {
    return Mono.just("> one")
        .delayElement(Duration.ofSeconds(1))
        .doOnNext {
            println(it)
        }
}

private fun testMethod2(): Mono<String> {
    return Mono.just(">> two")
        .doOnNext {
            println(it)
        }
}
fun zip() {
    val countDownLatch = CountDownLatch(1)
    Mono.zip(testMethod1(), testMethod2())
        .subscribe({}, {}, {
            countDownLatch.countDown()
        })
    countDownLatch.await()
}

fun `when test`() {
    val countDownLatch = CountDownLatch(1)

    Mono.`when`(testMethod1(), testMethod2())
        .subscribe({}, {}, {
            countDownLatch.countDown()
        })
    countDownLatch.await()
}

fun `delay test`() {
    val countDownLatch = CountDownLatch(1)

    Mono.delay(Duration.ofSeconds(1))
        .doOnNext {
            println(it)
        }.subscribe({}, {}, { countDownLatch.countDown() })

    countDownLatch.await()
}

fun `delay error test`() {
    val countDownLatch = CountDownLatch(1)

    Mono.zipDelayError(testMethod1(), Mono.error<NullPointerException>(NullPointerException()), testMethod1(), Mono.error<IllegalArgumentException>(IllegalArgumentException()))
        .subscribe({ }, {
            println(it)
            countDownLatch.countDown()
        }, {
            countDownLatch.countDown()
        })

    countDownLatch.await()
}

object Mono {
    @JvmStatic
    fun main(args: Array<String>) {
        // Just
        val just = Mono.just("Hello Reactor")
            .subscribe {
                println(">>> just : $it")
            }
        // FromSupplier
        val fromSupplier = Mono.fromSupplier { "Hello Suupplier" }
            .subscribe { print(">>> fromSupplier : $it")}
        // Error
//        val error = Mono.error<String>(NullPointerException())
//            .block()
        // Zip
        println(">>> Zip")
        val zip = zip()
        // When
        println(">>> When")
        val when_test = `when test`()
        // Delay
        println(">>> Delay")
        val delay = `delay test`()
        // Defer
        val defer = Mono.just("Defer")
            .switchIfEmpty(Mono.defer {Mono.just("As")})
            .subscribe { println(">>> Defer : $it")}
        // From
        Mono.from(Flux.just(1, 2, 3, 4, 5))
            .subscribe { println(">>> from : $it") }
        // DelayError
        `delay error test`()
    }
}