import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import lab.actor.Greeter
import lab.actor.Greeter.Start

object GreeterMain {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Sys", ConfigFactory.load("greeter"))

    val remotePath = s"akka.tcp://Sys@127.0.0.1:2553/user/receiver"
    val totalMessages = if (args.length >= 2) args(0).toInt else 10

    system.actorOf(Props(new Greeter(remotePath, totalMessages)), "greeter") ! Start
  }
}

