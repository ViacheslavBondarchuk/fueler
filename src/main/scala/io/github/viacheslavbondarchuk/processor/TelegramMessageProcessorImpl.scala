package io.github.viacheslavbondarchuk.processor

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.Message
import io.github.viacheslavbondarchuk.actor.TelegramMessageActor
import io.github.viacheslavbondarchuk.converter.TelegramMessageConverter
import io.github.viacheslavbondarchuk.domain.TelegramMessage

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 12:12 PM
 * */
class TelegramMessageProcessorImpl(sender: Messages[Future]) extends TelegramMessageProcessor[Message, Messages[Future]] {
  private val telegramMessageBehaviour: Behavior[TelegramMessage] = Behaviors.setup((context: ActorContext[TelegramMessage]) => new TelegramMessageActor(context))
  private val telegramMessageActor: ActorRef[TelegramMessage] = ActorSystem(telegramMessageBehaviour, "telegramMessageActor")
  private val telegramMessageConverter: TelegramMessageConverter = TelegramMessageConverter(sender)

  override def handle(message: Message): Unit = telegramMessageActor ! telegramMessageConverter.convert(message)
}

object TelegramMessageProcessorImpl {
  def apply(sender: Messages[Future]): TelegramMessageProcessor[Message, Messages[Future]] = new TelegramMessageProcessorImpl(sender)
}
