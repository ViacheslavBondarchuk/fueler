package io.github.viacheslavbondarchuk.processor

import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.Message

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 12:09 PM
 * */
trait TelegramMessageProcessor[M <: Message, S <: Messages[Future]] {
  def handle(message: M): Unit
}
