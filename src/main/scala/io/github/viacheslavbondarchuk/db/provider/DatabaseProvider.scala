package io.github.viacheslavbondarchuk.db.provider

import io.github.viacheslavbondarchuk.utils.ConfigUtils
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, PostgresProfile}

/**
 * author: viacheslavbondarchuk
 * date: 6/13/2022
 * time: 2:02 PM
 * */
object DatabaseProvider {
  private[this] lazy val database = DatabaseConfig.forConfig[PostgresProfile]("postgres", ConfigUtils.getConfig).db

  def getDatabase(): JdbcBackend#DatabaseDef = database
}
