//val str = "bestmove e2e4 ponder e7e6"
val str = "bestmove e2e4"

str.stripPrefix("bestmove").trim.replaceAll(" (.*)", "")

str.replaceAll("(.*) ", "")

val res = str.split(" ")
res(1)
res(3)