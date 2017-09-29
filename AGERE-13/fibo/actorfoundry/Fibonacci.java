package osl.examples.fibonacci;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Fibonacci extends Actor {

  public static final int NUM_CLIENTS = 100;
  public static final int NUM_ASKS = 40;
  public static final int MAX_ITH = 17;
  private int numResponses;
  private int partialResponse;
  private ActorName client;
  private String meth;

  public Fibonacci() {
  }

  public Fibonacci(ActorName theClient, String theMeth) {
    client = theClient;
    meth   = theMeth;
    numResponses = 0;
  }

  @message
  public void boot() throws SecurityException, RemoteCodeException {
    ActorName manager = create (Manager.class, NUM_CLIENTS, "data-af.csv");
    ActorName [] clients = new ActorName[NUM_CLIENTS];
    for (int i = 0; i < NUM_CLIENTS; i++) {
	 clients[i] = create(Client.class, MAX_ITH, NUM_ASKS, self(), manager);
    	 send(clients[i], "ask");
    }
  }

  @message
  public void fib(Integer val, ActorName client, String meth) {
    ActorName newChild = null;
    if (val == 0) {
      send(client, meth, 0);
    } else if (val < 3) {
      send(client, meth, 1);
    } else {
      try {
		newChild = create(Fibonacci.class, client, meth);
      } catch (RemoteCodeException e) {
	    }
      send(self(), "fib", val - 1, newChild, "result");
      send(self(), "fib", val - 2, newChild, "result");
    }
  }

  @message
  public void result(Integer val) {
    if (numResponses == 0) {
      numResponses++;
      partialResponse = val;
    } else {
      send(client, meth, val + partialResponse);
    }
  }
}
