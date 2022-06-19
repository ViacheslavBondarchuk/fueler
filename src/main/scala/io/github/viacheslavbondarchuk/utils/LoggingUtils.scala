package io.github.viacheslavbondarchuk.utils

import org.slf4j.Logger

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 11:20 AM
 * */
object LoggingUtils {

  private def getStackTraceElement: StackTraceElement = {
    Thread.currentThread().getStackTrace()(3)
  }

  def debug[A](handler: => A, stackTraceElement: StackTraceElement = getStackTraceElement)(implicit logger: Logger): A = {
    val result: A = handler
    logger.debug(s"Caller: ${stackTraceElement.getClassName}, Method: ${stackTraceElement.getMethodName}, Message: ${String.valueOf(result)}")
    result
  }

  def logError(ex: Throwable)(implicit logger: Logger, msg: String = "Error: "): Unit = logger.error(msg, ex)

}
