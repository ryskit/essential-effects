package com.ryskit.ee.effects

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

// exercise 1
object Timing extends App {
  val clock: MyIO[Long] = MyIO(() => System.currentTimeMillis())

  def time[A](action: MyIO[A]): MyIO[(FiniteDuration, A)] = for {
    start <- clock
    a <- action
    end <- clock
  } yield (FiniteDuration(end - start, TimeUnit.MILLISECONDS), a)

  val timedHello = Timing.time(MyIO.putStr("hello"))

  timedHello.unsafeRun() match {
    case (duration, _) => println(s"'hello' took $duration")
  }
}
