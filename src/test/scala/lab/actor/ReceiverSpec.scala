package lab.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import lab.actor.Greeter.Message
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ReceiverSpec extends TestKit(ActorSystem("Receiver")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val receiver = system.actorOf(Props[Receiver])

  "Receiver" should {
    "send back random messages accordingly" in {
      for ((greet: String, replies: Array[String]) <- Receiver.replies) {
        receiver ! Message(greet)
        expectMsgAnyOf(replies.map(m => Message(m)): _*)
      }
    }

    "send unknown message reply if it got something unexpected" in {
      receiver ! Message("Donald T. is awesome")
      expectMsg(Receiver.unknownMessageReply)
    }

    "shut itself down as told" in {
      val receiverToShutdown = system.actorOf(Props[Receiver])
      val probe = TestProbe()
      probe watch receiverToShutdown
      receiverToShutdown ! Greeter.Shutdown
      probe.expectTerminated(receiverToShutdown)
    }
  }
}
