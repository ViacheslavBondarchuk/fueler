package io.github.viacheslavbondarchuk.utils

/**
 * author: viacheslavbondarchuk
 * date: 6/11/2022
 * time: 5:43 AM
 * */
object ResponseUtils {

  def createUserTelegramMessageResponse(firstname: String): String = {
    s"""Привіт, $firstname
       |
       | Я Fueler, твій помічник у розрахунках витрат на паливо.
       | Допоможу тобі заощадити. Маю досить цікаві функції для тебе.
       |
       | Щоб розпочати роботу я маю уточнити деякі дані,
       | у тебе це не займе багато часу.
       | Аби розпочати тицяй сюди -> /init""".stripMargin
  }

  def createWrongTelegramMessageResponse(text: String): String = {
    s"""Ooopppsss!!! Something wrong!!
       |Unknown command: $text""".stripMargin
  }

  def createNonInitializedMessageResponse(): String = "Пройдіт етап ініціалізації /init"

  def createFuelSortMessageResponse(): String = ""

}
