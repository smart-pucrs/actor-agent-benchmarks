// Agent sample_agent in project eval_fib

/* Initial beliefs and rules */

/* Initial goals */

!run(40).

/* Plans */

+!run(0) <- 
  .send(manager,tell,finish).
+!run(S) <- 
  .my_name(Me);
  N = math.round(math.random(17)+1);
  Start = system.time;
  .send(fibonacher,askOne,fib(N,_),fib(_,F)); 
  .send(manager,tell,elapse_time(Me,S,N,system.time-Start));
  !run(S-1).
