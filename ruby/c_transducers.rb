
class IntRange
  def initialize(startNum, endNum)
    @startNum = startNum
    @endNum = endNum
  end
end

instr = IntRange.new(0, 10).filter(Function.new("x", "x % 2 == 0")).map(Function.new("y", "y * y")).each(
  Function.new("z", "printf(\"%d \", z);"))

# should go to

# int x;
# for(x = 0; x < 10; x ++) {
#   if (x % 2 == 0) {
#     int t2 = x * x;
#     printf("%d ", t2);
#   }
# }