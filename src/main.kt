import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

val f = flow {
    var i = 1
    while(true){
        emit(i)
        i += 2
    }
}

suspend fun f(){
    delay(200)
    println("Привет из f")
}

fun main(): Unit {
    runBlocking{
        CoroutineScope(Dispatchers.IO).launch{
            delay(500)
            println("Привет из другого CoroutineScope")
        }
        val d = async{
            println("Привет из async")
            delay(700)
            Random.nextInt(1000)
        }
        launch {
            delay(100)
            println("Привет из launch")
            f()
        }
        launch {
            println("Привет из второго launch")
        }
        delay(500)
        println("Привет из main")
        println(d.await())
        try {
            withTimeout(7000) {
                launch {
                    f.collect {
                        println(it)
                        delay(1000)
                    }
                }
            }
        } catch (_: Throwable){}
        println("Повторный запуск потока")
        val job = launch {
            f.collect {
                println(it)
                delay(1000)
            }
        }
        delay(7000)
        job.cancelAndJoin()
    }
    println("Выход")
}