package com.tort.trade.model

import com.fasterxml.uuid.{Generators, EthernetAddress}

object UUIDGenerator {
  val nic = EthernetAddress.fromInterface
  val uuidGenerator = Generators.timeBasedGenerator(nic)

  def generate = uuidGenerator.generate
}
