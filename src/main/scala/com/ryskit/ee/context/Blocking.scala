package com.ryskit.ee.context

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import com.ryskit.ee.debug._

object Blocking extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Blocker[IO].use { blocker =>
      withBlocker(blocker).as(ExitCode.Success)
    }

  def withBlocker(blocker: Blocker): IO[Unit] =
    for {
      _ <- IO("on default").debug
      _ <- blocker.blockOn(IO("on blocker").debug)
      _ <- IO("where am I?").debug
    } yield ()
}
