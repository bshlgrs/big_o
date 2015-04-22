Here are some interesting facts about data structures I have learned recently:

- The problem with the naive implementation of a binary search tree is that it has worst case performance when you insert a sorted list. Self-balancing BSTs fix this, but I certainly couldn't implement one off the top of my head. If you need a roughly balanced BST and you only have five minutes to write it, I recommend just using a normal BST implementation, but ordering it based on a hashed version of the key. 

- 