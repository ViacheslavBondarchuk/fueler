package io.github.viacheslavbondarchuk.domain

import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.{Message, User}
import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import io.github.viacheslavbondarchuk.domain.GlobalStateEnum.GlobalStateEnum

import java.time.LocalDate
import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/11/2022
 * time: 12:40 AM
 * */
sealed trait TelegramMessage

case class CreateUserMessage(uuid: Long, state: GlobalStateEnum, user: User, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class WrongMessage(text: String, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class InitUserMessage(uuid: Long, state: GlobalStateEnum, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateMarkMessage(uuid: Long, mark: String, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateModelMessage(uuid: Long, model: String, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateConsumptionMessage(uuid: Long, consumption: Double, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateOdometerMessage(uuid: Long, odometer: Long, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateFuelSortMessage(uuid: Long, fuelSort: FuelSort, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class AddFuelMessage(uuid: Long, state: GlobalStateEnum, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateFuelQuantityMessage(uuid: Long, quantity: Double, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateFuelPriceMessage(uuid: Long, price: Double, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class UpdateFuelOdometerMessage(uuid: Long, odometer: Long, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class ShowCostsByPeriodMessage(uuid: Long, state: GlobalStateEnum, message: Message, replyTo: Messages[Future]) extends TelegramMessage

case class FuelInfoByPeriod(uuid: Long, start: LocalDate, end: LocalDate, message: Message, replyTo: Messages[Future]) extends TelegramMessage
