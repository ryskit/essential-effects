package com.ryskit.ee.resources

import cats.effect.{ExitCode, IO, IOApp}

object HelloWorld extends IOApp {

  val helloWorld: IO[Unit] =
    IO(println("Hello world!"))

  def run(args: List[String]): IO[ExitCode] =
    helloWorld.as(ExitCode.Success)
}
