package com.ryskit.ee.resources

import cats.effect.{IO, Resource}
import com.ryskit.ee.debug._

import java.io.RandomAccessFile

class FileBufferReader private (in: RandomAccessFile) {
  def readBuffer(offset: Long): IO[(Array[Byte], Int)] =
    IO {
      in.seek(offset)

      val buf = new Array[Byte](FileBufferReader.bufferSize)
      val len = in.read(buf)

      (buf, len)
    }

  private def close: IO[Unit] = IO(in.close())
}

object FileBufferReader {
  val bufferSize = 4096

  def makeResource(fileName: String): Resource[IO, FileBufferReader] =
    Resource.make(
      IO(new FileBufferReader(new RandomAccessFile(fileName, "r")))
    ) { res =>
      res.close
    }
}
