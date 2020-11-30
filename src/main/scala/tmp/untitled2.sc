val f = Range.inclusive(1, 8, 1)
val b = Range.inclusive(-1, -8, -1)
//val zero = Range.inclusive(0, 0, 0)

f.map((_, 0)) ++ f.map((0, _)) ++ b.map((_, 0)) ++ b.map((0, _))

f.zip(f) ++ f.zip(b) ++ b.zip(f) ++ b.zip(b)