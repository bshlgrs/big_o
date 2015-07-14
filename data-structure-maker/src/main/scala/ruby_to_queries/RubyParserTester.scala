package ruby_to_queries

import java.io.StringReader

import org.jrubyparser.ast.Node
import org.jrubyparser.parser.ParserConfiguration
import org.jrubyparser.{CompatVersion, Parser, ast}

object RubyParserTester {
  def main(args: Array[String]): Unit = {
    val codeString = "def foo(bar)\n bar\n end\n foo('astring')"

    val node = parseContents(codeString)
    println(node)
  }

  def parseContents(string: String): Node = {
    val rubyParser = new Parser()
    val in = new StringReader(string)
    val version = CompatVersion.RUBY1_8
    val config = new ParserConfiguration(0, version)
    rubyParser.parse("<code>", in, config)
  }
}
