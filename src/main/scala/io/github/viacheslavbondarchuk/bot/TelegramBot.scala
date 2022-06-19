package io.github.viacheslavbondarchuk.bot

import com.bot4s.telegram.api.declarative.{Commands, Messages}
import com.bot4s.telegram.api.{AkkaTelegramBot, RequestHandler}
import com.bot4s.telegram.clients.AkkaHttpClient
import com.bot4s.telegram.future.Polling
import com.bot4s.telegram.models.Message
import io.github.viacheslavbondarchuk.processor.{TelegramMessageProcessor, TelegramMessageProcessorImpl}

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/10/2022
 * time: 10:47 PM
 * */
class TelegramBot(val token: String) extends AkkaTelegramBot with Polling with Commands[Future] {
  private val telegramMessageProcessor: TelegramMessageProcessor[Message, Messages[Future]] = TelegramMessageProcessorImpl(this)
  override val client: RequestHandler[Future] = new AkkaHttpClient(token)

  onMessage { message: Message =>
    Future(telegramMessageProcessor.handle(message))
  }

}

object TelegramBot {
  def apply(token: String): TelegramBot = new TelegramBot(token)
}


