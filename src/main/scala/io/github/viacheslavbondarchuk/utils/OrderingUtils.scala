package io.github.viacheslavbondarchuk.utils

import com.bot4s.telegram.models.KeyboardButton

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 6:53 PM
 * */
object OrderingUtils {
  implicit val inlineKeyboardButton: Ordering[KeyboardButton] = Ordering.by(_.text)

}
