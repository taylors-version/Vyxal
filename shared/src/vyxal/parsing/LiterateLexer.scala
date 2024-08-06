package vyxal.parsing

import vyxal.parsing.TokenType.*
import vyxal.Elements
import vyxal.Modifier
import vyxal.Modifiers
import vyxal.UnopenedGroupException
import vyxal.VyxalException

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

class LiterateLexer extends LexerCommon:
  private val KeywordLetters = raw"a-zA-Z0-9_<>?!*+\-=&%@"
  def headIsOpener: Boolean =
    structOpeners.exists((kw, _) =>
      headLookaheadMatch(s"${Regex.quote(kw)}([^$KeywordLetters]|$$)")
    ) || headEqual("{") ||
      lambdaOpeners.exists((kw, _) =>
        headLookaheadMatch(s"${Regex.quote(kw)}([^$KeywordLetters]|$$)")
      )

  def headIsBranch: Boolean =
    branchKeywords.exists(kw =>
      headLookaheadMatch(s"${Regex.quote(kw)}([^$KeywordLetters]|$$)")
    ) || headEqual("|") || headEqual(",") || headLookaheadMatch(":[^=$!>]")
  def headIsCloser: Boolean =
    closeAllKeywords.exists((kw, _) =>
      headLookaheadMatch(s"${Regex.quote(kw)}([^$KeywordLetters]|$$)")
    ) ||
      endKeywords.exists(kw =>
        headLookaheadMatch(s"${Regex.quote(kw)}([^$KeywordLetters]|$$)")
      )
  private val literateKeywords = Elements.elements.values.flatMap(_.keywords)
  private val _tokens = ArrayBuffer[LitToken]()
  private val groups = ArrayBuffer[ArrayBuffer[LitToken]]()
  private var unpackDepth = 0

  private val groupModifierToToken = Map(
    "." -> ((range) => LitToken(MonadicModifier, "⸠", range)),
    ":" -> ((range) => LitToken(DyadicModifier, "ϩ", range)),
    ":." -> ((range) => LitToken(TriadicModifier, "э", range)),
    "::" -> ((range) => LitToken(TetradicModifier, "Ч", range)),
    "," -> ((range) => LitToken(MonadicModifier, "♳", range)),
    ";" -> ((range) => LitToken(DyadicModifier, "♴", range)),
    ";," -> ((range) => LitToken(TriadicModifier, "♵", range)),
    ";;" -> ((range) => LitToken(TetradicModifier, "♶", range)),
  )

  private val keywords = Map(
    "close-all" -> TokenType.StructureAllClose,
    "end-all" -> TokenType.StructureAllClose,
    "end-end" -> TokenType.StructureDoubleClose,
  )

  private val endKeywords = List(
    "endfor",
    "end-for",
    "endwhile",
    "end-while",
    "endlambda",
    "end-lambda",
    "end",
  )

  private val branchKeywords = List(
    "->",
    "else:",
    "else",
    "elif",
    "else-if",
    "body",
    "do",
    "branch",
    "then",
    "in",
    "using",
    "no?",
    "=>",
    "from",
    "as",
    "with",
    "given",
    ":and:",
    "has",
    "does",
    "using",
    "on",
  )

  /** Map keywords to their token types */
  private val closeAllKeywords = Map(
    "close-all" -> TokenType.StructureAllClose,
    "end-all" -> TokenType.StructureAllClose,
  )

  private val lambdaOpeners = Map(
    "lambda" -> StructureType.Lambda,
    "lam" -> StructureType.Lambda,
    "map-lambda" -> StructureType.LambdaMap,
    "map-lam" -> StructureType.LambdaMap,
    "map<" -> StructureType.LambdaMap,
    "filter-lambda" -> StructureType.LambdaFilter,
    "filter-lam" -> StructureType.LambdaFilter,
    "filter<" -> StructureType.LambdaFilter,
    "sort-lambda" -> StructureType.LambdaSort,
    "sort-lam" -> StructureType.LambdaSort,
    "sort<" -> StructureType.LambdaSort,
    "reduce-lambda" -> StructureType.LambdaReduce,
    "reduce-lam" -> StructureType.LambdaReduce,
    "reduce<" -> StructureType.LambdaReduce,
    "fold-lambda" -> StructureType.LambdaReduce,
    "fold-lam" -> StructureType.LambdaReduce,
    "fold<" -> StructureType.LambdaReduce,
  )

  /** Keywords for opening structures. Has to be a separate map because while
    * all of them have the same [[TokenType]], they have different values
    * depending on the kind of structure
    */
  private val structOpeners = Map(
    // These can't go in the big map, because that's autogenerated
    "yes?" -> StructureType.Ternary,
    "?" -> StructureType.Ternary,
    "if" -> StructureType.IfStatement,
    "for" -> StructureType.For,
    "for<" -> StructureType.For,
    "do-to-each" -> StructureType.For,
    "each-as" -> StructureType.For,
    "while" -> StructureType.While,
    "while<" -> StructureType.While,
    "exists<" -> StructureType.DecisionStructure,
    "relation<" -> StructureType.GeneratorStructure,
    "generate-from<" -> StructureType.GeneratorStructure,
    "generate<" -> StructureType.GeneratorStructure,
  )

  def lex(program: String): Seq[Token] =
    programStack.pushAll(program.reverse.map(_.toString))
    while programStack.nonEmpty do
      if headIsDigit || headLookaheadMatch("-[1-9]") || headEqual(".")
      then numberToken
      else if safeCheck(c =>
          c.length == 1 && (c.head.isLetter || "<>!*+-=&%@".contains(c))
        )
      then keywordToken
      else if headEqual(""""""") then stringToken(true)
      else if headEqual("(") then
        eat("(")
        groups += ArrayBuffer[LitToken]()
        if headLookaheadMatch(":[.:]|;[,;]") then
          addToken(groupModifierToToken(pop(2))(Range(index, index)))
        else if headIn(".:,;") then
          addToken(groupModifierToToken(pop(1))(Range(index, index)))
      else if headEqual(")") then
        if groups.nonEmpty then
          val group = groups.last
          groups.dropRightInPlace(1)
          addToken(
            LitToken(Group, group.toSeq, Range(index, index))
          )
          eat(")")
        else throw new UnopenedGroupException(index)
      else if headEqual("\n") then quickToken(Newline, "\n")
      else if headIsWhitespace then pop(1)
      else if headEqual("{") || lambdaOpeners.contains(programStack.head) then
        addToken(LitToken(TokenType.StructureOpen, "λ", Range(index, index)))
        pop()
        lambdaParameters
      else if structOpeners.contains(programStack.head) then
        val tempRange = Range(index, index)
        addToken(TokenType.StructureOpen, structOpeners(pop()).open, tempRange)
      else if headIsBranch then quickToken(TokenType.Branch, "|")
      else if endKeywords.contains(programStack.head) || headEqual("}") then
        quickToken(TokenType.StructureClose, "}")
      else if headEqual("end-end") then
        quickToken(TokenType.StructureDoubleClose, ")")
      else if closeAllKeywords.contains(programStack.head) then
        quickToken(TokenType.StructureAllClose, "]")
      else if headLookaheadMatch("\\$([^@:]|$)") then
        pop()
        getVariableToken
      else if headLookaheadEqual(":=[") then
        quickToken(TokenType.UnpackTrigraph, "#:[")
        unpackDepth = 1
      else if headLookaheadEqual(":=") then
        pop(2)
        setVariableToken
      else if headLookaheadEqual(":!=") then
        pop(3)
        setConstantToken
      else if headLookaheadEqual(":>") then
        pop(2)
        augmentedAssignToken
      else if headLookaheadEqual("$@") then
        pop(2)
        commandSymbolToken
      else if headLookaheadEqual("define") then customDefinitionToken
      else if headEqual("[") then
        pop()
        if unpackDepth > 0 then
          unpackDepth += 1
          addToken(LitToken(TokenType.ListOpen, "[", Range(index, index)))
        else addToken(TokenType.ListOpen, "#[", Range(index, index))
      else if headEqual("]") then
        pop()
        if unpackDepth > 0 then
          unpackDepth -= 1
          addToken(
            LitToken(TokenType.StructureAllClose, "]", Range(index, index))
          )
        else addToken(TokenType.ListClose, "#]", Range(index, index))
      else if headIsWhitespace then
        if headEqual("\n") then
          addToken(LitToken(Newline, "\n", Range(index, index)))
        pop()
      else if headLookaheadEqual("##") then
        while safeCheck(c => c != "\n" && c != "\r") do pop()
      else
        for c <- pop() do
          addToken(
            LitToken(TokenType.Command, c.toString, Range(index, index))
          )
          index += 1
      end if
    end while
    // Flatten _tokens into tokens

    for token <- flattenTokens(_tokens.toSeq) do tokens += token.toNormal
    tokens.toSeq
  end lex

  def flattenTokens(tokens: Seq[LitToken]): Seq[LitToken] =
    val flattened = ArrayBuffer[LitToken]()
    for token <- tokens do
      token.tokenType match
        case Group =>
          val group = token.value.asInstanceOf[Seq[LitToken]]
          val flattenedGroup = flattenTokens(group)
          flattened ++= flattenedGroup
        case _ => flattened += token
    flattened.toSeq
  def addToken(token: LitToken): Unit =
    if groups.nonEmpty then groups.last += token
    else _tokens += token

  def addToken(tokenType: TokenType, value: String, range: Range): Unit =
    addToken(LitToken(tokenType, value, range))

  def lastToken: LitToken =
    if groups.nonEmpty then
      groups.last.lastOption.getOrElse(LitToken(Empty, "", Range.fake))
    else _tokens.lastOption.getOrElse(LitToken(Empty, "", Range.fake))

  def dropLastToken(): Unit =
    if groups.nonEmpty then groups.last.dropRightInPlace(1)
    else _tokens.dropRightInPlace(1)

  private def keywordToken: Unit =
    val start = index
    val keyword = StringBuilder()
    if !safeCheck(c => c.head.isLetterOrDigit || "_<>?!*+\\-=&%@".contains(c))
    then return

    while safeCheck(c =>
        c.head.isLetterOrDigit || "_<>?!*+\\-=&%'@".contains(c)
      )
    do keyword ++= pop(1)
    val value = removeDoubleNt(keyword.toString())
    if isKeyword(value) || value.length() == 1 then
      if value == "i" then addToken(LitToken(Number, "ı", Range(start, index)))
      else
        addToken(
          LitToken(
            Command,
            getSymbolFromKeyword(value),
            Range(start, index),
          )
        )
    else if isKeyword(value.stripSuffix("n't")) then
      addToken(
        LitToken(NegatedCommand, value, Range(start, index))
      )
    else if isModifier(value) then
      val mod = getModifierFromKeyword(value)
      val name = Modifiers.modifiers.find(_._2._3.contains(value)).get._1
      val tokenType = mod.arity match
        case 1 => TokenType.MonadicModifier
        case 2 => TokenType.DyadicModifier
        case 3 => TokenType.TriadicModifier
        case 4 => TokenType.TetradicModifier
        case _ => TokenType.SpecialModifier
      addToken(LitToken(tokenType, name, Range(start, index)))
    else programStack.push(value)
    end if
  end keywordToken

  private def removeDoubleNt(word: String): String =
    var temp = word
    while temp.endsWith("n'tn't") do temp = temp.stripSuffix("n'tn't")
    temp

  private def isKeyword(word: String): Boolean =
    literateKeywords.toSet.contains(word)

  private def isModifier(word: String): Boolean =
    Modifiers.modifiers.values.exists(_.keywords.contains(word))

  private def getSymbolFromKeyword(word: String): String =
    Elements.elements.values
      .find(elem => elem.keywords.contains(word))
      .map(_.symbol)
      .getOrElse(word)

  private def getModifierFromKeyword(word: String): Modifier =
    Modifiers.modifiers.values.find(mod => mod._3.contains(word)).get

  /** Number = 0 | [1-9][0-9]*(\.[0-9]*)? | \.[0-9]* */
  private def numberToken: Unit =
    val rangeStart = index
    // Check the single zero case
    if headLookaheadMatch("0[^.ı]") then
      val zeroToken = Token(TokenType.Number, "0", Range(index, index))
      pop(1)
      tokens += zeroToken

    // Then check for negative
    val sign = if headEqual("-") then pop(1) else ""

    // Then the headless decimal case
    if headEqual(".") then
      pop(1)
      if safeCheck(c => c.head.isDigit) then
        val head = simpleNumber()
        val numberToken = LitToken(
          TokenType.Number,
          s"${sign}.$head",
          Range(rangeStart, index),
        )
        addToken(numberToken)
      else
        val zeroToken = LitToken(
          TokenType.Number,
          ".",
          Range(rangeStart, index),
        )
        addToken(zeroToken)
    else
      // Not a 0, and not a headless decimal, so it's a normal number
      val head = simpleNumber()
      // Test for a decimal tail
      if headEqual(".") then
        pop(1)
        if safeCheck(c => c.head.isDigit) then
          val tail = simpleNumber()
          val numberToken = LitToken(
            TokenType.Number,
            s"$sign$head.$tail",
            Range(rangeStart, index),
          )
          addToken(numberToken)
        else
          val numberToken = LitToken(
            TokenType.Number,
            s"${sign}$head.",
            Range(rangeStart, index),
          )
          addToken(numberToken)
      // No decimal tail, so normal number
      else
        val numberToken = LitToken(
          TokenType.Number,
          s"$sign$head",
          Range(rangeStart, index),
        )
        addToken(numberToken)
    end if
    if headEqual("i") then
      // Grab an imaginary part and merge with the previous number
      pop()
      val combinedTokenValue = lastToken.value.asInstanceOf[String] + "ı"
      dropLastToken()
      numberToken
      val finalTokenValue = combinedTokenValue +
        lastToken.value.asInstanceOf[String]
      dropLastToken()
      addToken(
        LitToken(TokenType.Number, finalTokenValue, Range(rangeStart, index))
      )

  end numberToken

  private def simpleNumber(): String =
    val numberVal = StringBuilder()
    while safeCheck(c => c.head.isDigit || c == "_") do
      if headEqual("_") then pop()
      else numberVal ++= s"${pop()}"
    numberVal.toString()

  private def customDefinitionToken: Unit =
    val rangeStart = index
    pop()
    tokens +=
      Token(
        TokenType.StructureOpen,
        "#::",
        Range(rangeStart, index),
      )
    eatWhitespace()
    if !headLookaheadMatch("(element|modifier)") then
      throw VyxalException(
        s"Invalid definition type. Expected \"element\" or \"modifier\""
      )
    val definitionType = pop().toUpperCase()
    while !headIsWhitespace do pop()
    eatWhitespace()
    val nameRangeStart = index
    val name = simpleName()

    tokens +=
      Token(
        TokenType.Param,
        s"$definitionType$name",
        Range(nameRangeStart, index),
      )

    quickToken(TokenType.Branch, "|")
    val params = lambdaParameters
    val arity = calcArity(params)

    symbolTable += s"$definitionType$name" -> arity

  end customDefinitionToken

  lazy val mapping: Map[String, String] =
    Elements.elements.values.view.flatMap { elem =>
      elem.keywords.map(_ -> elem.symbol)
    }.toMap ++
      Modifiers.modifiers.view.flatMap { (symbol, mod) =>
        mod.keywords.map(_ -> symbol)
      }.toMap ++
      keywords.map { (kw, typ) => kw -> typ.canonicalSBCS.get }.toMap ++
      endKeywords.map(_ -> TokenType.StructureClose.canonicalSBCS.get).toMap ++
      branchKeywords.map(_ -> TokenType.Branch.canonicalSBCS.get).toMap ++
      (lambdaOpeners ++ structOpeners).map { (kw, typ) => kw -> typ.open }
end LiterateLexer
