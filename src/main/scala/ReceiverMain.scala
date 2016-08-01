import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import lab.actor.Receiver

object ReceiverMain {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Sys", ConfigFactory.load("receiver"))
    system.actorOf(Props[Receiver], "receiver")
  }
}

