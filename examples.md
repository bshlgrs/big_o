# examples

## Stock picker.

This problem is dynamic programming. :(

    StockPicker {
      pickStocks(stocks: Array[Int]): (Int, Int) = {
        maximise(i: Integer, j:Integer)
          a[j] - a[i]
          suchThat 0 < _ < _ < stocks.length
      }
    }


## Min-stack.

    MinStack (val items: List[Int]) {
      push(item: Int): Unit = items.push(item)
      pop(): Int = items.pop()
      getMin: Int = items.minimum
    }

(The solution is to have a stack of minimums. To figure this out, my algorithm-maker would have to notice that we only take things off at the end, so we can just keep the stack. I'm okay with just getting the heap-based solution to this, where all operations are O(log(n)) instead of O(1).)

## Min-list

    MinList(val items: List[Int]) {
      get = items.get
      set = items.set
      getMinimum = items.minimum
    }

## Ordered priority queue

    OrderedPriorityQueue[A](val items: Collection[(A, Int)]) {
      append(item: A, priority: Int) = items.add(item, priority)
      getMinPriority
      getMin
    }

## Ordered, arbitrary insertion priority queue

...

## Ordered, arbitrary insertion priority queue on both keys and values

## Mean-array

    MeanArray(val items: List[Int])
      get = items.get
      set = items.set
      append = items.append
      getMean = items.sum / items.length

This one should be solved by storing the sum. It's a reduction over a group, so we can do it easily.

## Min-array

    MinArray(val items: List[Int])
      ask items for get, set, append
      getMin = items.minimum

This one: hmm. I think that you can do it with a min heap and an array.

## Median array
    
    MedianArray(val items: List[Int])
      ask items for get, set, append # it's a different story if there's append but not set
      getMedian = items.select(items.length / 2)

I think that the best solution for this is to have a sorted version of the array.