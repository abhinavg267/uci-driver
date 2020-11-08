package uci.driver.util

import scala.util.{Failure, Success, Try}

object SafeProcessInteraction {
  /**
   * @param f a interaction with process, which can throw exceptions
   */
  def apply[T, R](f: => T)(onSuccess: T => R): R = {
    Try {
      f
    } match {
      case Failure(exception) =>
        val error = ProcessInteractionError(exception)
        Logger.error(error.getMessage)
        throw error
      case Success(value) => onSuccess(value)
    }
  }
}

/** Throw exception with custom wrapper so that we can catch it wherever required
 * */
case class ProcessInteractionError(error: Throwable) extends Exception {
  override def getMessage: String = s"Failed to interact with Process due to: ${error.getMessage}"
}
