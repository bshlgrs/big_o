# Cleverer DS gen

I want to add some cleverness. For example, if we need to quickly provide the minimum of our collection, we should do that efficiently. If our collection is a stack, we can get by with a stack of smallest values we've seen so far. If our collection is an array supporting index-based set and get, this doesn't work and we need a min heap and array.

But if we wanted to store the sum, in both cases we're better off just storing a single result and adding or subtracting from this as we go as needed.

I suspect that certain algebraic structures of reduction operations are what determine efficient ways of doing this.

## Examples

### How to deal with queues, stacks, and lists, when you want quick access to reductions

With an invertible, commutative operation, you want to just store the reduction in one place and update it as necessary.

With stacks, an asymptotically optimal strategy to store the result of a non-invertible reduction is just to store it every time you push something. That's O(1) for each operation. If it's invertible, you can save some space by using the inverse operation.

With invertible (non-commutative) operations on a queue, you can still do this: you just left-multiply by the inverse if it's an enqueue operation, and right-multiply if it's a dequeue. Or whatever.

With everything, you can store a tree which remembers intermediate results. This has log(n) time for changing operations. (As a bonus, it gets you results for subarrays in log(n) time too.)

Operations: C+I, I, C, neither (e.g. +, SLn_r *, min, GLn_r *)
Stack: easy, easy, easy, easy
Queue: easy, easy, [amortized easy](http://www.keithschwarz.com/interesting/code/?dir=min-queue), tree
List: easy, tree?, tree?, tree

*Operations which are invertible except for a finite number of annihilators basically count as invertible.* Eg, suppose you want to have quick access to the product of a list. You can just store the product of the non-zero elements and the number of 0s, and update them in the relevant manner when you change elements. This gives you constant time everything.

### Quickly responding to queries about reductions over array slices, in the presence and absence of mutation

In the presence of mutation, you always store a tree of sub-results.

In the absence of mutation, with +, you just store an array of the partial results of reducing from the left, and then take the difference between two partial results in order to get the actual answers.

In the absence of mutation, with SLn_R *, you can store the same array of partial results and just query that, because for example `CD = (AB)^-1 * ABCD`.

In the absence of mutation, with min, I again think that you can't do better than a tree, because your annihilator could be anywhere in a subarray that you make.

In presence of mutation, with GLn_R *, I don't think you can do any better than a tree, for the same reason as above.

What about *, which is invertible except a finite number of annihilators? In the absence of mutation, just let every node in the array know about the index of the 0 closest to it on the left. This gives us O(1) lookups.