package osl.examples.benchmarks;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Threadring2 extends Actor {

	public static final int NUM_ACTORS = 500;
        int id;
	ActorName nextActor;
	ActorName counterActor;
	
	public Threadring2()
	{
		this(0);
	}
	
	public Threadring2(Integer id)
	{
		this(id, null, null);
	}
	
	public Threadring2(Integer id, ActorName next, ActorName counter)
	{
		this.id = id.intValue();
		this.nextActor = next;
		this.counterActor = counter;
	}
	
	@message
    public void boot() throws RemoteCodeException {
        int passes = 500000;
  	ActorName [] actors = new ActorName[NUM_ACTORS];
        ActorName counter = null;
        counter = create(Counter.class);
	actors[NUM_ACTORS - 1] = create(Threadring2.class, NUM_ACTORS);
  	for (int i = NUM_ACTORS - 2; i >= 0; i--)
	    actors[i] = create(Threadring2.class, i+1, actors[i+1], counter);
  	call(actors[NUM_ACTORS - 1], "setNextActor", actors[0], counter); 
        for (int i = 1; i <= 50; i++)
	    sendByRef(actors[Math.round(i*(NUM_ACTORS/50)-1)], "passToken", passes);
        
        
  }

	@message
    public void setNextActor (ActorName next, ActorName counter)
	{
		this.nextActor = next;
		this.counterActor = counter;
	}
	
	@message
    public void passToken(Integer passes) {
  	if (passes == 0)
  	{
  		sendByRef(counterActor, "increment");
  	}
  	else
  	{
    	        sendByRef(nextActor, "passToken", passes-1);
        }
  }
}
