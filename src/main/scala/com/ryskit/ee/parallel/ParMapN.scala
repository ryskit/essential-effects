package com.ryskit.ee.parallel

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ryskit.ee.debug._

object ParMapN extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    par.as(ExitCode.Success)

  val hello = IO("hello").debug
  val world = IO("world").debug

  val par = (hello, world)
    .parMapN((h, w) => s"$h $w")
    .debug
}
