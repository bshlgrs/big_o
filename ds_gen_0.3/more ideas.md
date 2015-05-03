# methods:

## collection:

    getMin()
    getMax()
    findOrError(value)
    findOrNone(value)
    count(value) // ignore this one for the moment
    contains(key)

## map inherits from collection and adds:
    // all three of these come under "add"
    addOrUpdate(all fields of the table)
    addOrGiveUp(all fields of the table)
    addOrError(all fields of the table)

    // all of these come under "get"
    getOrError(key)
    getOrNone(key)

## multiset on expression:

    add(value)

## list:

    get(index) // throws error
    append(node)
    prepend(node)
    insert(index) // throws error if index is out of bounds

## node:

    detach
    update[field]


# examples:

    PriorityQueue = Map[item: Int, priority: Float] {
      get(item: Int): Float = getOrError(item).priority
      add(item: Int, priority: Float) = addOrError(item, priority)
      popMin(): Item = getMin[priority]().detach().item
    }


Multiset:
  Default:
    general methods:
      contains: n
      find: n
      add: 1 // ?
      update: 1 // ?
      getMin: n
      getMax: n

  MinHeap:
    general methods:
      add: 1
      update: 1
      detach: log(n)
    on my column:
      getMin: 1
      update: log(n)
  DoubleLinkedList:
    general methods:
      add: 1
      update: 1
      detach: 1
  HashMultiSet:
    general methods:
      add: 1
      update: 1
      detach: 1
    on my column:
      contains: 1
      find: 1
List:
  LinkedList:
    methods:
      get: n
      insert: n
      append: 1
      prepend: 1
      deleteNode: 1
  ArrayList:
    methods:
      get: 1
      insert: n
      append: 1
      prepend: n // you can probably make a better version to not have this problem
      deleteNode: n
  FingerTreeList:
    methods:
      get: log(n)
      insert: log(n)
      append: 1
      prepend: 1
      deleteNode: log(n)

Map:
  HashMap:
    collection methods:
      find: 1
      contains: n
    on my column:
      getMin: n
      getMax: n
      contains: 1
      add: 1
      get: 1


    
