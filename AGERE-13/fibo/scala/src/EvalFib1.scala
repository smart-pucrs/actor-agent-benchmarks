import scala.actors.Actor._
import scala.actors.Actor

object EvalFib1 {

  val nbAgs  = 100
  val nbAsks = 40
  val maxIth = 40
  
  class Fibonaccer() extends Actor {
    def fib(i: Int): Int = { if (i <= 2) 1 else fib(i-1) + fib(i-2)  }
    def act() { loop { react {
      case (i: Int, client: Actor) => client ! fib(i)
    }}}
  }
  
  val fs = new Fibonaccer()
  fs.start
  val manager = new Manager(nbAgs,"data-scala1.csv")
  manager.start
  
  def main(args: Array[String]): Unit = {
      val clients = Array.tabulate(nbAgs)(i => new Client(maxIth,nbAsks,fs,manager))
      clients.foreach(t => {
        t.start
        t ! "ask"
      })
  }
}