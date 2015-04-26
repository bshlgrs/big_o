

class Node
  attr_accessor :data, :parent
  
  def initialize(data, parent)
    self.data = data
    self.parent = parent
  end

  def update(newData)
    self.parent.updateNode(self, newData)
  end

  def [](field)
    self.data[field]
  end

  def method_missing(field)
    self.data[field]
  end

  def pop
    self.parent.detachNode(self)
    self
  end

  def to_s
    "#<Node #{self.data.to_s}>"
  end
end

class MagicMapDataStructure
  attr_accessor :internal_storage

  def initialize
    self.internal_storage = {}
  end

  def columns
    self.value_columns + [self.key_name]
  end

  def get(key)
    self.internal_storage[key]
  end

  def empty?
    self.internal_storage.empty?
  end

  def getMin(column)
    return nil if self.empty?

    result = nil

    self.internal_storage.values.each do |node|
      if result.nil?
        result = node
      elsif result.send(column) > node.send(column) 
        result = node
      end
    end

    result
  end

  def add(dict)
    raise StandardError, "missing key" unless dict.keys.include?(self.key_name)

    self.internal_storage[dict[self.key_name]] = Node.new(dict, self)
  end

  def updateNode(node, newData)
    if newData.keys.include? self.key_name
      self.internal_storage.delete(node[self.key_name])
      self.internal_storage[newData[self.key_name]] = node
    end
    
    node.data = newData
  end

  def detachNode(node)
    self.internal_storage.delete(node[self.key_name])
  end
end



class PriorityQueue < MagicMapDataStructure 
  def key_name
    :item
  end

  def value_columns
    [:priority]
  end

  def getPriority(item)
    self.get(item)[:priority]
  end

  def adjustPriority(item, priority)
    self.get(item).update({:priority => priority})
  end

  def addItem(item, priority)
    self.add({:item => item, :priority => priority})
  end

  def popMin
    self.getMin(:priority).pop[:item]
  end  
end


q = PriorityQueue.new
q.add({item: "Hello", priority: 4})
p q

p q.getPriority("Hello")
p q.popMin
p q