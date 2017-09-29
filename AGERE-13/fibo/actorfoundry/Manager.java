package osl.examples.fibonacci;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager extends Actor {

  private int nclients;
  private int count = 0;
  private String fileName;
  private BufferedWriter out;

  public Manager() {
  }

  public Manager(Integer theClient, String fileN) {
    nclients = theClient;
    fileName   = fileN;
     try { out = new BufferedWriter(new FileWriter(fileName)); }
     catch(IOException e) { }
  }

  @message
  public void writeF(String msg) {
       try { out.write(msg+"\n"); }
       catch(IOException e) { }
  }

  @message
  public void end() {
      count += 1;
      if (count == nclients) {
	  try { out.close(); }
	  catch(IOException e) { }
	  System.exit(0);
      }
  }
}
