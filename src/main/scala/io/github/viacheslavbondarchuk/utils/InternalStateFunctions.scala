package io.github.viacheslavbondarchuk.utils

import com.bot4s.telegram.api.declarative.Messages
import com.bot4s.telegram.models.{Message, User}
import io.github.viacheslavbondarchuk.domain.InternalStateEnum.InternalStateEnum
import io.github.viacheslavbondarchuk.domain._
import io.github.viacheslavbondarchuk.validator.TelegramMessageValidator

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 12:12 PM
 * */
object InternalStateFunctions {
  private val functions: Map[InternalStateEnum, (User, Message, Messages[Future]) => TelegramMessage] = Map(
    InternalStateEnum.MARK -> markFunction,
    InternalStateEnum.MODEL -> modelFunction,
    InternalStateEnum.CONSUMPTION -> consumptionFunction,
    InternalStateEnum.ODOMETER -> odometerFunction,
    InternalStateEnum.FUEL_SORT -> fuelSortFunction,
    InternalStateEnum.FUEL_QUANTITY -> fuelQuantityFunction,
    InternalStateEnum.PRICE -> fuelPriceFunction,
    InternalStateEnum.FUEL_ODOMETER -> fuelOdometerFunction,
    InternalStateEnum.FUEL_INFO_BY_PERIOD -> fuelInfoByPeriodFunction
  )

  private def markFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateMark(message.text.get) match {
      case Valid(mark) => UpdateMarkMessage(user.id, mark, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть коректну марку автомобіля", message, sender)
    }
  }

  private def modelFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateModel(message.text.get) match {
      case Valid(model) => UpdateModelMessage(user.id, model, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть коректну модель автомобіля", message, sender)
    }
  }

  private def consumptionFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateConsumption(message.text.get) match {
      case Valid(consumption) => UpdateConsumptionMessage(user.id, consumption, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть витрати палива на 100 км у форматі: 8.0 ", message, sender)
    }
  }

  private def odometerFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateOdometer(message.text.get) match {
      case Valid(odometer) => UpdateOdometerMessage(user.id, odometer, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть тільки цифри", message, sender)
    }
  }

  private def fuelSortFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateFuelSort(message.text.get) match {
      case Valid(fuelSort) => UpdateFuelSortMessage(user.id, fuelSort, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, виберіть сорт палива з перерахованих", message, sender)
    }
  }

  private def fuelQuantityFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateFuelQuantity(message.text.get) match {
      case Valid(quantity) => UpdateFuelQuantityMessage(user.id, quantity, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть кількість палива у форматі: 235.3", message, sender)
    }
  }

  private def fuelPriceFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateFuelPrice(message.text.get) match {
      case Valid(price) => UpdateFuelPriceMessage(user.id, price, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть ціну у форматі: 22.1", message, sender)
    }
  }

  private def fuelOdometerFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateOdometer(message.text.get) match {
      case Valid(price) => UpdateFuelOdometerMessage(user.id, price, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть тільки цифри", message, sender)
    }
  }

  private def fuelInfoByPeriodFunction(user: User, message: Message, sender: Messages[Future]): TelegramMessage = {
    TelegramMessageValidator.validateDatePeriod(message.text.get) match {
      case Valid(period) => FuelInfoByPeriod(user.id, period._1, period._2, message, sender)
      case Invalid() => WrongMessage("Будь-ласка, введіть тільки цифри", message, sender)
    }
  }


  def getFunction(internalState: InternalStateEnum): (User, Message, Messages[Future]) => TelegramMessage = functions.getOrElse(internalState, markFunction)

}
