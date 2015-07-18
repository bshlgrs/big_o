require "set"

class UnorderedCollection
  def initialize(things = nil)
    @things = things.to_set || Set.new
  end

  def size
    @things.length
  end

  def reduce(initial_value, &blk)
    @things.reduce(initial_value, &blk)
  end

  def filter(&blk)
    UnorderedCollection.new(@things.select(&blk))
  end

  # do I want to support order_by with a limit?
  def order_by(&blk)
    OrderedCollection.new(@things.sort_by(&blk))
  end

  def limit_by(n, &blk)
    UnorderedCollection.new(@things.sort_by(&blk).take(n))
  end

  def add(item)
    @things.add(item)
  end

  def delete(thing)
    @things.delete(thing)
  end

  # def take(n)
  #   UnorderedCollection.new(@things.take(n))
  # end

  # def drop(n)
  #   UnorderedCollection.new(@things.drop(n))
  # end
end

class OrderedCollection
  # insert methods: pushToFront, pushToEnd, insertBefore, insertAfter
  # update methods, probably. update, move?
  # removal methods: popFromFront, popFromEnd
end
