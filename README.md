# ADOPTED
Distributed extension of the ADOPT system.

Running code on a single machine can be bottlenecked by the performance of a single machine; therefore, it makes sense to be able to run the code on a distributed architecture across multiple machines.
In this project, there is a Leader that distributes work to the Worker nodes. The data is sent between nodes over sockets as serialized data. The workers run code from ADOPT using the LeapFrog TrieJoin (LFTJ) algorithm. Different variables such as time budget of LFTJ can be changed to experiment with the trade-off between networking overhead and time budget. The workers send back their LFTJ results to the leader and the leader accumulates the results of the queries.
___

## Architecture Diagram
![image (1)](https://github.com/MitchellGray100/ADOPTED/assets/67762738/4c8deeef-5253-4ea5-9ef8-21bdb4839f6d)
