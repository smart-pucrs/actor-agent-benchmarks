package osl.examples.benchmarks;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Counter extends Actor {

	public static int count = 1;
	
	@message
    public void increment() {
  	if (count == 50)
  	{
  		System.exit(0);
  	}
  	else
  	{
    	        count++;
        }
  }
}
