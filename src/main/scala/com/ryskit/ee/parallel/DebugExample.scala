package com.ryskit.ee.parallel

import com.ryskit.ee.debug._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}

object DebugExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    seq.as(ExitCode.Success)

  val hello = IO("hello").debug
  val world = IO("world").debug

  val seq =
    (hello, world)
      .mapN((h, w) => s"$h $w")
      .debug

}
