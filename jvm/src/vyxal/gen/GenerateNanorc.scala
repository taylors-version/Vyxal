/** To generate nanorc files for syntax highlighting in JLine. See build.sc */
package vyxal.gen

import vyxal.{Elements, Modifiers, VNum}
import vyxal.parsing.Codepage

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.util.matching.Regex

object GenerateNanorc:
  /** The name of the nanorc file for Vyxal in SBCS mode */
  val SBCSNanorc = "vyxal.nanorc"

  /** The name of the nanorc file for Vyxal in literate mode */
  val LitNanorc = "vyxal-lit.nanorc"

  val codepage =
    Codepage.filter(_ != '\n').map(c => Regex.quote(c.toString)).mkString

  /** NOTE: Make sure to escape each $ with another $ */
  val commonHeader = raw"""|syntax "Vyxal" "\.(vy)$$"
    |comment "##"

    |## Default
    |color white "^.+$$"
    |
    |## Structures and lists
    |color yellow "" # TODO
    |color yellow "[})\]]"
    |color yellow "\|"
    |color yellow "(#\[)|⟨"
    |color yellow "(#\])|⟩"
    |
    |## Numbers
    |color cyan "\<((${VNum.DecimalRegex}?ı(${VNum.DecimalRegex}|_)?)|${VNum.DecimalRegex})\>"
    |
    |## Invalid characters
    |color yellow,red "[^$codepage]*"
    |
    |## Modifiers
    |color brightmagenta "${Modifiers.modifiers.keys.map(Regex.quote).mkString("|")}"
    |""".stripMargin

  val commonFooter = """|
    |## Variables
    |color yellow "#[$=!>]\w*"
    |
    |## Strings
    |color green "L?\"[^"„”“\\]*([\"„”“]|$)"
    |
    |## Comments
    |color blue "\s*##.*$$"
    |""".stripMargin

  val litDecimalRegex = raw"(-?((0|[1-9][0-9_]*)?\.[0-9]*|0|[1-9][0-9_]*))"
  val elementKeywords = Elements.elements.values.flatMap(_.keywords)
  val litSpecific = raw"""|
    |## Numbers (literate)
    |color cyan "\<((${litDecimalRegex}i($litDecimalRegex)?)|(i$litDecimalRegex)|$litDecimalRegex|(i\b))\>"
    |
    |## Elements (literate)
    |color magenta "\<(${elementKeywords.map(Regex.quote).mkString("|")})\>"
    |""".stripMargin

  /** Generates nanorc files for both SBCS and literate modes, returning a
    * mapping from each file's name to its contents
    */
  def main(args: Array[String]): Unit =
    val Array(outDir) = args
    val sbcs = commonHeader + commonFooter
    val lit = commonHeader + litSpecific + commonFooter
    Files.write(
      Paths.get(outDir, SBCSNanorc),
      sbcs.getBytes(StandardCharsets.UTF_8),
    )
    Files.write(
      Paths.get(outDir, LitNanorc),
      lit.getBytes(StandardCharsets.UTF_8),
    )
end GenerateNanorc
