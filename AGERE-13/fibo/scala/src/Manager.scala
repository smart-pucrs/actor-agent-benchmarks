import scala.actors.Actor
import java.io.BufferedWriter
import java.io.FileWriter

class Manager(nbAgs:Int,fileName:String) extends Actor() {
  
    val out = new BufferedWriter(new FileWriter(fileName)) 
  
    var finished = 0
    
    def act() {
      while (true) {
        receive {
          case "end" => 
            finished += 1
            if (finished == nbAgs) {
              out.close()
              System.exit(0)
            }
            
          case msg   => out.write(msg+"\n")
        }
      }
    }
}