import akka.actor._

object threadring21 {

  val system = ActorSystem("Threadring2")
  val ring = for (i <- 1 to 500) yield system.actorOf(Props(new Thread(i)), name = ""+i)
  val counter = system.actorOf(Props[Counting])

  class Counting extends Actor {
    var count: Int = 1
    def receive = {
       case 1 if count == 50 => system.shutdown()
       case 1 => count += 1
    }
  }

  class Thread(label: Int) extends Actor {
    var next: String = ""
    if(label == 500) { next = "/user/1"
    }
    else { next = "/user/"+(label+1)
    }

    def receive = {
      case 0 => counter ! 1
      case n: Int => context.system.actorSelection(next) ! n - 1
    }
  }

  def main(args : Array[String]): Unit =  {
    for(i <- 1 to 50){
    	ring(scala.math.round(i*(500/50)-1)) ! 500000
    }
  }

}
