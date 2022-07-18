package com.ryskit.ee.scheduler

import cats.data.Chain
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.implicits._

trait JobScheduler {
  def schedule(task: IO[_]): IO[Job.Id]
}

object JobScheduler {
  case class State(
      maxRunning: Int,
      scheduled: Chain[Job.Scheduled] = Chain.empty,
      running: Map[Job.Id, Job.Running] = Map.empty,
      completed: Chain[Job.Completed] = Chain.empty
  ) {
    def enqueue(job: Job.Scheduled): State =
      copy(scheduled = scheduled :+ job)

    def dequeue: (State, Option[Job.Scheduled]) = {
      if (running.size >= maxRunning) this -> None
      else
        scheduled.uncons
          .map { case (head, tail) =>
            copy(scheduled = tail) -> Some(head)
          }
          .getOrElse(this -> None)
    }

    def running(job: Job.Running): State =
      copy(running = running + (job.id -> job))

    def onComplete(job: Job.Completed): State =
      copy(completed = completed :+ job)
  }

  def scheduler(stateRef: Ref[IO, JobScheduler.State]): JobScheduler =
    new JobScheduler {
      override def schedule(task: IO[_]): IO[Job.Id] =
        for {
          job <- Job.create(task)
          _ <- stateRef.update(_.enqueue(job))
        } yield job.id
    }

}
