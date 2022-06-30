package com.ryskit.ee.parallel

import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import com.ryskit.ee.debug.{DebugHelper, _}

object ParSequence extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    tasks.parSequence.debug.as(ExitCode.Success)

  val numTasks = 100
  val tasks: List[IO[Int]] = List.tabulate(numTasks)(task)

  def task(id: Int): IO[Int] = IO(id).debug
}
