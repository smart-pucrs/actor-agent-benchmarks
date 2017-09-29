import akka.actor._

object threadring11 {

  val system = ActorSystem("Threadring1")	
  val ring = for (i <- 1 to 500) yield system.actorOf(Props(new Thread(i)), name = ""+i)

  class Thread(label: Int) extends Actor {
    var next: String = ""
    if(label == 500) { next = "/user/1"
    }
    else { next = "/user/"+(label+1)
    }

    def receive = {
      case 0 => system.shutdown()
      case n: Int => context.system.actorSelection(next) ! n - 1
    }
  }
      
  def main(args : Array[String]): Unit =  {
    ring(0) ! 500000
  }

}
