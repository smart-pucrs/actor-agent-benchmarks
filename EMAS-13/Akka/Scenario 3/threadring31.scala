import akka.actor._

object threadring31 {

sealed trait Message
case class Tok20(a: Int, n: Int) extends Message
case class Tok21(a: Int, n: Int) extends Message
case class Tok11(a: Int, n: Int) extends Message
case class Tok10(a: Int, n: Int, b: Int) extends Message

  val system = ActorSystem("Threadring3")
  val ring = for (i <- 1 to 500) yield system.actorOf(Props(new Thread(i)), name = ""+i)
  val counter = system.actorOf(Props[Counting])

  class Counting extends Actor {
    var count: Int = 1
    def receive = {
       case 1 if count == 50 => system.shutdown()
       case 1 => count += 1
    }
  }

  class SubActor(val n: Int, var father: ActorRef) extends Actor {
    def receive = {
       case 1 => for(i <- 1 to 1000){}; father ! Tok10(1,n,0); context.stop(self)
    }
  }

  class Thread(label: Int) extends Actor {
    var next: String = ""
    if(label == 500) { next = "/user/1"
    }
    else { next = "/user/"+(label+1)
    }

    def receive = {
      case Tok20(2,0) => counter ! 1
      case Tok21(2,n : Int) if n-1 == 0 => context.system.actorSelection(next) ! Tok20(2,n - 1)
      case Tok21(2,n : Int) => context.system.actorSelection(next) ! Tok21(2,n - 1)
      case Tok11(1,n : Int) => var sub = context.actorOf(Props(new SubActor(n, self)));  sub ! 1
      case Tok10(1,n : Int,0) => context.system.actorSelection(next) ! Tok11(1,n)
    }
  }

  def main(args : Array[String]): Unit =  {
    for(j <- 1 to 2){
	    for(k <- 0 to 499){
    		ring(k) ! Tok11(1,500)
	    }
    }
    for(j <- 1 to 50){
    	ring((j*(500/50))-1) ! Tok21(2,500)
  }
  }  

}
