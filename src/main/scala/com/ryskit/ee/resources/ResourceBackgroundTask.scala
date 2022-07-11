package com.ryskit.ee.resources

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import com.ryskit.ee.debug._

import scala.concurrent.duration.DurationInt

object ResourceBackgroundTask extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- backgroundTask.use { _ =>
        IO("other work while background task is running").debug *>
        IO.sleep(200.millis) *>
        IO("other work done").debug
      }
      _ <- IO("all done").debug
    } yield ExitCode.Success

  val backgroundTask: Resource[IO, Unit] = {
    val loop =
      (IO("looping...").debug *> IO.sleep(50.millis))
        .foreverM

    Resource
      .make(IO("> forking backgroundTask").debug *> loop.start)(
        IO("< canceling backgroundTask").debug.void *> _.cancel
      )
      .void
  }
}
