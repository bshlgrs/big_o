require "json"
require "erb"

class Mixin
  def self.all_mixins
    @all_mixins ||= {}.tap do |mixins|
      Dir.entries("./mixins").select { |x| x =~ /.json/ }.each do |mixin_file|
        mixin_json = JSON.parse(File.read("mixins/"+mixin_file))
        mixins[mixin_json["type"]] = mixin_json
      end
    end
  end

  def self.create(data_structure_json, mixin_json)
    raise Exception.new("I don't know what #{mixin_json["type"]} is") unless Mixin.all_mixins[mixin_json["type"]]
    Mixin.new(mixin_json)
  end

  def initialize(json)
    @json = json
    @mixin_json = Mixin.all_mixins[@json["type"]] || (raise Exception.new("I don't know what #{mixin_json["type"]} is"))
  end

  def method_missing(method_name, *args)
    puts "in method_missing with #{method_name}"
    if args == []
      @json[method_name]
    end
  end

  def render_method(method_name)
    ERB.new(@mixin_json["methods"][method_name]["code"]).result(binding)
  end
end


def make_stack(stack_json)
  mixins = stack_json["mixins"].map do |mixin_json|
    Mixin.create(stack_json, mixin_json)
  end

  p mixins

  stack_erb = ERB.new(File.read("./stack.rb.erb"))

  stack_erb.result(binding)
end

stack_json = JSON.parse(File.read("./example_stack.json"))

puts Mixin.all_mixins
puts make_stack(stack_json)