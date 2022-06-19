package io.github.viacheslavbondarchuk.converter

import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.Message
import io.github.viacheslavbondarchuk.constants.Common._
import io.github.viacheslavbondarchuk.db.repository.PostgresRepositories.GlobalStateRepository
import io.github.viacheslavbondarchuk.domain._
import io.github.viacheslavbondarchuk.utils.GlobalStateFunctions
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

/**
 * author: viacheslavbondarchuk
 * date: 6/11/2022
 * time: 1:25 AM
 * */
class TelegramMessageConverter(private val sender: Messages[Future]) {
  private[this] val globalStateRepository: GlobalStateRepository = GlobalStateRepository()
  private[this] implicit val logger: Logger = LoggerFactory.getLogger(classOf[TelegramMessageConverter])

  def convert(message: Message): TelegramMessage = {
    message.text.get match {
      case START_COMMAND => CreateUserMessage(message.from.get.id, GlobalStateEnum.CREATED, message.from.get, message, sender)
      case INIT_COMMAND => InitUserMessage(message.from.get.id, GlobalStateEnum.INIT, message, sender)
      case ADD_FUEL => AddFuelMessage(message.from.get.id, GlobalStateEnum.ADD_FUEL, message, sender)
      case SHOW_COSTS_BETWEEN_PERIOD => ShowCostsByPeriodMessage(message.from.get.id, GlobalStateEnum.SHOW_COSTS_BY_PERIOD, message, sender)
      case _ => Await.result(globalStateRepository.findByUuid(message.from.get.id)
        .map(GlobalStateFunctions.getFunction(_)(message.from.get, message, sender)), 5.seconds)
    }
  }
}

object TelegramMessageConverter {
  def apply(sender: Messages[Future]): TelegramMessageConverter = new TelegramMessageConverter(sender)
}

