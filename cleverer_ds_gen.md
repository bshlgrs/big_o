# Cleverer DS gen

I want to add some cleverness. For example, if we need to quickly provide the minimum of our collection, we should do that efficiently. If our collection is a stack, we can get by with a stack of smallest values we've seen so far. If our collection is an array supporting index-based set and get, this doesn't work and we need a min heap and array.

But if we wanted to store the sum, in both cases we're better off just storing a single result and adding or subtracting from this as we go as needed.

I suspect that certain algebraic structures of reduction operations are what determine efficient ways of doing this.

## Examples

### How to deal with queues, stacks, and lists, when you want quick access to reductions

With an invertible, commutative operation, you want to just store the reduction in one place and update it as necessary.

Operations which are invertible except for a finite number of annihilators basically count as invertible. Eg, suppose you want to have quick access to the product of a list. You can just store the product of the non-zero elements and the number of 0s, and update them in the relevant manner when you change elements. This gives you constant time everything.

With stacks, an asymptotically optimal strategy to store the result of a non-invertible reduction is just to store it every time you push something. That's O(1) for each operation. If it's invertible, you can save some space by using the inverse operation.

With invertible (non-commutative) operations on a queue, you can still do this: you just left-multiply by the inverse if it's an enqueue operation, and right-multiply if it's a dequeue. Or whatever.

With everything, you can store a tree which remembers intermediate results. This has log(n) time for changing operations. (As a bonus, it gets you results for subarrays in log(n) time too.)



|                    | +    | *        | SLn_R * | min                | GLn_R * |
|--------------------|------|----------|---------|--------------------|---------|
| Stack              | easy | easy     | easy    | easy               | easy    |
| Queue              | easy | easy [1] | easy    | amortized easy [2] | amortized easy [3]    |
| Random access list | easy | tree     | tree?   | tree?              | tree    |

[1]: Here's the algorithm:

```python
class ProductQueue:
  def __init__(self):
    self.queue = Queue()
    self.zero_counter = Queue()
    self.product = 0

  def enqueue(self, item):
    self.queue.enqueue(item)
    if item == 0:
      self.zero_counter += 1
      self.product = 1
    else:
      self.product *= item

  def get_product(self):
    if len(zeroes) == 0:
      return self.product
    else:
      return 0

  def dequeue(self):
    item = self.dequeue()
    if item == 0:
      self.zero_counter -= 1
    return item
```

[2]: [amortized easy](http://www.keithschwarz.com/interesting/code/?dir=min-queue)

[3]: inspired by [2]:

```python

class MatrixProductQueue:
		  """
  This class assumes that enqueues put matrices on the end of an expression,
  and dequeue takes a matrix off the front. Eg if I enqueue A then B then C, I get
  ABC, then if I dequeue I get BC.

  We represent this with two stacks and a partial result. When I enqueue a matrix M, I set my
  saved partial result `product` to be `product * M`, and push it on the in_stack. This means that my stack
  looks backwards.

  The out_stack represents the second part of the matrix product. For example, DEF would be the list
  [D, E, F].

  For every index i in the out_stack, out_stack_products[i] = product(out_stack[:i+1]). So in the previous
  case, our out_stack_products would be the list [D, DE, DEF].

  When we dequeue, we pop off the last element of out_stack and return it, and pop off the last element
  of out_stack_products and discard it.

  To get the product, we multiply the `product` by the last item in `out_stack_products`.
		  """

  def __init__(self):
    self.in_stack = []
    self.out_stack = []
    self.out_stack_products = []
    self.product = 1

  def enqueue(self, item):
    self.in_stack.append(item)
    self.product = self.product * item # writing it this way to emphasise right-multiplication

  def dequeue(self, item):
    if self.out_stack:
      self.out_stack_products.pop()
      return self.out_stack.pop()
    else:
      if self.in_stack:
        self.out_stack_products.pop()
        result = self.in_stack[-1]

        partial_product = 1
        self.out_stack = 0
        for matrix in reversed(self.in_stack):
          partial_product = partial_product * matrix
          self.out_stack.append(matrix)
          self.out_stack_products.append(partial_product)

        self.in_stack = []
        self.product = 1

        return result
      else:
        raise Exception("empty queue")

  def get_product(self):
    return self.product * self.out_stack_products
```

#### Special cases

How about cases where the monoid can be factored into different kinds of monoids, some of which are nicer than others?

### Quickly responding to queries about reductions over array slices, in the presence and absence of mutation

In the presence of mutation, you always store a tree of sub-results.

In the absence of mutation, with +, you just store an array of the partial results of reducing from the left, and then take the difference between two partial results in order to get the actual answers.

In the absence of mutation, with SLn_R *, you can store the same array of partial results and just query that, because for example `CD = (AB)^-1 * ABCD`.

In the absence of mutation, with min, I again think that you can't do better than a tree, because your annihilator could be anywhere in a subarray that you make.

What about *, which is invertible except a finite number of annihilators? In the absence of mutation, just let every node in the array know about the index of the 0 closest to it on the left. This gives us O(1) lookups. In the presence of mutation, I think you can't do better than a tree?

### Queries about one-sided array slices

