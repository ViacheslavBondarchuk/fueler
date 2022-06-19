package io.github.viacheslavbondarchuk.validator

import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import io.github.viacheslavbondarchuk.domain.{FuelSort, Invalid, Valid, ValidationResult}

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.matching.Regex

/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 5:33 PM
 * */
object TelegramMessageValidator {
  private lazy val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  private lazy val markRegex: Regex = "([a-zA-Z]*)".r
  private lazy val modelRegex: Regex = "([a-zA-Z]*\\s?-?[0-9]?)".r
  private lazy val doubleRegex: Regex = "(\\d*\\.\\d)".r
  private lazy val odometerRegex: Regex = "(\\d*)".r
  private lazy val fuelSortRegex: Regex = FuelSort.toRegex
  private lazy val localDateTimeRegex: Regex = "([0-9]{2}\\.[0-9]{2}\\.[0-9]{4})-(([0-9]{2}\\.[0-9]{2}\\.[0-9]{4}))".r

  def validateConsumption(text: String): ValidationResult[Double] = if (doubleRegex.matches(text)) Valid(text.toDouble) else Invalid()

  def validateMark(text: String): ValidationResult[String] = if (markRegex.matches(text)) Valid(text) else Invalid()

  def validateModel(text: String): ValidationResult[String] = if (modelRegex.matches(text)) Valid(text) else Invalid()

  def validateOdometer(text: String): ValidationResult[Long] = if (odometerRegex.matches(text)) Valid(text.toLong) else Invalid()

  def validateFuelSort(text: String): ValidationResult[FuelSort] = if (fuelSortRegex.matches(text)) Valid(FuelSort.getFuelSortByName(text)) else Invalid()

  def validateFuelQuantity(text: String): ValidationResult[Double] = if (doubleRegex.matches(text)) Valid(text.toDouble) else Invalid()

  def validateFuelPrice(text: String): ValidationResult[Double] = if (doubleRegex.matches(text)) Valid(text.toDouble) else Invalid()

  def validateDatePeriod(text: String): ValidationResult[(LocalDate, LocalDate)] = {
    val dateStr: String = text.replaceAll("з|по|\\s", "")
    if (localDateTimeRegex.matches(dateStr)) {
      val strings: Array[String] = dateStr.split("-")
      Valid(LocalDate.parse(strings(0), dateTimePattern), LocalDate.parse(strings(1), dateTimePattern))
    } else {
      Invalid()
    }
  }

}
