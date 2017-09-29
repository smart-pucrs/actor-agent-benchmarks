import akka.dispatch.Await
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import akka.util.duration._

object chameneos {

  sealed trait Message
  case class Hook(creature: ActorRef, c: Color.Value) extends Message
  case class Done(hooks: Int) extends Message

  case object Color extends Enumeration {  val Blue, Red, Yellow = Value }

  val system = ActorSystem("Chameneos")

    // the input sets of creatures defined by the benchmark
    val firstCreatures = List(Color.Blue, Color.Red, Color.Yellow)
    val secondCreatures = List(Color.Blue, Color.Red, Color.Yellow, Color.Red,
            Color.Yellow, Color.Blue, Color.Red, Color.Yellow, Color.Red, Color.Blue)

    def complement(a: Color.Value, b: Color.Value) : Color.Value = {
        // the complement of two identical colors is defined to be that color
        if (a == b) a
        else if (a > b) complement(b, a)
        else {
            Pair(a, b) match {
                case Pair(Color.Blue, Color.Red) => Color.Yellow
                case Pair(Color.Blue, Color.Yellow) => Color.Red
                case Pair(Color.Red, Color.Yellow) => Color.Blue
                }
        }
    }

    def spellNumber(n : Int) : String = {
            val numbers = Array("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            def spellDigits(m : Int) : String = {
                if (m < 10) {
                    numbers(m % 10)
               } else {
                    spellDigits(m / 10) + " " + numbers(m % 10)
                }
            }
            spellDigits(n)
        }

    class Broker(nMeetings: Int, creatures: List[Color.Value]) extends Actor {
	var totalRendezvous:Int = nMeetings
	var firstHook, nextRun:ActorRef = null
	var firstColor:Color.Value = null
	var total, count:Int = 0
	def receive = {
		case "run" =>  creatures foreach{ c => system.actorOf(Props(new Chameneos(c,self)))}
			       nextRun = sender
					    
		case Hook(creature, c) => if (totalRendezvous == 0)
					  {
						creature ! "stop"
						
					  }
					  else if (firstHook == null)
					  {
						firstHook = creature
						firstColor = c
					  }
					  else
			   		  {
						firstHook ! Hook(creature, c);
						creature ! Hook(firstHook, firstColor)
						firstHook = null
						totalRendezvous -= 1
		                          }

	       	case Done(hooks) => total += hooks
				    count += 1
				    if (creatures.length == count) {
					    println(spellNumber(total))
					    nextRun ! "done"
					    context.stop(self)
				    }				    
	}
    }

    class Chameneos(c : Color.Value, broker: ActorRef) extends Actor {	
	var myHooks, selfHooks: Int = 0
	var myColor:Color.Value = c
	broker ! Hook(self, myColor)
	def receive = {
		case Hook(creature, color) => myColor = complement(myColor, color)
					      if (self == creature)
						   selfHooks += 1
					      myHooks += 1
					      broker ! Hook(self, myColor)
		case "stop" => println(myHooks+" "+spellNumber(selfHooks))
			       broker ! Done(myHooks)
			       context.stop(self)
	}
    }

    def showColor(c : Color.Value) = {
        c match {
            case Color.Red => "red"
            case Color.Yellow => "yellow"
            case Color.Blue => "blue"
    }}

    def showComplements() {
        def showComplement(a : Color.Value, b : Color.Value) {
            println(showColor(a)  + " + "  + showColor(b) + " -> " + showColor(complement(a, b)))
        }
        Color.values foreach{ a =>
            Color.values foreach{ b =>
                showComplement(a, b) }}
        println("")
    }

  def main(args : Array[String]): Unit =  {   
    showComplements
    val nMeetings = 60000
    var broker = system.actorOf(Props(new Broker(nMeetings, firstCreatures)), name ="broker")
    println ((firstCreatures map showColor) mkString(" ", " ", ""))
    println("")
    implicit val timeout = Timeout(10000 seconds)
    val run1 = broker ? "run"
    Await.ready(run1, timeout.duration)
    println("")
    println ((secondCreatures map showColor) mkString(" ", " ", ""))
    broker = system.actorOf(Props(new Broker(nMeetings, secondCreatures)), name ="broker")
    val run2 = broker ? "run"
    Await.ready(run2, timeout.duration)
    system.shutdown()
  }

}
