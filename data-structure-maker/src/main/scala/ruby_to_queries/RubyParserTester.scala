package ruby_to_queries

import java.io.StringReader

import org.jrubyparser.ast.Node
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.{CompatVersion, Parser, ast}

object RubyParserTester {
  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("example-apis/AverageAgeMultiset.rb")
    val lines = try source.mkString finally source.close()

    val node = parseContents(lines)
    println(node)
  }

  def parseContents(string: String): Node = {
    val rubyParser = new Parser()
    val in = new StringReader(string)
    val version = CompatVersion.RUBY2_0
    val config = new ParserConfiguration(0, version)
    rubyParser.parse("<code>", in, config)
  }
}
