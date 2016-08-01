package lab.actor

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify, ReceiveTimeout, Terminated}
import lab.util.RandomUtils

import scala.concurrent.duration._

class Greeter(path: String, totalMessages: Int) extends Actor with akka.actor.ActorLogging {
  import Greeter._

  context.setReceiveTimeout(3.seconds)

  def receive = {
    case Start =>
      sendIdentifyRequest()
    case ActorIdentity(`path`, Some(actor)) =>
      context.watch(actor)
      context.become(active(actor))
      sendGreetings(actor, totalMessages)
    case ActorIdentity(`path`, None) => log.warning(s"Remote actor not available: $path")
    case ReceiveTimeout => sendIdentifyRequest()
  }

  def active(actor: ActorRef): Receive = {
    case Message(message: String) =>
      log.info(s"Receiver says $message")

    case Done =>
      log.info(s"We are done greeting!")
      actor ! Shutdown

    case Terminated(`actor`) =>
      log.warning("Receiver terminated")
      context.system.terminate()
  }

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(path) ! Identify(path)
  }

  def sendGreetings(actor: ActorRef, remaining: Int) : Unit = {
    if (remaining == 0) {
      self ! Done
    } else {
      actor ! RandomUtils.randomMessage(greetings)
      sendGreetings(actor, remaining - 1)
    }
  }
}

object Greeter {
  case object Done
  case object Start
  case object Shutdown
  case class Message(message: String)

  val greetings = Array("Hi", "How are you", "Bye")
}
