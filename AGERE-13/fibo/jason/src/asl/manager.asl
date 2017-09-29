
nbClients(0).

!id_clients.

+!id_clients 
   <- .all_names(Agents);
      for (.member(A,Agents)) {
          if (.substring("client",A)) {
             ?nbClients(NB);
             -+nbClients(NB+1); 
          }   
      }.

+finish 
   :  .findall(A,finish[source(A)],L) & nbClients(NB) & //.print("finished ",L,"/",NB) & 
      .length(L) = NB
   <- for (elapse_time(A,_,N,T)) {
         stat.write_csv(N,T); 
      }
      .stopMAS.
      