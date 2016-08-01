package lab.util

import java.nio.ByteBuffer

import org.scalatest._

class RandomUtilsSpec extends WordSpecLike with Matchers {
  "RandomUtils" should {
    "convert from byte arrays to Integers" in {
      val zero = ByteBuffer.allocate(4)
      RandomUtils.byteArrayToLeInt(zero.array()) should be(0)

      val one = ByteBuffer.allocate(4).put(0, 1)
      RandomUtils.byteArrayToLeInt(one.array()) should be(1)

      val two = ByteBuffer.allocate(4).put(0, 2)
      RandomUtils.byteArrayToLeInt(two.array()) should be(2)

      val powerOfTwo = ByteBuffer.allocate(4).put(1, 1)
      RandomUtils.byteArrayToLeInt(powerOfTwo.array()) should be(256)

      val maxValue = ByteBuffer.allocate(4).put(1, 127)
      maxValue
        .put(0, 127)
        .put(1, 127)
        .put(2, 127)
        .put(3, 127)

      RandomUtils.byteArrayToLeInt(maxValue.array()) should be(2139062143)
    }

    "randomly return messages" in {
      val messages = Set("a", "b", "c")

      def collectAll(count: Int, found: Set[String]): Boolean = {
        if (count == 0) {
          fail(s"Missed ${messages.diff(found)}, maybe limits are not set properly?")
        }

        if (found.intersect(messages).size != messages.size) {
          val next = RandomUtils.randomMessage(messages.toArray)
          collectAll(count - 1, found + next.message)
        } else {
          true
        }
      }

      collectAll(100, Set.empty) shouldBe true
    }
  }
}
