package io.github.viacheslavbondarchuk

import io.github.viacheslavbondarchuk.bot.TelegramBot
import io.github.viacheslavbondarchuk.utils.ConfigUtils

/**
 * author: viacheslavbondarchuk
 * date: 6/10/2022
 * time: 10:46 PM
 * */
object Main {

  def main(args: Array[String]): Unit = {
    TelegramBot(ConfigUtils.getTelegramToken).run()
  }

}
