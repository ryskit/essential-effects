package com.ryskit.ee.context

import cats.effect.{ExitCode, IO, IOApp}
import com.ryskit.ee.debug._

object Shifting extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("one").debug
      _ <- IO.shift
      _ <- IO("two").debug
      _ <- IO.shift
      _ <- IO("three").debug
    } yield ExitCode.Success
}
