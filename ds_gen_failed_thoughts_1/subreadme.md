
# v0.1

everything is an array

[get, set, insert, delete, contains, push]

we have array, linked list, bst with indices, index tree

[get] # => array
[get, insert] # => index tree
[get, set, push] # => array
[get, set, insert] # => index tree
[push, contains] # => BST
[get, set, contains] # => array and BST

# v0.3

class Dictionary[Key, Value](table: {key: unique Key, value: Value}) {
  get(key: Key): Option[Value] = table.get(key)
  set(key: Key, value: Value): Unit = table.set(key, value)
}

class MinArray[Item <: Orderable](table: List[Item]) {
  get(index: Int): Item = table.get(index)
  set(index: Int, value: Item): Item = table.set(index, value)
  getMin(): Option[Item] = table.getMin()
}

## v0.4

class DifferenceArray(table: List[Item]) {
  get(index: Int): Item = table.get(index)
  set(index: Int, value: Item): Item = table.set(index, value)
  getDifference(): Option[Item] = table.getMax() - table.getMin()
}