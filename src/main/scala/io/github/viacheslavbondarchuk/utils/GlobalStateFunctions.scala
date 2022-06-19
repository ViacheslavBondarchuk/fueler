package io.github.viacheslavbondarchuk.utils

import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.{Message, User}
import io.github.viacheslavbondarchuk.domain.GlobalStateEnum.GlobalStateEnum
import io.github.viacheslavbondarchuk.domain._
import io.github.viacheslavbondarchuk.holder.InternalStateHolder

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 1:37 PM
 * */
object GlobalStateFunctions {
  private val functions: Map[GlobalStateEnum, (User, Message, Messages[Future]) => TelegramMessage] = Map(
    GlobalStateEnum.INIT -> initFunction,
    GlobalStateEnum.ADD_FUEL -> addFuelFunction,
    GlobalStateEnum.SHOW_COSTS_BY_PERIOD -> showCostsByPeriodFunction
  )

  private def initFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    InternalStateFunctions.getFunction(InternalStateHolder.getInternalState(user.id, InternalStateEnum.MARK))(user, message, sender)
  }

  private def addFuelFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    InternalStateFunctions.getFunction(InternalStateHolder.getInternalState(user.id, InternalStateEnum.FUEL_QUANTITY))(user, message, sender)
  }

  private def showCostsByPeriodFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    InternalStateFunctions.getFunction(InternalStateHolder.getInternalState(user.id, InternalStateEnum.FUEL_INFO_BY_PERIOD))(user, message, sender)
  }

  def getFunction(maybeState: Option[GlobalState]): (User, Message, Messages[Future]) => TelegramMessage = {
    maybeState match {
      case Some(value) => functions.getOrElse(value.state, throw new RuntimeException("Unknown state"))
      case None => throw new RuntimeException("Unknown state")
    }
  }

}
