val f = Range.inclusive(1, 8, 1)
val b = Range.inclusive(-1, -8, -1)
//val zero = Range.inclusive(0, 0, 0)

f.map((_, 0)) ++ f.map((0, _)) ++ b.map((_, 0)) ++ b.map((0, _))

f.zip(f) ++ f.zip(b) ++ b.zip(f) ++ b.zip(b)


f.zip(f).takeWhile { case (i, i1) => i <= 2 }


def takeUntil[T](underlying: List[T], predicate: T => Boolean):List[T] = {
  underlying.span(predicate) match {
    case (head, tail) => head ::: tail.take(1)
  }
}

takeUntil[Int](f.toList, _ < 22)

val s = "1011"
//s.charAt(0) == "1"
s.charAt(0) == '1'
//s.charAt(0) == "1"