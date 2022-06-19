package io.github.viacheslavbondarchuk.domain

import com.bot4s.telegram.models.KeyboardButton
import io.github.viacheslavbondarchuk.constants.Common
import io.github.viacheslavbondarchuk.utils.OrderingUtils._

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 7:38 PM
 * */
object OperationEnum extends Enumeration {
  type OperationEnum = Value

  val ADD_FUEL = Value(Common.ADD_FUEL)
  val SHOW_COSTS_BETWEEN_PERIOD = Value(Common.SHOW_COSTS_BETWEEN_PERIOD)

  def toKeyboardButtons: Seq[KeyboardButton] = values.map(op => KeyboardButton.text(op.toString)).toList

}
