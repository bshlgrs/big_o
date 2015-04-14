# PriorityQueue[A]{item: A, priority: Float} {
#   set(item: A, priority: Float) = set[Item](item, priority)
#   get(item: A): Float = get[Item](item).priority
#   popMin(): Item = pop(getMin[Priority]()).item
# }

# turns into:

# PriorityQueue[A]:
#   MinHeap on priority provides getMin[Priority]
#   HashMap on item, priority provides get, set

# turns into

class PriorityQueue:
  def __init__(self):
    self.heap = MinHeap()
    self.map = {}

  def set(self, item, priority):
    (_, hashNode) = self.map[item]
    self.map[item] = priority
    hashNode.set


    I HAVE NO IDEA WHAT I AM DOING