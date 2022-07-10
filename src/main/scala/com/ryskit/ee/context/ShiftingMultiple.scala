package com.ryskit.ee.context

import cats.effect.{ExitCode, IO, IOApp}
import com.ryskit.ee.debug._

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object ShiftingMultiple extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (ec("1"), ec("2")) match {
      case (ec1, ec2) =>
        for {
          _ <- IO("one").debug
          _ <- IO.shift(ec1)
          _ <- IO("two").debug
          _ <- IO.shift(ec2)
          _ <- IO("three").debug
        } yield ExitCode.Success
    }

  def ec(name: String): ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor { r =>
      val t = new Thread(r, s"pool-$name-thread-1")
      t.setDaemon(true)
      t
    })
}
