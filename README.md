# big_o

programming problem solver

- big O solver
- dynamic programming solver
- question generator
- parallelizer!

abstracting over time. If we want to calculate mean, we want to cache the sum and total.

problems:

- stock picker: Given array a, find integer i, integer j, such that 0 < i < j < size(A), maximizing a[j] - a[i]
		main(a: Array) = maximise (i: Integer, j: Integer) { a[j] - a[i] } suchThat { 0 < i < j < 1}
- maximum subarray: Given array a, find integer i, integer j, such that 0 < i < j < size(A), maximizing sum(a[i:j])
- server for mean: mean() = array.sum / array.length
- insert(x: Int) = arr.push(x)
- server for mode
- server for subsums
- subsum(i: Int, j: Int) = array.slice(i, j).sum
- server for minimums of subarrays
- submin(i: Int, j: Int) = array.slice(i, j).min
- find repeated element of array
   - n log n time, 0 space
   - n time, n space
- find most common element
- find n most common elements
- find n largest items
- server for 40th largest item

General ideas:

- useful to discover that things are monoids or groups, or natural transformations
- Give it all the theorems about reduction operations over monoids
  - monoids:
    sum xs + sum ys = sum (xs ++ ys) = sum (ys ++ xs)
  - groups:
    sum (xs ++ ys) - sum xs = sum ys
- Use transducers for map and filter



Different stages:

- Pythony pseudocode
- Big O
- Query language
- Functional language

## Classes:
List
- includes
- slice
- get
- set
- reduce
- sum, maximum, minimum
- length
- pop
- push
- insertAt
- deleteAt
- Map

Data structures:
- Linked list
- Array
- Resizing array
- Hash map
- Hash set
- Binary tree (AVR, red-black, splay)
- Finger tree (stores intermediate results)  http://hackage.haskell.org/package/fingertree-0.1.0.2/docs/Data-FingerTree.html

