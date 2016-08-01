package lab.actor

import akka.actor.Actor
import lab.actor.Greeter.Message
import lab.util.RandomUtils

class Receiver extends Actor with akka.actor.ActorLogging {
  import Greeter._
  import Receiver.replies

  def receive = {
    case Message(message: String) =>
      log.info(s"Got greeting $message")
      val replyOptions: Option[Array[String]] = replies.get(message)

      replyOptions match {
        case Some(ro) =>
          val reply = RandomUtils.randomMessage(ro)
          sender() ! reply
        case _ =>
          log.warning(s"Got unkown message $message")
          sender() ! Receiver.unknownMessageReply
      }
    case Shutdown =>
      log.info(s"We are done receiving!")
      context.system.terminate()
  }
}

object Receiver {
  val unknownMessageReply = Message("That doesn't make any sense to me")

  val replies = Map(
    "Hi" -> Array("Yo, what's up", "hey you again!"),
    "How are you" -> Array("I don't know I'm a machine", "Kinda electric tbh"),
    "Bye" -> Array("Cya!", "Great talking to you"))
}