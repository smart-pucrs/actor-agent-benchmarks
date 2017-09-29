/*
    This agent receives asks for meetings from chameneos,
	when pairs are formed, they are informed of their partners for
	the mutation.
	
	It also notifies "main" agent when a given number of meetings
	are achieved.
*/

@lm[atomic] // this plan is atomic because it changes I, I is the number of meetings left
+!meet(C2)[source(A2)] : first(A1,C1) & i(I) & I > 0
   <- -first(A1,C1);
      -+i(I-1);
      //.println("meet ",A1," and ",A2);
      .send(A1,achieve,mutate(A2,C2));
      .send(A2,achieve,mutate(A1,C1)).
          
+!meet(C1)[source(A1)] : not i(0) <- +first(A1,C1).

+!meet(_) <- .send(main,tell,run_finished).
   
+!stop <- -first(_,_).
   
+nb_meets(N) <- -+i(N); .abolish(nb_meets(_)). // receives the number of meetings and start the i counter

