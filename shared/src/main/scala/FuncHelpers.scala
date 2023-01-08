package vyxal

import vyxal.impls.UnimplementedOverloadException

/** Helpers for function-related stuff */
object FuncHelpers:

  /** Take a monad and return a proper (not Partial) function that errors if
    * it's not defined for the input
    */
  def fillMonad(
      name: String,
      fn: Context ?=> PartialFunction[VAny, VAny]
  ): VAny => Context ?=> VAny = args =>
    if fn.isDefinedAt(args) then fn(args)
    else throw UnimplementedOverloadException(name, args)

  /** Take a dyad and return a proper (not Partial) function that errors if it's
    * not defined for the input
    */
  def fillDyad(name: String, fn: PartialVyFn[2]): Dyad = (a, b) =>
    val args = (a, b)
    if fn.isDefinedAt(args) then fn(args)
    else throw UnimplementedOverloadException(name, args)

  /** Take a triad and return a proper (not Partial) function that errors if
    * it's not defined for the input
    */
  def fillTriad(name: String, fn: PartialVyFn[3]): Triad = (a, b, c) =>
    val args = (a, b, c)
    if fn.isDefinedAt(args) then fn(args)
    else throw UnimplementedOverloadException(name, args)

  /** Take a tetrad and return a proper (not Partial) function that errors if
    * it's not defined for the input
    */
  def fillTetrad(name: String, fn: PartialVyFn[4]): Tetrad = (a, b, c, d) =>
    val args = (a, b, c, d)
    if fn.isDefinedAt(args) then fn(args)
    else throw UnimplementedOverloadException(name, args)

  def vectorise(fn: VFun)(using ctx: Context): Unit =
    if fn.arity == 1 then ctx.push(vectorise1(fn))
    else if fn.arity == 2 then ctx.push(vectorise2(fn))
    else if fn.arity == 3 then ???
    else if fn.arity == 4 then ???
    else
      throw UnsupportedOperationException(
        s"Vectorising functions of arity ${fn.arity} not possible"
      )

  private def vectorise1(fn: VFun)(using ctx: Context): VAny =
    ctx.pop() match
      case lst: VList =>
        lst.vmap { elem =>
          ctx.push(elem)
          vectorise1(fn)
        }
      case x =>
        ctx.push(x)
        Interpreter.executeFn(fn)

  private def vectorise2(fn: VFun)(using ctx: Context): VAny =
    val b, a = ctx.pop()

    (a, b) match
      case (a: VList, b: VList) =>
        a.zipWith(b) { (x, y) =>
          ctx.push(x)
          ctx.push(y)
          vectorise2(fn)
        }
      case (a: VList, b) =>
        a.vmap { x =>
          ctx.push(x)
          ctx.push(b)
          vectorise2(fn)
        }
      case (a, b: VList) =>
        b.vmap { y =>
          ctx.push(a)
          ctx.push(y)
          vectorise2(fn)
        }
      case (a, b) =>
        ctx.push(a)
        ctx.push(b)
        Interpreter.executeFn(fn)
    end match
  end vectorise2

  private def vectorise3(fn: VFun)(using ctx: Context): VAny =
    val a = ctx.pop()
    val b = ctx.pop()
    val c = ctx.pop()

    (a, b, c) match
      case (a: VList, b: VList, c: VList) =>
        VList.zipMulti(a, b, c) { case Seq(x, y, z) =>
          ctx.push(x)
          ctx.push(y)
          ctx.push(z)
          vectorise3(fn)
        }
      case (a: VList, b: VList, c) =>
        a.zipWith(b) { (x, y) =>
          ctx.push(x)
          ctx.push(y)
          ctx.push(c)
          vectorise3(fn)
        }
      case (a: VList, b, c: VList) =>
        a.zipWith(c) { (x, z) =>
          ctx.push(x)
          ctx.push(b)
          ctx.push(z)
          vectorise3(fn)
        }
      case (a, b: VList, c: VList) =>
        b.zipWith(c) { (y, z) =>
          ctx.push(a)
          ctx.push(y)
          ctx.push(z)
          vectorise3(fn)
        }
      case (a: VList, b, c) =>
        a.vmap { x =>
          ctx.push(x)
          ctx.push(b)
          ctx.push(c)
          vectorise3(fn)
        }
      case (a, b: VList, c) =>
        b.vmap { y =>
          ctx.push(a)
          ctx.push(y)
          ctx.push(c)
          vectorise3(fn)
        }
      case (a, b, c: VList) =>
        c.vmap { z =>
          ctx.push(a)
          ctx.push(b)
          ctx.push(z)
          vectorise3(fn)
        }
      case (a, b, c) =>
        ctx.push(a)
        ctx.push(b)
        ctx.push(c)
        Interpreter.executeFn(fn)
    end match
  end vectorise3

  def reduceByElement(fn: VFun)(using ctx: Context): Unit =
    val iter = ctx.pop()
    ctx.push(MiscHelpers.reduce(iter, fn))
end FuncHelpers