package io.github.viacheslavbondarchuk.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.bot4s.telegram.models
import com.bot4s.telegram.models.{KeyboardButton, ReplyKeyboardMarkup}
import io.github.viacheslavbondarchuk.db.repository.PostgresRepositories.{CarInfoRepository, FuelInfoRepository, GlobalStateRepository, UserRepository}
import io.github.viacheslavbondarchuk.domain.GlobalStateEnum.GlobalStateEnum
import io.github.viacheslavbondarchuk.domain._
import io.github.viacheslavbondarchuk.holder.{DataHolder, InternalStateHolder}
import io.github.viacheslavbondarchuk.utils.OrderingUtils._
import io.github.viacheslavbondarchuk.utils.{LoggingUtils, ResponseUtils}
import org.slf4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * author: viacheslavbondarchuk
 * date: 6/11/2022
 * time: 12:53 AM
 * */
class TelegramMessageActor(actorContext: ActorContext[TelegramMessage]) extends AbstractBehavior[TelegramMessage](actorContext) {
  private val globalStateRepository: GlobalStateRepository = GlobalStateRepository()
  private val fuelInfoRepository: FuelInfoRepository = FuelInfoRepository()
  private val carInfoRepository: CarInfoRepository = CarInfoRepository()
  private val userRepository: UserRepository = UserRepository()
  private implicit val logger: Logger = actorContext.log

  private def updateUserPartialFunction(user: models.User, state: String): PartialFunction[Option[(User, GlobalState)], Unit] = {
    case Some(value) =>
      userRepository.save(value._1.copy(username = user.username.get, firstname = user.firstName, lastname = user.lastName.get))
      globalStateRepository.save(value._2.copy(state = state))
    case None =>
      userRepository.save(User(0, user.id, user.username.get, user.firstName, user.lastName.get))
      globalStateRepository.save(GlobalState(0, user.id, state))
  }

  private def updateCarInfoPartialFunction(carInfo: CarInfo): PartialFunction[Option[CarInfo], Unit] = {
    case Some(value) => carInfoRepository.save(value.copy(mark = carInfo.mark, model = carInfo.model, consumption = carInfo.consumption, odometer = carInfo.odometer, fuelSort = carInfo.fuelSort))
    case None => carInfoRepository.save(carInfo)
  }

  private def updateGlobalStatePartialFunction(uuid: Long, state: GlobalStateEnum): PartialFunction[Option[GlobalState], Unit] = {
    case Some(value) => globalStateRepository.save(value.copy(state = state))
    case None => globalStateRepository.save(GlobalState(0, uuid, state))
  }

  private def handleCreateUserMessage(cum: CreateUserMessage): Behavior[TelegramMessage] = {
    userRepository.findByUuidInnerJoin(cum.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateUserPartialFunction(cum.user, cum.state)
        .andThen(_ => cum.replyTo.reply(ResponseUtils.createUserTelegramMessageResponse(cum.user.firstName))(cum.message))))
    Behaviors.same
  }

  private def handleInitUserMessage(ium: InitUserMessage): Behavior[TelegramMessage] = {
    globalStateRepository.findByUuid(ium.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(ium.uuid, ium.state)
        .andThen(_ => ium.replyTo.reply("Яка у Вас марка авто?")(ium.message))))
    Behaviors.same
  }

  private def handleUpdateMarkMessage(umm: UpdateMarkMessage): Behavior[TelegramMessage] = {
    DataHolder.updateMark(umm.uuid, umm.mark)
    InternalStateHolder.updateInternalState(umm.uuid, InternalStateEnum.MODEL)
    umm.replyTo.reply("Яка у Вас модель авто?")(umm.message)
    Behaviors.same
  }

  private def handleUpdateModelMessage(umm: UpdateModelMessage): Behavior[TelegramMessage] = {
    DataHolder.updateModel(umm.uuid, umm.model)
    InternalStateHolder.updateInternalState(umm.uuid, InternalStateEnum.CONSUMPTION)
    umm.replyTo.reply("Введіть витрати на 100 км?\r\nНаприклад 8.0 ")(umm.message)
    Behaviors.same
  }

  def handleUpdateConsumptionMessage(ucm: UpdateConsumptionMessage): Behavior[TelegramMessage] = {
    DataHolder.updateConsumption(ucm.uuid, ucm.consumption)
    InternalStateHolder.updateInternalState(ucm.uuid, InternalStateEnum.ODOMETER)
    ucm.replyTo.reply("Введіть показник одометра")(ucm.message)
    Behaviors.same
  }

  private def handleUpdateOdometerMessage(uom: UpdateOdometerMessage): Behavior[TelegramMessage] = {
    DataHolder.updateOdometer(uom.uuid, uom.odometer)
    InternalStateHolder.updateInternalState(uom.uuid, InternalStateEnum.FUEL_SORT)
    uom.replyTo.reply("Оберіть сорт палива у меню", replyMarkup = Option(
      ReplyKeyboardMarkup.singleColumn(
        FuelSort.values.map(KeyboardButton text _.toString).toList
      )))(uom.message)
    Behaviors.same
  }

  private def handleAddFuelMessage(afm: AddFuelMessage): Behavior[TelegramMessage] = {
    globalStateRepository.findByUuid(afm.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(afm.uuid, afm.state)
        .andThen(_ => InternalStateHolder.updateInternalState(afm.uuid, InternalStateEnum.FUEL_QUANTITY))
        .andThen(_ => afm.replyTo.reply("Введіть кількість літрів?")(afm.message))
      ))
    Behaviors.same
  }

  private def handleUpdateFuelSortMessage(ufsm: UpdateFuelSortMessage): Behavior[TelegramMessage] = {
    DataHolder.updateFuelSort(ufsm.uuid, ufsm.fuelSort)
    InternalStateHolder.updateInternalState(ufsm.uuid, InternalStateEnum.NON_SPECIFY)
    globalStateRepository.findByUuid(ufsm.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(ufsm.uuid, GlobalStateEnum.NON_SPECIFY)
        .andThen(_ => carInfoRepository.findByUuid(ufsm.uuid)
          .onComplete(_.fold(LoggingUtils.logError, updateCarInfoPartialFunction(DataHolder.getDataByUuid[CarInfo](ufsm.uuid)))))
        .andThen(_ => InternalStateHolder.updateInternalState(ufsm.uuid, InternalStateEnum.NON_SPECIFY))
        .andThen(_ => ufsm.replyTo.reply("Оберіть дію у меню", replyMarkup = Option(ReplyKeyboardMarkup.singleRow(OperationEnum.toKeyboardButtons)))(ufsm.message))
      ))
    Behaviors.same
  }

  def handleUpdateFuelQuantityMessage(ufq: UpdateFuelQuantityMessage): Behavior[TelegramMessage] = {
    DataHolder.updateFuelQuantity(ufq.uuid, ufq.quantity)
    InternalStateHolder.updateInternalState(ufq.uuid, InternalStateEnum.PRICE)
    ufq.replyTo.reply("Введіть вартість")(ufq.message)
    Behaviors.same
  }

  def handleUpdateFuelPriceMessage(ufp: UpdateFuelPriceMessage): Behavior[TelegramMessage] = {
    DataHolder.updateFuelPrice(ufp.uuid, ufp.price)
    InternalStateHolder.updateInternalState(ufp.uuid, InternalStateEnum.FUEL_ODOMETER)
    ufp.replyTo.reply("Введіть показник одометра")(ufp.message)
    Behaviors.same
  }

  def handleUpdateFuelOdometerMessage(ufo: UpdateFuelOdometerMessage): Behavior[TelegramMessage] = {
    DataHolder.updateFuelOdometer(ufo.uuid, ufo.odometer)
    globalStateRepository.findByUuid(ufo.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(ufo.uuid, GlobalStateEnum.NON_SPECIFY)
        .andThen(_ => fuelInfoRepository.save(DataHolder.getDataByUuid[FuelInfo](ufo.uuid)))
        .andThen(_ => InternalStateHolder.updateInternalState(ufo.uuid, InternalStateEnum.NON_SPECIFY))
        .andThen(_ => ufo.replyTo.reply("Дані збережено")(ufo.message))))
    Behaviors.same
  }

  def handleShowCostsByPeriodMessage(scbp: ShowCostsByPeriodMessage): Behavior[TelegramMessage] = {
    globalStateRepository.findByUuid(scbp.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(scbp.uuid, scbp.state)
        .andThen(_ => InternalStateHolder.updateInternalState(scbp.uuid, InternalStateEnum.FUEL_INFO_BY_PERIOD))
        .andThen(_ => scbp.replyTo.reply("Введіть пероід у форматі:\r\nз 01.02.2005 по 02.02.2005?")(scbp.message))
      ))
    Behaviors.same
  }

  def handleFuelInfoByPeriodMessage(fibp: FuelInfoByPeriod): Behavior[TelegramMessage] = {
    globalStateRepository.findByUuid(fibp.uuid)
      .onComplete(_.fold(LoggingUtils.logError, updateGlobalStatePartialFunction(fibp.uuid, GlobalStateEnum.NON_SPECIFY)
        .andThen(_ => InternalStateHolder.updateInternalState(fibp.uuid, InternalStateEnum.NON_SPECIFY))
        .andThen(_ =>
          fuelInfoRepository.findByUuidAndDate(fibp.uuid, fibp.start, fibp.end)
            .map(_.map(_.price))
            .map(_.foldLeft(0D)(_ + _))
            .onComplete(_.fold(LoggingUtils.logError, sum => fibp.replyTo.reply(s"Витрати за період з ${fibp.start} по ${fibp.end} становлят $sum грн")(fibp.message)))
        )))
    Behaviors.same
  }

  override def onMessage(message: TelegramMessage): Behavior[TelegramMessage] = message match {
    case cum: CreateUserMessage => handleCreateUserMessage(cum)
    case ium: InitUserMessage => handleInitUserMessage(ium)
    case umm: UpdateMarkMessage => handleUpdateMarkMessage(umm)
    case umm: UpdateModelMessage => handleUpdateModelMessage(umm)
    case ucm: UpdateConsumptionMessage => handleUpdateConsumptionMessage(ucm)
    case uom: UpdateOdometerMessage => handleUpdateOdometerMessage(uom)
    case ufsm: UpdateFuelSortMessage => handleUpdateFuelSortMessage(ufsm)
    case afm: AddFuelMessage => handleAddFuelMessage(afm)
    case ufq: UpdateFuelQuantityMessage => handleUpdateFuelQuantityMessage(ufq)
    case ufp: UpdateFuelPriceMessage => handleUpdateFuelPriceMessage(ufp)
    case ufo: UpdateFuelOdometerMessage => handleUpdateFuelOdometerMessage(ufo)
    case scbp: ShowCostsByPeriodMessage => handleShowCostsByPeriodMessage(scbp)
    case fibp: FuelInfoByPeriod => handleFuelInfoByPeriodMessage(fibp)
    case WrongMessage(text, message, replyTo) =>
      replyTo.reply(text)(message)
      Behaviors.same
  }
}
