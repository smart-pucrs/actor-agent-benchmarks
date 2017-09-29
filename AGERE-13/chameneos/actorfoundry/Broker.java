package osl.examples.benchmarks;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Broker extends Actor {

	public static final Chameneos.Colour[][] groups = new Chameneos.Colour[][] {
		{Chameneos.Colour.blue, Chameneos.Colour.red, Chameneos.Colour.yellow},
		{Chameneos.Colour.blue, Chameneos.Colour.red, Chameneos.Colour.yellow, Chameneos.Colour.red, Chameneos.Colour.yellow,
                Chameneos.Colour.blue, Chameneos.Colour.red, Chameneos.Colour.yellow, Chameneos.Colour.red, Chameneos.Colour.blue}
	};
  
 	int totalRendezvous;
 	int total = 0;
	int count = 0;
	int run = 0;
	ActorName firstHooker;
	Chameneos.Colour firstColor;
	
	@message
    public void boot() throws RemoteCodeException {
	totalRendezvous = 60000;
  	ActorName [] actors = new ActorName[groups[0].length];
	Chameneos.printColours();
	System.out.println();
	run = 1;  	
  	for (int i = 0; i < groups[0].length; i++)
  	{
	    sendByRef(stdout, "print", " "+groups[0][i]);
	    actors[i] = create(Chameneos.class, self(), groups[0][i]);
	    sendByRef(actors[i], "start");
	}
	System.out.println();
  }

  
	@message
    public void hook(ActorName other, Chameneos.Colour c) throws RemoteCodeException {
		if (totalRendezvous == 0)
		{
			sendByRef(other, "stop");
			return;
		}
			
		if (firstHooker == null)
		{
			firstHooker = other;
			firstColor = c;
		}
		else
		{
			sendByRef(firstHooker, "hook", other, c);
			sendByRef(other, "hook", firstHooker, firstColor);
			firstHooker = null;
			totalRendezvous--;
		}
  }

	@message
    public void done(Integer myHooks) throws RemoteCodeException {
		total += myHooks;
		count++;
		if ((count == groups[1].length) && (run == 2)) {
			sendByRef(stdout, "println", ""+Chameneos.getNumber(total));
			System.exit(0);
		}
		else if ((count == groups[0].length) && (run == 1)) {
			sendByRef(stdout, "println", ""+Chameneos.getNumber(total)+"\n");
			total = 0;
			count = 0;
			run = 2;
			totalRendezvous = 60000;
			firstHooker = null;
			ActorName [] actors2 = new ActorName[groups[1].length];
			for (int i = 0; i < groups[1].length; i++)
  			{
	  			sendByRef(stdout, "print", " "+groups[1][i]);
	    			actors2[i] = create(Chameneos.class, self(), groups[1][i]);
	    			sendByRef(actors2[i], "start");
			}
			System.out.println();
		}
  }

}
