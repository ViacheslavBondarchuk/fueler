package io.github.viacheslavbondarchuk.db.table

import io.github.viacheslavbondarchuk.domain.IndexedEntity
import slick.jdbc.PostgresProfile.Table
import slick.jdbc.PostgresProfile.api._

/**
 * author: viacheslavbondarchuk
 * date: 6/13/2022
 * time: 4:47 PM
 * */
abstract class IndexedPostgresTable[A <: IndexedEntity](tag: Tag, tableName: String) extends Table[A](tag, Some("public"), tableName) {

  def id(): Rep[Long]

}
