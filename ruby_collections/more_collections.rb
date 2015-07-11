class Magic
  def self.make_class(name, fields)
    thing = Magic.new(name, fields)
    yield thing
    thing
  end

  def initialize(name, fields)
    @name = name
    @fields = fields
    @methods = []
  end

  def method(name, args, ast)
    @methods << [name, args, ast]
  end
end

class Call
  def initialize(method, args)
    @method = method
    @args = args
  end
end

p (Magic.make_class "Stack", value: :int do |stack|
  stack.method "push", [:value], Call.new(:push, [:value])
  stack.method "pop", [:value], Call.new(:pop, [])
end)

def Stack
  def initialize
    @stack = []
  end

  def push(value)
    @stack << {value: value}
  end

  def pop
    @stack.pop.value
  end
end

def StackSumComponent
  def initialize
    @sum = 0
  end

  def push(value)
    @sum += value
  end

  def pop; end
  
  def value_has_been_removed(value)
    @sum -= value
  end

  def get_sum
    @sum
  end
end

def SumStack
  def initialize
    @stack = []
    @sum = 0
  end

  def push(value)
    @stack << {value: value}
    @sum += value
  end

  def pop
    @stack.pop.value.tap { |x| @sum -= x }
  end

  def get_sum
    @sum
  end
end



def MinStack
  def initialize
    @stack = []
    @minimums = []
  end

  def push(item)
    @stack << item
    @minimums << [minimums.last, item].min
  end

  def pop
    result = @stack.pop
    @minimums.pop
    result
  end

  def get_minimum
    @minimum
  end
end

class Object
  def things(*args)
    args.map { |method| x.send(method) }
  end
end

