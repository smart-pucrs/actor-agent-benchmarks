/* version with sub actor */

import akka.actor._
import akka.util.Duration
import akka.util.duration._
import scala.util.Random
import java.io.BufferedWriter
import java.io.FileWriter


object EvalFib2{

sealed trait Message
case object msg extends Message
case class Res(value: Int) extends Message
case class Calc1(i: Int, client: ActorRef) extends Message
case class Calc2(i: Int, client: ActorRef) extends Message


  val system = ActorSystem("FiboSubActors")

  val fs = system.actorOf(Props[Fibonaccer], name = "fibonaccer")
  val manager = system.actorOf(Props(new Manager(nbAgs,"data-akka2.csv")), name = "manager")

  val nbAgs  = 100
  val nbAsks = 30
  val maxIth = 38	

class Manager(nbAgs:Int,fileName:String) extends Actor {
    val out = new BufferedWriter(new FileWriter(fileName))   
    var finished = 0
    def receive = {
          case "end" => 
            finished += 1
            if (finished == nbAgs) {
              out.close()
              system.shutdown()
            }
          case msg   => out.write(msg+"\n")
    }
}

class Client(maxIth:Int, nbAsks:Int, fs:ActorRef, manager:ActorRef) extends Actor {
    var qtdAsks = nbAsks; 
    var startTime: Long = 0;
    var ith = 0;
    val rand = new Random()
    def receive = {
      case "ask" =>
        if (qtdAsks > 0) {
            startTime = System.currentTimeMillis()
            ith = rand.nextInt(maxIth)+1
            fs ! Calc1(ith,self)
            qtdAsks -= 1
        } else {
          manager ! "end"
        }
      case f: Int => 
        manager ! ith+","+(System.currentTimeMillis() - startTime)
        self ! "ask"
    }
}

  class SubActor extends Actor {
    def fib(i: Int): Int = { if (i <= 2) 1 else fib(i-1) + fib(i-2)  }
    def receive = {
      case Calc2(i, client) => client ! fib(i); exit()
    }
  }
  
  class Fibonaccer extends Actor {
    def receive = {
      case Calc1(i, client) => 
        val sact = system.actorOf(Props[SubActor])
        sact ! Calc2(i, client)
    }
  }
  

  def main(args: Array[String]): Unit = {
      val clients = for (i <- 1 to nbAgs) yield system.actorOf(Props(new Client(maxIth,nbAsks,fs,manager)), name = "client"+i)
      clients.foreach(t => {
        t ! "ask"
     })
  }

}
