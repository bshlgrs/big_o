

class MagicMapDataStructure

end

class PriorityQueue < MagicMapDataStructure 
  self.key_name = :item
  self.columns = [:priority]

  def getPriority(item)
    self.get(item).priority
  end

  def adjustPriority(item, priority)
    self.get(item).update({:priority => priority})
  end

  def popMin
    self.getMin(:priority).pop.item
  end  
end

