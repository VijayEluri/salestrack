package org.tort.trade.service

import java.io.File

import scala.io.Source
import scalaz.Scalaz._
import scalaz.ValidationNel

trait DBConfigReader {

  def homeDir = new File(System.getProperty("user.home"))
  def cfgFile = new File(homeDir, "trade.cfg")

  def readConfig: ValidationNel[String, Config] = {
    val lines = Source.fromFile(cfgFile).getLines().toList
    def url = lines.find(line => line.startsWith("url=")).map(_.split('=')(1)).toSuccess("IP not found in config").toValidationNel
    def ip = lines.find(line => line.startsWith("ip=")).map(_.split('=')(1)).toSuccess("URL not found in config").toValidationNel
    (ip |@| url) {Config(_, _)}
  }
}
