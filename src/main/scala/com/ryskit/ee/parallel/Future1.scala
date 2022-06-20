package com.ryskit.ee.parallel

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import cats.implicits._

object Future1 extends App {
  implicit val ec = ExecutionContext.global

  val hello = Future(println(s"[${Thread.currentThread().getName}] Hello"))
  val world = Future(println(s"[${Thread.currentThread().getName}] World"))

  val hw1: Future[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()

  Await.ready(hw1, 5.seconds)

  val hw2: Future[Unit] =
    (hello, world).mapN((_, _) => ())

  Await.ready(hw2, 5.seconds)
}
