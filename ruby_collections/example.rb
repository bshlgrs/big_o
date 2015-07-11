class MyStore < MagicStack
  def initialize
    @stack = []
    super
  end

  make_magic_method :sum, :stack_memo_op, &:+, &:-
end

class Magic
  def self.make_magic_method

  end
end

class MagicStack < MagicThing
  def initialize
    super
  end

  def after_push

  end
end