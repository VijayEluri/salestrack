package com.tort.trade.model

import com.fasterxml.uuid.{Generators, EthernetAddress}

object UUIDGenerator {
  private val nic = EthernetAddress.fromInterface
  private val uuidGenerator = Generators.timeBasedGenerator(nic)

  def generate = uuidGenerator.generate
}
