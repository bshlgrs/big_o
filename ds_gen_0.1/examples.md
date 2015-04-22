# examples

trying to support these operations on a list of Float:

- get(index): Float
- set(index, value)
- prepend
- append
- insert(index, value)
- remove(index)
- contains(Float): Bool
- minimum: Float


get, set => [ArrayList(get, set)]
get, set, append => [VectorList(get, set, append)]
get, set, contains => [ArrayList(get, set), HashSet(contains)]
contains => [HashSet(contains)]
get, set, insert, remove => [IndexTreeList(get, set, insert, remove)]
get, set, insert, remove, contains => [IndexTreeList(get, set, insert, remove), ListOccuranceCountingHashMap(contains)]


This one is too hard to express in this current framework:
get, set, minimum => Array[(Float, heap index)] + MinHeap[Float]