package vyxal

import vyxal.parsing.Lexer

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters.*

/** To avoid loading Scopt with the JSVyxal object */
@JSExportTopLevel("HelpText", moduleID = "helpText")
object HelpText:
  @JSExport
  def getHelpText(): String = CLI.helpText

/** A bridge between the interpreter and JS */
@JSExportTopLevel("Vyxal", moduleID = "vyxal")
object JSVyxal:
  @JSExport
  def execute(
      code: String,
      inputs: String,
      flags: String,
      printFunc: js.Function1[String, Unit],
      errorFunc: js.Function1[String, Unit],
  ): Unit =
    // todo take functions to print to custom stdout and stderr

    // The help flag should be handled in the JS
    if flags.contains('h') then return

    var printRequestCount = 0

    val settings = Settings(online = true).withFlags(flags.toList)
    val globals: Globals = Globals(
      settings = settings,
      printFn = str =>
        if printRequestCount <= 20000 then
          printFunc(str)
          printRequestCount += 1,
      inputs = Inputs(
        inputs.split("\n").map(x => MiscHelpers.eval(x)(using Context())).toSeq
      ),
    )

    val ctx = Context(
      inputs = inputs
        .split("\n")
        .map(x => MiscHelpers.eval(x)(using Context()))
        .toIndexedSeq,
      globals = globals,
    )
    try Interpreter.execute(code)(using ctx)
    catch
      case ex: VyxalException => errorFunc(
          ex.getMessage() +
          (if (ctx.settings.fullTrace) "\n" + ex.getStackTrace().mkString("\n")
          else "")
        )
      case ex: Throwable => errorFunc(
          "Unrecognized error" +
          (if (ctx.settings.fullTrace) ":\n" + ex.getStackTrace().mkString("\n")
          else ", use the 'X' flag for full traceback")
        ) 
  end execute

  @JSExport
  def setShortDict(dict: String): Unit =
    Dictionary._shortDictionary = dict.split("\r\n").toSeq

  @JSExport
  def setLongDict(dict: String): Unit =
    Dictionary._longDictionary = dict.split("\r\n").toSeq

  @JSExport
  def compress(text: String): String = StringHelpers.compressDictionary(text)

  @JSExport
  def decompress(compressed: String): String =
    StringHelpers.decompress(compressed)

  /** Bridge to turn literate code into SBCS */
  @JSExport
  def getSBCSified(code: String): String =
    Lexer.lexLiterate(code).map(Lexer.sbcsify).getOrElse(code)

  @JSExport
  def getCodepage(): String = Lexer.Codepage

  @JSExport
  def getElements() =
    Elements.elements.values.map {
      case Element(
            symbol,
            name,
            keywords,
            _,
            vectorises,
            overloads,
            _,
          ) => js.Dynamic.literal(
          "symbol" -> symbol,
          "name" -> name,
          "keywords" -> keywords.toJSArray,
          "vectorises" -> vectorises,
          "overloads" -> overloads.toJSArray,
        )
    }.toJSArray

  @JSExport
  def getModifiers() =
    Modifiers.modifiers.map {
      case (symbol, info) => js.Dynamic.literal(
          "symbol" -> symbol,
          "name" -> info.name,
          "description" -> info.description,
          "keywords" -> info.keywords.toJSArray,
        )
    }.toJSArray

  @JSExport
  def getVersion(): String = Interpreter.version
end JSVyxal
