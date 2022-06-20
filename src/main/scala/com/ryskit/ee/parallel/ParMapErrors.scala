package com.ryskit.ee.parallel

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ryskit.ee.debug._

import scala.concurrent.duration.DurationInt

object ParMapErrors extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    e1.attempt.debug *>
      IO("---").debug *>
      e2.attempt.debug *>
      IO("---").debug *>
      e3.attempt.debug *>
      IO.pure(ExitCode.Success)

  val ok = IO("hi").debug
//  val ko1 = IO.raiseError[String](new RuntimeException("oh!")).debug
  val ko1 = IO.sleep(1.second).as("ko1").debug *>
    IO.raiseError[String](new RuntimeException("oh!")).debug
  val ko2 = IO.raiseError[String](new RuntimeException("noes!")).debug

//  val e1 = (ok, ko1).parMapN((_, _) => ())
  val e1 = (ok, ko1).parTupled.void
  val e2 = (ko1, ok).parMapN((_, _) => ())
  val e3 = (ko1, ko2).parMapN((_, _) => ())
}
