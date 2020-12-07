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

    def takeUntil(predicate: T => Boolean):List[T] = {
      underlying.span(predicate) match {
        case (head, tail) => head ::: tail.take(1)
      }
    }
  }

  implicit class StringUtils(underlying: String) {
    def replaceCharAt(i: Int, c: Char): String = {
      s"${underlying.substring(0, i)}$c${underlying.substring(i+1)}"
    }
  }
}
