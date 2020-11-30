package chessapi.util

object CommonImplicitExtensions {
  implicit class ArrayUtils[T](underlying: Array[T]) {
    def get(i: Int): Option[T] = {
      if(i < underlying.length) Some(underlying(i))
      else None
    }
  }

  implicit class ListUtils[T](underlying: List[T]) {
    def *(that: List[T]): List[(T, T)] = for {
      x <- underlying
      y <- that
    } yield (x, y)
  }
}
