package com.ryskit.ee.coordination

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ryskit.ee.debug._

import scala.concurrent.duration.DurationInt

object IsThirteenLatch extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      latch <- CountdownLatch(13)
      _ <- (beeper(latch), tickingClock(latch)).parTupled
    } yield ExitCode.Success

  def beeper(latch: CountdownLatch) =
    for {
      _ <- latch.await
      _ <- IO("BEEP!").debug
    } yield ()

  def tickingClock(latch: CountdownLatch): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ <- latch.decrement
      _ <- tickingClock(latch)
    } yield ()
}
