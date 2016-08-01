package lab.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import lab.actor.Greeter.{Done, Shutdown, Start}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class GreeterSpec extends TestKit(ActorSystem("Receiver")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val receiverProbe = TestProbe("receiver")
  val greeter = system.actorOf(Props(new Greeter(receiverProbe.ref.path.toString, 10)))

  "Greeter" should {
    "send as many messages as specified" in {
      greeter ! Start
      receiverProbe.receiveN(10)
    }

    "shutdown when done is received" in {
      greeter ! Done
      receiverProbe.expectMsg(Shutdown)
    }

    "terminate when the remote actor is terminated" in {
      val receiverThatTerminatedProbe = TestProbe("receiverThatTerminated")
      val greeterToTerminate = system.actorOf(Props(new Greeter(receiverThatTerminatedProbe.ref.path.toString, 100)))
      val probe = TestProbe()
      probe watch greeterToTerminate
      receiverThatTerminatedProbe.shutdown()
      probe.expectTerminated(greeterToTerminate)
    }
  }
}
