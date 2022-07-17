package com.ryskit.ee.coordination

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ryskit.ee.debug._

import scala.concurrent.duration.DurationInt

// this code has a problem
object ConcurrentStateVar extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- (tickingClock, printTicks).parTupled
    } yield ExitCode.Success

  var ticks: Long = 0L

  val tickingClock: IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ = (ticks = ticks + 1)
      _ <- tickingClock
    } yield ()

  val printTicks: IO[Unit] =
    for {
      _ <- IO.sleep(5.seconds)
      _ <- IO(s"TICKS: $ticks").debug.void
      _ <- printTicks
    } yield ()
}
