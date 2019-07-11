# RouterConfiguration

![RmiConfiguration ](https://user-images.githubusercontent.com/47759383/61080960-d4f83d00-a42e-11e9-8982-48a03c2f20f0.png)

almost the same as the basic configuration of a router done in cisco

config t or configuration terminal

interfaces port1 port2 .....(->assgining the ports of the router)

interface port1 (configuring interface port1)

      ip address (neig@) hostname (neigh name) port (neighport)   (-> establish conx through port1)
      exit
      
router rip

       network (neig@) (neigh name)
       exit (-> initialiing the routing protocol with the entered networks)
