class BuckArray < Array
  def initialize
    @method_call_frequencies = {}
  end

  def method_missing(method_name, args)
    {[method_name, args] => self.length}
    self.send(method_name, args)
  end

  def method_call_frequencies
    @method_call_frequencies
  end
end
