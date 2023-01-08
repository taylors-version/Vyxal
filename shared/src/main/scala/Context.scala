package vyxal

import scala.collection.mutable.Stack
import scala.collection.mutable as mut
import scala.io.StdIn

/** @constructor
  *   Make a Context object for the current scope
  * @param stack
  *   The stack on which all operations happen
  * @param _contextVar
  *   The context variable. It's an Option because this scope might not have its
  *   own context variable. See [[this.contextVarN]] for more information.
  * @param vars
  *   The variables currently in scope, accessible by their names. Null values
  *   signify that the variable is nonlocal, i.e., it should be gotten from the
  *   parent context
  * @param inputs
  *   The inputs available in this scope
  * @param parent
  *   The context inside which this context is (to inherit variables). `None`
  *   for toplevel contexts
  */
class Context private (
    private var stack: mut.ArrayBuffer[VAny],
    private var _contextVarN: Option[VAny] = None,
    private var _contextVarM: Option[VAny] = None,
    private val vars: mut.Map[String, VAny] = mut.Map(),
    private var inputs: Inputs = Inputs(),
    private val parent: Option[Context] = None,
    val globals: Globals = Globals()
):
  def settings: Settings = globals.settings

  def pop(): VAny =
    val elem =
      if stack.nonEmpty then stack.remove(stack.size - 1)
      else if inputs.nonEmpty then inputs.next()
      else
        val temp = StdIn.readLine()
        if temp.nonEmpty then Parser.parseInput(temp)
        else settings.defaultValue
    if settings.logLevel == LogLevel.Debug then println(s"Popped $elem")
    elem

  /** Pop n elements and wrap in a list */
  def pop(n: Int): List[VAny] = List.fill(n)(this.pop())

  /** Get the top element on the stack without popping */
  def peek: VAny =
    if stack.nonEmpty then stack.last
    else if inputs.nonEmpty then inputs.peek
    else settings.defaultValue

  /** Get the top n elements on the stack without popping */
  def peek(n: Int): List[VAny] =
    // Number of elements peekable from the stack
    val numStack = n.max(stack.length)
    // Number of elements that need to be taken from the input
    val numInput = n - numStack
    // todo repeat the inputs or something?
    inputs.peek(numInput) ++ stack.slice(stack.length - numStack, stack.length)

  def push(item: VAny): Unit = stack += item

  /** Whether the stack is empty */
  def isStackEmpty: Boolean = stack.isEmpty

  /** Get the context variable for this scope if it exists. If it doesn't, get
    * the context variable from the parent. If there's no parent Context, just
    * get the default value (0)
    */
  def contextVarN: VAny =
    _contextVarN
      .orElse(parent.map(_.contextVarN))
      .getOrElse(settings.defaultValue)

  /** Setter for the context variable so that outsiders don't have to deal with
    * it being an Option
    */
  def contextVarN_=(newCtx: VAny) =
    _contextVarN = Some(newCtx)

  def contextVarM: VAny =
    _contextVarM
      .orElse(parent.map(_.contextVarM))
      .getOrElse(settings.defaultValue)

  /** Setter for the context variable so that outsiders don't have to deal with
    * it being an Option
    */
  def contextVarM_=(newCtx: VAny) =
    _contextVarM = Some(newCtx)

  /** Get a variable by the given name. If it doesn't exist in the current
    * context, looks in the parent context. If not found in any context, returns
    * the default value (0).
    */
  def getVar(name: String): VAny =
    vars
      .get(name)
      .orElse(parent.map(_.getVar(name)))
      .getOrElse(settings.defaultValue)

  /** Set a variable to a given value. If found in this context, changes its
    * value. If it's not found in the current context but it exists in the
    * parent context, sets it there. Otherwise, creates a new variable in the
    * current context.
    */
  def setVar(name: String, value: VAny): Unit =
    if vars.contains(name) then vars(name) = value
    else
      Context.findParentWithVar(this, name) match
        case Some(parent) => parent.setVar(name, value)
        case None         => vars(name) = value

  /** Make a new Context for a structure inside the current structure */
  def makeChild() = new Context(
    stack,
    _contextVarN,
    _contextVarM,
    vars,
    inputs,
    Some(this),
    globals
  )
end Context

object Context:
  def apply(
      inputs: Seq[VAny] = Seq.empty,
      globals: Globals = Globals()
  ): Context =
    new Context(
      stack = mut.ArrayBuffer(),
      inputs = Inputs(inputs),
      globals = globals
    )

  /** Find a parent that has a variable with the given name */
  @annotation.tailrec
  private def findParentWithVar(
      ctx: Context,
      varName: String
  ): Option[Context] =
    ctx.parent match
      case Some(parent) =>
        if parent.vars.contains(varName) then Some(parent)
        else findParentWithVar(parent, varName)
      case None => None

  /** Make a new Context for a function that was defined inside `origCtx` but is
    * now executing inside `currCtx`
    *
    * @param origCtx
    *   The context in which the function was defined
    * @param currCtx
    *   The context where the function is currently executing
    * @param popArgs
    *   Whether the inputs for the function will be popped from the stack
    *   (instead of merely peeking)
    */
  def makeFnCtx(
      origCtx: Context,
      currCtx: Context,
      arity: Int,
      params: List[String],
      args: Option[Seq[VAny]],
      popArgs: Boolean
  ) =
    val newInputs = args
      .map(_.toList)
      .getOrElse(if popArgs then currCtx.pop(arity) else currCtx.peek(arity))
    if currCtx.settings.logLevel == LogLevel.Debug then
      println(
        s"newInputs = $newInputs, arity = $arity, stack = ${currCtx.stack}, popArgs = $popArgs"
      )
    new Context(
      mut.ArrayBuffer.empty,
      currCtx._contextVarN,
      currCtx._contextVarM,
      mut.Map(params.zip(newInputs)*),
      Inputs(newInputs),
      Some(currCtx),
      currCtx.globals
    )
  end makeFnCtx
end Context