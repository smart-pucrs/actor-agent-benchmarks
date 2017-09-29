import scala.actors.Actor
import scala.util.Random

class Client(maxIth:Int, nbAsks:Int, fs:Actor, manager:Actor) extends Actor {
    var qtdAsks = nbAsks; 
    var startTime: Long = 0;
    var ith = 0;
    val rand = new Random()
    def act() { loop { react {
      case "ask" =>
        if (qtdAsks > 0) {
            startTime = System.currentTimeMillis()
            ith = rand.nextInt(maxIth)+1
            fs ! ((ith,this))
            qtdAsks -= 1
        } else {
          manager ! "end"
        }
      case f: Int => 
        manager ! ith+","+(System.currentTimeMillis() - startTime) //(ith+" = "+f+" in "+elapTime)
        this ! "ask"
    }}}
  
}