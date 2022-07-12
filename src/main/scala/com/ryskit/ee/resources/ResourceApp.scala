package com.ryskit.ee.resources

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.catsSyntaxTuple3Semigroupal

object ResourceApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    resources
      .use { case (a, b, c) =>
        applicationLogic(a, b, c)
      }
      .as(ExitCode.Success)

  val resources: Resource[IO, (DependencyA, DependencyB, DependencyC)] =
    (resourceA, resourceB, resourceC).tupled

  val resourceA: Resource[IO, DependencyA] = ???
  val resourceB: Resource[IO, DependencyB] = ???
  val resourceC: Resource[IO, DependencyC] = ???

  def applicationLogic(
      a: DependencyA,
      b: DependencyB,
      c: DependencyC
  ): IO[ExitCode] = ???

  trait DependencyA
  trait DependencyB
  trait DependencyC
}
