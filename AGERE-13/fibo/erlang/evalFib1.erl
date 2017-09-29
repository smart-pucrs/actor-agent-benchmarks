-module(evalFib1).
-export([main/0, client/3, fibonaccer/0, manager/1]).

-define(NAG, 100).
-define(NASK, 30).
-define(MAXITH, 38).
-define(FILENAME, "data-erlang1.csv").

spawn_many(F,M) -> spawn_many(1,F,M).

spawn_many(?NAG,F,M) -> C = spawn(evalFib1, client, [F,?NASK,M]),
                 C ! "ask";


spawn_many(A,F,M) -> C = spawn(evalFib1, client, [F,?NASK,M]),
                 C ! "ask",
                 spawn_many(A+1,F,M).

fibo(0) -> 1;
fibo(1) -> 1;
fibo(N) when N > 1 -> fibo(N-1) + fibo(N-2).	

get_timestamp() ->
		   {Mega,Sec,Micro} = erlang:now(),
		   ((Mega*1000000+Sec)*1000000+Micro) div 1000.		

 client(F,Quant,M) ->
   receive
     "ask" -> if 
		Quant > 0 ->    Start =  get_timestamp(),
				N = crypto:rand_uniform(1,?MAXITH),
				F ! {N,self(),Start},
				client(F,Quant-1,M);
	   	true ->   M ! "end"
	      end;
      {_,N,Start} -> Final =  get_timestamp(),
	   M ! {N,Final - Start},
	   self() ! "ask",
           client(F,Quant,M)
   end.

 fibonaccer() ->
   receive
	{N, Pid, Start} -> Pid ! {fibo(N), N, Start},
        fibonaccer()
   end.
 
  manager(Finished) ->
    receive
	"end" -> if
		   Finished < ?NAG ->
			 manager(Finished+1);
		   true -> erlang:halt()
                end;
        {N, Time} ->  {ok, FileId} = file:open(?FILENAME, [write, append]),
		io:fwrite(FileId, "~p,~p~n", [N,Time]), 
		file:close(FileId),
		manager(Finished)
    end.

main() ->
   file:delete(?FILENAME),  
   F = spawn(evalFib1, fibonaccer, []),
   M = spawn(evalFib1, manager, [1]),
   spawn_many(F,M).
