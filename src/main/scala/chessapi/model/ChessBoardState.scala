package chessapi.model

sealed trait ChessBoardState {
  def state: Array[Array[Option[Piece]]]
  def asString: String = {
    state.map { row =>
      row.map {
        case Some(piece) => String.format("%4s", piece.toString)
        case None => String.format("%4s", "_")
      }.mkString(",")
    }.mkString("\n")
  }
}

object ChessBoardState {
  val initialStateStr: String =
    """ bR, bN, bB, bQ, bK, bB, bN, bR
      | bP, bP, bP, bP, bP, bP, bP, bP
      | _ , _ , _ , _ , _ , _ , _ , _
      | _ , _ , _ , _ , _ , _ , _ , _
      | _ , _ , _ , _ , _ , _ , _ , _
      | _ , _ , _ , _ , _ , _ , _ , _
      | wP, wP, wP, wP, wP, wP, wP, wP
      | wR, wN, wB, wQ, wK, wB, wN, wR
      |""".stripMargin

  val initialState: ChessBoardState = ChessBoardState(initialStateStr)

  private case class ChessBoardStateImpl(state: Array[Array[Option[Piece]]]) extends ChessBoardState
  def apply(state: String): ChessBoardState = {
    val elements = state.split("\n").map { row =>
      row.split(",")
    }

    assert(elements.length == 8, s"Total number of rows in board state should be 8, found ${elements.length}")
    elements.foreach(row => assert(row.length == 8, s"Row $row has length other then 8, ${row.length}"))

    val stateParsed = elements.map(row => row.map(Piece.fromStringOpt))

    ChessBoardStateImpl(stateParsed)
  }
}
