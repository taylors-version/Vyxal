package vyxal

import vyxal.impls.Elements

import java.util.regex.Pattern
import scala.util.matching.Regex
import scala.util.parsing.combinator.*
import LiterateToken.*

val hardcodedKeywords = Map(
  // These can't go in the big map, because that's autogenerated
  "if" -> "[",
  "if-true" -> "[",
  "if-then" -> "[",
  "endif" -> "}",
  "end-if" -> "}",
  "for" -> "(",
  "endfor" -> "}",
  "end-for" -> "}",
  "while" -> "{",
  "endwhile" -> "}",
  "end-while" -> "}",
  "lambda" -> "λ",
  "lam" -> "λ",
  "map-lambda" -> "ƛ",
  "map-lam" -> "ƛ",
  "filter-lambda" -> "Ω",
  "filter-lam" -> "Ω",
  "sort-lambda" -> "µ",
  "sort-lam" -> "µ",
  "reduce-lambda" -> "₳",
  "reduce-lam" -> "₳",
  "fold-lambda" -> "₳",
  "fold-lam" -> "₳",
  "endlambda" -> "}",
  "end-lambda" -> "}",
  "end" -> "}",
  "else" -> "|",
  "do-to-each" -> "(",
  "body" -> "|",
  "do" -> "|",
  "branch" -> "|",
  "->" -> "|",
  "then" -> "|",
  "close-all" -> "]"
)

enum LiterateToken:
  // Object instead of String like the normal lexer because it's way easier
  case Word(value: String)
  case AlreadyCode(value: String)
  // This is for strings that are already in SBCS form
  case Number(value: String)
  case Variable(value: String)
  case Newline(value: String)
  case Group(value: List[Object])
  case LitComment(value: String)
  case LambdaBlock(value: List[Object])
  case ListToken(value: List[Object])

object LiterateLexer extends RegexParsers:
  lazy val literateModeMappings: Map[String, String] =
    Elements.elements.values.view.flatMap { elem =>
      elem.keywords.map(_ -> elem.symbol)
    }.toMap ++ Modifiers.modifiers.view.flatMap { (symbol, mod) =>
      mod.keywords.map(_ -> symbol)
    }.toMap ++ hardcodedKeywords

  override def skipWhitespace = true
  override val whiteSpace: Regex = "[ \t\r\f]+".r

  private def decimalRegex = raw"((0|[1-9][0-9]*)?\.[0-9]*|0|[1-9][0-9]*)"
  def number: Parser[LiterateToken] =
    raw"(${decimalRegex}i$decimalRegex?)|(i$decimalRegex)|$decimalRegex|(i( |$$))".r ^^ {
      value =>
        Number(
          value.replace("i", "ı")
        )
    }

  def string: Parser[LiterateToken] = raw"""("(?:[^"\\]|\\.)*["])""".r ^^ {
    value => AlreadyCode(value)
  }

  def singleCharString: Parser[LiterateToken] = """'.""".r ^^ { value =>
    AlreadyCode(value)
  }

  def comment: Parser[LiterateToken] = """##[^\n]*""".r ^^ { value =>
    LitComment(value)
  }

  def lambdaBlock: Parser[LiterateToken] =
    "{" ~> rep(lambdaBlock | """(#}|[^{}])+""".r) <~ "}" ^^ { body =>
      LambdaBlock(body)
    }
  def normalGroup: Parser[LiterateToken] =
    "(" ~> rep(normalGroup | """[^()]+""".r) <~ ")" ^^ { body =>
      Group(body)
    }

  def list: Parser[LiterateToken] =
    "[" ~> repsep(list | """[^\]\[|]+""".r, "|") <~ "]" ^^ { body =>
      ListToken(body)
    }

  def word: Parser[LiterateToken] =
    """[a-zA-Z?!*+=&%><-][a-zA-Z0-9?!*+=&%><-]*""".r ^^ { value =>
      Word(value)
    }

  def varGet: Parser[LiterateToken] = """\$([_a-zA-Z][_a-zA-Z0-9]*)?""".r ^^ {
    value => Variable("#" + value)
  }

  def varSet: Parser[LiterateToken] = """:=([_a-zA-Z][_a-zA-Z0-9]*)?""".r ^^ {
    value => Variable("#" + value.substring(1))
  }

  def augVar: Parser[LiterateToken] = """:>([a-zA-Z][_a-zA-Z0-9]*)?""".r ^^ {
    value => Variable("#>" + value.substring(2))
  }

  def unpackVar: Parser[LiterateToken] = ":=" ~> list ^^ { value =>
    (value: @unchecked) match
      case ListToken(value) =>
        AlreadyCode("#:" + value.map(recHelp).mkString("[", "|", "]"))
  }

  def newline: Parser[LiterateToken] = "\n" ^^^ Newline("\n")

  def branch: Parser[LiterateToken] = "|" ^^^ AlreadyCode("|")

  def tilde: Parser[LiterateToken] = "~" ^^^ AlreadyCode("!")

  def colon: Parser[LiterateToken] = ":" ^^^ AlreadyCode("|")

  def comma: Parser[LiterateToken] = "," ^^^ AlreadyCode(",")
  def rawCode: Parser[LiterateToken] = "#([^#]|#[^}])*#}".r ^^ { value =>
    AlreadyCode(value.substring(1, value.length - 2))
  }

  def tokens: Parser[List[LiterateToken]] = phrase(
    rep(
      number | string | singleCharString | comment | rawCode | list | lambdaBlock | normalGroup |
        unpackVar | varGet | varSet | augVar | word | branch | newline | tilde | colon |
        comma
    )
  )

  def apply(code: String): Either[VyxalCompilationError, List[LiterateToken]] =
    (parse(tokens, code): @unchecked) match
      case NoSuccess(msg, next)  => Left(VyxalCompilationError(msg))
      case Success(result, next) => Right(result)

  def recHelp(token: Object): String =
    token match
      case Word(value)        => value
      case AlreadyCode(value) => value
      case LitComment(value)  => value
      case Number(value)      => value
      case Variable(value)    => value
      case LambdaBlock(value) => value.map(recHelp).mkString("λ", " ", "}")
      case ListToken(value)   => value.map(recHelp).mkString("[", "|", "]")
      case Newline(value)     => value
      case value: String      => value

  def sbcsify(tokens: List[LiterateToken]): String =
    val out = StringBuilder()

    for i <- tokens.indices do
      val token = tokens(i)
      val next = if i == tokens.length - 1 then None else Some(tokens(i + 1))
      token match
        case Word(value) =>
          out.append(
            literateModeMappings.getOrElse(value, value)
          )
        case Number(value) =>
          if value == "0" then out.append("0")
          else
            next match
              case Some(Number(nextNumber)) =>
                if nextNumber == "." && value.endsWith(".") then
                  out.append(value)
                else out.append(value + " ")
              case Some(Group(items)) =>
                if items.length == 1 &&
                  LiterateLexer(items.head.toString)
                    .getOrElse(Nil)
                    .head
                    .isInstanceOf[Number]
                then out.append(value + " ")
                else out.append(value)
              case _ => out.append(value)
        case Variable(value) =>
          next match
            case Some(Number(_)) => out.append(value + " ")
            case Some(Word(w)) =>
              if "[a-zA-Z0-9_]+".r.matches(
                  literateModeMappings.getOrElse(w, "")
                )
              then out.append(value + " ")
              else out.append(value)
            case _ => out.append(value)
        case AlreadyCode(value) => out.append(value)
        case Newline(value)     => out.append(value)
        case Group(value) =>
          out.append(value.map(sbcsify).mkString)
        case LitComment(value) => ""
        case LambdaBlock(value) =>
          out.append(value.map(sbcsify).mkString("λ", "", "}"))
        case ListToken(value) =>
          out.append(value.map(sbcsify).mkString("#[", "|", "#]"))
      end match
    end for

    out.mkString
  end sbcsify

  def sbcsify(token: Object): String =
    token match
      case Word(value) =>
        literateModeMappings.getOrElse(
          value,
          hardcodedKeywords.getOrElse(value, value)
        )
      case AlreadyCode(value) => value
      case Variable(value)    => value
      case Number(value)      => value
      case Group(value)       => value.map(sbcsify).mkString
      case LitComment(value)  => ""
      case LambdaBlock(value) => value.map(sbcsify).mkString("λ", "", "}")
      case ListToken(value)   => value.map(sbcsify).mkString("#[", "|", "#]")
      case value: String      => litLex(value)

  def litLex(code: String): String =
    sbcsify(LiterateLexer(code).getOrElse(Nil))
end LiterateLexer
