package com.ryskit.ee.resources

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.catsSyntaxTuple2Semigroupal
import com.ryskit.ee.debug._

object BasicResourceComposed extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (stringResource, intResource).tupled
      .use {
        case (s, i) =>
          IO(s"$s is so cool!").debug *>
          IO(s"$i is also cool!").debug
      }
      .as(ExitCode.Success)

  val stringResource: Resource[IO, String] =
    Resource.make(
      IO("> acquiring stringResource").debug *> IO("String")
    )(_ => IO("< releasing stringResource").debug.void)

  val intResource: Resource[IO, Int] =
    Resource.make(
      IO("> acquiring intResource").debug *> IO(99)
    )(_ => IO("< releasing intResource").debug.void)
}
