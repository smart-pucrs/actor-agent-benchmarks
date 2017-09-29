package osl.examples.benchmarks;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Threadring1 extends Actor {

	public static final int NUM_ACTORS = 500;
  
        int id;
	ActorName nextActor;
	
	public Threadring1()
	{
		this(0);
	}
	
	public Threadring1(Integer id)
	{
		this(id, null);
	}
	
	public Threadring1(Integer id, ActorName next)
	{
		this.id = id.intValue();
		this.nextActor = next;
	}
	
	@message
    public void boot() throws RemoteCodeException {
        int passes = 500000;
  	ActorName [] actors = new ActorName[NUM_ACTORS];
  	
		actors[NUM_ACTORS - 1] = create(Threadring1.class, NUM_ACTORS);
  	for (int i = NUM_ACTORS - 2; i >= 0; i--)
	    actors[i] = create(Threadring1.class, i+1, actors[i+1]);
  	call(actors[NUM_ACTORS - 1], "setNextActor", actors[0]);    

    sendByRef(actors[0], "passToken", passes);
  }

	@message
    public void setNextActor (ActorName next)
	{
		this.nextActor = next;
	}
	
	@message
    public void passToken(Integer passes) {
  	if (passes != 0)
  	{
  		System.exit(0);
  	}
  	else
  	{
    		sendByRef(nextActor, "passToken", passes-1);
        }
  }
}
