class Collection
  def initialize(columns_header)
    @columns_header = columns_header
    @rows = []
  end

  def columns_header
    @columns_header
  end

  def add(data)
    @rows << Row.new(self, data)
  end

  def select(column_name)
    return @rows.map { |x| x[column_name] }
  end
end



class Row
  def initialize(collection, data)
    @collection = collection

    if data.keys.sort != @collection.columns_header.keys.sort
      raise "data was wrong"
    else
      @data = data
    end
  end

  def [](column)
    @data[column]
  end

  def []=(column, newValue)
    @data[column] = newValue
  end
end


c = Collection.new({i: Integer, s: String})

c.add({i: 1, s: "hello"})

p c.select(:i)
