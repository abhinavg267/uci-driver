package chessapi.model

sealed trait Axis {
  def asString: String
}

sealed trait Row extends Axis
object Row {
  case object One extends Row {
    override def asString: String = "1"
  }

  case object Two extends Row {
    override def asString: String = "2"
  }

  case object Three extends Row {
    override def asString: String = "3"
  }

  case object Four extends Row {
    override def asString: String = "4"
  }

  case object Five extends Row {
    override def asString: String = "5"
  }

  case object Six extends Row {
    override def asString: String = "6"
  }

  case object Seven extends Row {
    override def asString: String = "7"
  }

  case object Eight extends Row {
    override def asString: String = "8"
  }
}

sealed trait Column extends Axis
object Column {
  case object A extends Column {
    override def asString: String = "a"
  }

  case object B extends Column {
    override def asString: String = "b"
  }

  case object C extends Column {
    override def asString: String = "c"
  }

  case object D extends Column {
    override def asString: String = "d"
  }

  case object E extends Column {
    override def asString: String = "e"
  }

  case object F extends Column {
    override def asString: String = "f"
  }

  case object G extends Column {
    override def asString: String = "g"
  }

  case object H extends Column {
    override def asString: String = "h"
  }
}

case class Position(row: Row, column: Column) {
  def asString: String = s"${row.asString}${column.asString}"
}
