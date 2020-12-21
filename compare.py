#pip3 install hopcroftkarp
from hopcroftkarp import HopcroftKarp
import re
from datetime import datetime
import time 

f = open("log2.txt", "r")
graph = {}
#graph = {'a': {1}, 'b': {1, 2}, 'c': {1, 2}, 'd': {2, 3, 4}, 'e': {3, 4}, 'f': {4, 5, 6}, 'g': {5, 6, 7}, 'h': {8}}
# print(graph)
f.readline()
for line in f:
    string = re.findall('\d+', line)
    # string = line.split()
    left = int(string[0])
    right = int(string[1])
    if(left not in graph):
        graph[left] = set()
    graph[left].add(int(right))

# print(graph)
dttime = time.time()
print(len(HopcroftKarp(graph).maximum_matching())/2)
dttime = time.time()-dttime
print("the time is: " + str(dttime*1000))