# stages

## desiderata

### methods:

global: get, add
table: getMin
node: [getter and setter for each field]
Option: Map

### example:


    DoublePriorityQueue <: Map(item: Int, [p1: Float, p2: Float]) {
      getPriorities(i: Int): Option[(Float, Float)] = get(i).map((x) => (x.p1, x.p2))
      add(i: Int, p1: Float, p2: Float) = add(i, p1, p2)
      adjustP1(i: Int, p1: Float) = get(i).p1.setP1(p1)
      adjustP2(i: Int, p2: Float) = get(i).p2.setP2(p2)
      popMinP1(): Option[Int] = p1.getMin().map((x) => x.detach(); x.item)
      popMinP2(): Option[Int] = p2.getMin().map((x) => x.detach(); x.item) 
    }

decompose into map methods to get costs:

    getPriorities => [get]
    add => [add]
    adjustP1 => [get, update(p1)]
    popMinP1 => [getMin(p1), detach]

Now, the hard part is finding the correct data structures given that list of methods. For the moment, let's treat all data structures as having a privileged "key" field. For example, in this case, the correct answer is a hashmap and two heaps. Let's write that as:

    HashMap[item], MinHeap[p1], MinHeap[p2]

So the question is how we decide on those three data structures. It's impossible to choose without some way of trading off speed between different functions. How about we just generate all data structures which aren't strictly dominated by anything, and aren't dominated by anything with less memory usage?

(A good long term solution would be to get the user to enter the number of things they expect in their data structure, and how regularly they expect to call the different functions. Then a cost function could be written explicitly.)

I guess this is kind of a breadth first search. If we have the following data structures:

- HashMap
- SortedArray
- BinarySearchTree
- MinHeap

and all those need to choose their keys out of any of the fields. If you have three fields, then there's 12 different structures you could choose. 2**12 isn't *that* big, but it would be nice to not just exhaustively search that.

The only ways of altering nodes are: creating them, updating them, deleting them. Updating has different costs depending on whether you're updating a field which the table uses as a key.

