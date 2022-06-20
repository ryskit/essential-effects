package com.ryskit.ee.parallel

import cats.implicits._
import cats.effect.IO

object IOComposition extends App {
  val hello = IO(println(s"[${Thread.currentThread().getName}] Hello"))
  val world = IO(println(s"[${Thread.currentThread().getName}] World"))

  val hw1: IO[Unit] =
    for {
      _ <- hello
      _ <- world
    } yield ()

  val hw2: IO[Unit] =
    (hello, world).mapN((_, _) => ())

  hw1.unsafeRunSync()
  hw2.unsafeRunSync()
}
