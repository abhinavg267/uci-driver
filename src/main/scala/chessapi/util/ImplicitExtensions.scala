package chessapi.util

object ImplicitExtensions {
  implicit class ArrayUtils[T](underlying: Array[T]) {
    def get(i: Int): Option[T] = {
      if(i < underlying.length) Some(underlying(i))
      else None
    }
  }
}
