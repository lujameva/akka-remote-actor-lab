package lab.util

import java.nio.{ByteBuffer, ByteOrder}
import java.security.SecureRandom

import lab.actor.Greeter.Message

/**
  * Simple utility singleton that provides convenient random methods
  */
object RandomUtils {
  // Use of secure random preferred over plain scala.util.Random - due  lack of entropy of the later
  // There is a performance trade off, but guessing that's not a concern right now :)
  val randomGenerator = new SecureRandom()

  /**
    * Returns a random message from the given options array
    * @param options an array of options to get the message from
    * @return a Greeter.Message with a random value from the given options array
    */
  def randomMessage(options: Array[String]): Message = {
    val next = new Array[Byte](4)
    randomGenerator.nextBytes(next)

    val messageValue = options(Math.abs(byteArrayToLeInt(next) % options.length))
    Message(messageValue)
  }

  /**
    * Helper method that converts a byte to a Little Endian int
    * @param bytes the source byte array
    * @return a Little Endian int represented in the bytes array
    */
  def byteArrayToLeInt(bytes: Array[Byte]) : Int = {
    val byteBuffer: ByteBuffer = ByteBuffer.wrap(bytes)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.getInt()
  }
}
