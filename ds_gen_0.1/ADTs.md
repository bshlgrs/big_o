Abstract data structures I allow:

# List[A]

- new(List[A])
- get(index): A
- set(index, value)
- insert(index, value)
- remove(index)
- size: Int
- contains(A): Bool
- minimumBy[Column]: Node

## for later:

- sorted: List[A]
- reduce(identity: B, translator: A => B, op: (B, B) => B): B
- map(A => B): List[B]
- filter(A => Bool): List[A]
- reverse: List[A]

convenience methods:

- append
- prepend
- popEnd
- popStart
- sum
- product
- minimum
- maximum

# Map[A,B]

- new(List[(A, B)])
- get(key: A): Node
- set(key: A, value: B)
- pop(key: A): Node
- delete(key: A) ????
- getOrNextSmallest(key: A): Node
- size
- keys
- values


# Node
- remove ?????