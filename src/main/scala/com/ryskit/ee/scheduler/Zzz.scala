package com.ryskit.ee.scheduler

import cats.effect.IO

trait Zzz {
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}
