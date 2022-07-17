package com.ryskit.ee.coordination

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import com.ryskit.ee.debug._
import cats.implicits._

object RefUpdateImpure extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      ref <- Ref[IO].of(0)
      _ <- List(1, 2, 3).parTraverse(task(_, ref))
    } yield ExitCode.Success

  def task(id: Int, ref: Ref[IO, Int]): IO[Unit] =
    ref
      .modify(previous => id -> IO(s"$previous->$id").debug)
      .flatten
      .replicateA(3)
      .void
}
