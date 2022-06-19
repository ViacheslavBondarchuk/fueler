package io.github.viacheslavbondarchuk.utils

import com.typesafe.config.{Config, ConfigFactory}

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 1:41 PM
 * */
object ConfigUtils {
  private lazy val config: Config = ConfigFactory.parseResources("application.conf")
    .getConfig("conf")
    .resolve()

  def getConfig: Config = config

  def getTelegramToken: String = config.getString("telegram.token")

}
