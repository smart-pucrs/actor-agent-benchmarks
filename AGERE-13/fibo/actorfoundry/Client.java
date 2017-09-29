package osl.examples.fibonacci;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;
import java.util.Random;

public class Client extends Actor {

  private int maxIth = 0;
  private int nbAsks = 0;
  private ActorName fs = null;
  private ActorName manager = null;
  private long startTime = 0;
  private int rand = 0;

  public Client() {
  }

  public Client(Integer mIth, Integer nAsks, ActorName fser, ActorName manag) {
    maxIth = mIth;
    nbAsks   = nAsks;
    fs = fser;
    manager = manag;
  }

  @message
  public void finished(Integer finalVal) {
    send(self(), "done", finalVal);
  }

  @message
  public void done(Integer res) {
       String msg = rand+","+(System.currentTimeMillis() - startTime);
       send(manager, "writeF", msg);   
       send(self(), "ask");
  }
    
  @message
  public void ask() {
    Random randomGenerator = new Random();
    rand = randomGenerator.nextInt(maxIth)+1;
    if (nbAsks > 0) {
	startTime = System.currentTimeMillis();
        send(fs, "fib", rand, self(), "finished");
        nbAsks -= 1;
    }
    else { send(manager, "end");   }
  }
}
