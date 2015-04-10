Things that make sense:

List
Set
Map

Things that don't make as much sense:

OrderedSet
Map from unique A to unique B

It sure feels like the problem is that everything is a map.

This is something like the question of what you can structure in SQL. The difference is that SQL has these restrictions as a direct result of its implementation, which is not a problem I have here.

The reason this is unpleasant is that if you want to have an OrderedSet, you can't insert an item and then assume that it's where you put it, because it might already exist. Likewise, if you have a map from unique A to unique B {1:'a', 2:'b'} and then you try to set 1 to point to 'b', it's not obvious what should happen.

Maybe things are nicer when you have 0 or 1 unique fields.

List = map from Index to Item
Map = map from ItemA to ItemB
Set = map from ItemA to Unit
Multiset = collection of Item
OrderedMap = ugly....
PriorityQueue = map from ItemA to Priority

So there are some legit data structures which ignore these rules. Like OrderedSet. In those cases, the user needs to specify the non-obvious behaviour. (these are also probably valuable applications of my software.) Can I somehow make the behavior implicit unless it needs specification?

Methods:

- get(i: Index): Node --- should I be using Option here?
- set(i: Index, v:Value)
- get(i: unique): Node
- set(i: unique, v: Value)
- findMin[Column]() 
- findMax[Column]()
- size
- findNextLargest[Column](v: Column): Option[Node]
- delete(n: Node)
- values[Column](): collection of values? How should this work?