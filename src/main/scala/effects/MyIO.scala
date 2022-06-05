package effects

case class MyIO[A](unsafeRun: () => A) {
  def map[B](f: A => B): MyIO[B] =
    MyIO(() => f(unsafeRun()))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())
}

object MyIO {
  def putStr(s: => String): MyIO[Unit] = {
    MyIO(() => println(s))
  }
}

object Printing extends App {
  val hello = MyIO.putStr("hello!")
  val world = MyIO.putStr("world!")
  val helloWold: MyIO[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()
  helloWold.unsafeRun()
}
