package io.github.viacheslavbondarchuk.db.repository

import io.github.viacheslavbondarchuk.db.provider.DatabaseProvider
import io.github.viacheslavbondarchuk.db.table.IndexedPostgresTable
import io.github.viacheslavbondarchuk.domain.IndexedEntity
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 11:14 PM
 * */
abstract class AbstractPostgresRepository[A <: IndexedEntity, E <: IndexedPostgresTable[A]](protected val tableQuery: TableQuery[E]) {
  protected[this] val database = DatabaseProvider.getDatabase()

  def findById(id: Long): Future[Option[A]] = database.run(tableQuery.filter(_.id() === id).result.headOption)

  def findAll(): Future[Seq[A]] = database.run(tableQuery.result)

  def removeById(id: Long): Future[Int] = database.run(tableQuery.filter(_.id() === id).delete)

  def save(entity: A): Future[Int] = database.run(tableQuery.insertOrUpdate(entity))

  def removeAll(): Future[Int] = database.run(tableQuery.delete)

  def createIfNotExist(): Future[Unit] = database.run(tableQuery.schema.createIfNotExists)

  def dropIfExists(): Future[Unit] = database.run(tableQuery.schema.dropIfExists)

}
