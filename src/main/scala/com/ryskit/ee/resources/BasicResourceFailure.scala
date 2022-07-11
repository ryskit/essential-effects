package com.ryskit.ee.resources

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.ryskit.ee.debug._

object BasicResourceFailure extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    stringResource
      .use(_ => IO.raiseError(new RuntimeException("oh noes!")))
      .attempt
      .debug
      .as(ExitCode.Success)

  val stringResource: Resource[IO, String] =
    Resource.make(
      IO("> acquiring stringResource").debug *> IO("String")
    )(_ => IO("< releasing stringResource").debug.void)
}
