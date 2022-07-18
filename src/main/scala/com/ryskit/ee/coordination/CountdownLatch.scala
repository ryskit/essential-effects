package com.ryskit.ee.coordination

import cats.effect.concurrent.{Deferred, Ref}
import cats.effect.{ContextShift, ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxFlatten

trait CountdownLatch {
  def await: IO[Unit]
  def decrement: IO[Unit]
}

sealed trait State
case class Outstanding(n: Long, whenDone: Deferred[IO, Unit]) extends State

case class Done() extends State

object CountdownLatch {
  def apply(n: Long)(implicit cs: ContextShift[IO]): IO[CountdownLatch] =
    for {
      whenDone <- Deferred[IO, Unit]
      state <- Ref[IO].of[State](Outstanding(n, whenDone))
    } yield new CountdownLatch {

      override def await: IO[Unit] =
        state.get.flatMap {
          case Outstanding(_, whenDone) => whenDone.get
          case Done()                   => IO.unit
        }

      override def decrement: IO[Unit] =
        state
          .modify {
            case Outstanding(1, whenDone) =>
              Done() -> whenDone.complete(())
            case Outstanding(n, whenDone) =>
              Outstanding(n - 1, whenDone) -> IO.unit
            case Done() => Done() -> IO.unit
          }
          .flatten
          .uncancelable
    }
}
