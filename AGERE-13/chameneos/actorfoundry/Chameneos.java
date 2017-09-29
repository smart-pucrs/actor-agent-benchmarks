package osl.examples.benchmarks;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Chameneos extends Actor {

	public enum Colour {
        blue,
        red,
        yellow
    }

    public static Colour doCompliment(Colour c1, Colour c2) {
        switch (c1) {
        case blue:
            switch (c2) {
            case blue:
                return Colour.blue;
            case red:
                return Colour.yellow;
            case yellow:
                return Colour.red;
            }
        case red:
            switch (c2) {
            case blue:
                return Colour.yellow;
            case red:
                return Colour.red;
            case yellow:
                return Colour.blue;
            }
        case yellow:
            switch (c2) {
            case blue:
                return Colour.red;
            case red:
                return Colour.blue;
            case yellow:
                return Colour.yellow;
            }
        }

        throw new RuntimeException("Error");
    }

    public static void printColours() {
        printColours(Colour.blue, Colour.blue);
        printColours(Colour.blue, Colour.red);
        printColours(Colour.blue, Colour.yellow);
        printColours(Colour.red, Colour.blue);
        printColours(Colour.red, Colour.red);
        printColours(Colour.red, Colour.yellow);
        printColours(Colour.yellow, Colour.blue);
        printColours(Colour.yellow, Colour.red);
        printColours(Colour.yellow, Colour.yellow);
    }

    public static void printColours(final Colour c1, final Colour c2) {
        System.out.println(c1 + " + " + c2 + " -> " + doCompliment(c1, c2));
    }

    public static final String[] NUMBERS = {
        "zero", "one", "two", "three", "four", "five",
        "six", "seven", "eight", "nine"
    };

    public static String getNumber(final int n) {
        final StringBuilder sb = new StringBuilder();
        final String nStr = String.valueOf(n);
        for (int i = 0; i < nStr.length(); i++) {
            sb.append(" ");
            sb.append(NUMBERS[Character.getNumericValue(nStr.charAt(i))]);
        }
        return sb.toString();
    }
  
  int myHooks, selfHooks;
  Colour myColor;
  
	ActorName broker;
	
	public Chameneos(ActorName b, Colour c)
	{
		broker = b;
		myColor = c;
	}
	
	@message
    public void start ()
	{
		sendByRef(broker, "hook", self(), myColor);
	}
	
	
	@message
    public void hook(ActorName other, Colour c) throws RemoteCodeException {
    myColor = doCompliment(myColor, c);

    if (self() == other)
    	selfHooks++;
    myHooks++;
    this.start();
    //send(self(), "start");
  }

	@message
    public void stop ()
	{
		sendByRef(stdout, "println", myHooks+""+getNumber(selfHooks));
		sendByRef(broker, "done", myHooks);
	}
}
