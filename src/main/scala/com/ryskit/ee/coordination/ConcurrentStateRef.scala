package com.ryskit.ee.coordination

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

import com.ryskit.ee.debug._
import scala.concurrent.duration.DurationInt

object ConcurrentStateRef extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      ticks <- Ref[IO].of(0L)
      _ <- (tickingClock(ticks), printTicks(ticks)).parTupled
    } yield ExitCode.Success

  def tickingClock(ticks: Ref[IO, Long]): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ <- ticks.update(_ + 1)
      _ <- tickingClock(ticks)
    } yield ()

  def printTicks(ticks: Ref[IO, Long]): IO[Unit] =
    for {
      _ <- IO.sleep(5.seconds)
      n <- ticks.get
      _ <- IO(s"TICKS: $n").debug
      _ <- printTicks(ticks)
    } yield ()
}
