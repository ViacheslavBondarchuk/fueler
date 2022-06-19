package io.github.viacheslavbondarchuk.domain

import scala.util.matching.Regex

/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 4:42 PM
 * */
object FuelSort extends Enumeration {
  type FuelSort = Value

  val GAS = Value("Газ")
  val A98 = Value("A98")
  val A95 = Value("A95")
  val A92 = Value("A92")
  val DP = Value("ДП")

  private val mappings: Map[String, FuelSort] = values.map(x => (x.toString, x)).toMap

  def getFuelSortByName(name: String): FuelSort = mappings.getOrElse(name, A98)

  def toRegex: Regex = values.map(_.toString)
    .reduceLeft[String]((c, fs) => c.concat(s"|$fs"))
    .r
}
