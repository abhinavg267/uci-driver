val s: String =
  """ bR, bN, bB, bQ, bK, bB, bN, bR
    | bP, bP, bP, bP, bP, bP, bP, bP
    |   ,   ,   ,   ,   ,   ,   ,
    |   ,   ,   ,   ,   ,   ,   ,
    |   ,   ,   ,   ,   ,   ,   ,
    |   ,   ,   ,   ,   ,   ,   ,
    | wP, wP, wP, wP, wP, wP, wP, wP
    | wR, wN, wB, wQ, wK, wB, wN, wR
    |""".stripMargin

def state(string: String) = {
  string.split("\n").map(_.split(",").map(ele =>
    ele.split("//").head
  ))
}

state(s).length
state(s).map(_.length)


String.format("%3s", "wB")
String.format("%3s", "")