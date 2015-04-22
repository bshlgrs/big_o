import bintrees
import time

def treeTest(tree):
  startTime = time.time()
  for x in range(1000, 2000):
    tree1[x] = x**2

  for x in range(2000):
    x in tree1

  return time.time() - startTime

mylist = [(x,x**2) for x in range(1000)]

tree1 = bintrees.AVLTree(mylist)

