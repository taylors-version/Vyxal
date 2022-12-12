package vyxal.impls

import org.scalatest.funspec.AnyFunSpec

import vyxal.*
import Elements.Impls

/** Tests for specific elements */
class ElementTests extends AnyFunSpec {

  describe("Element +") {
    describe("when given lists") {
      it("should vectorise properly") {
        given Context = Context()
        assertResult(
          VList(
            VList(5, 9),
            "foo0"
          )
        )(
          Impls.add(VList(VList(2, 5), "foo"), VList(VList(3, 4)))
        )
      }
    }
    describe("when given functions") {
      it("should turn two functions into an fgh fork") {
        given ctx: Context = Context()
        // Factorial
        val f = VFun.fromElement(Elements.elements("!"))
        // Function to subtract 8
        val g = VFun.fromLambda(
          AST.Lambda(
            1,
            List.empty,
            AST.makeSingle(AST.Number(8), AST.Command("-"))
          )
        )
        ctx.push(3)
        ctx.push(Impls.add(f, g))
        Interpreter.execute(AST.ExecuteFn)
        assertResult(VNum(1))(ctx.pop())
      }
    }
  }

  describe("Element M") {
    describe("when given two lists") {
      it("should mold them properly") {
        given Context = Context()
        assertResult(
          VList(1, 2, VList(VList(VList(3, 4), 5, 1), 2))
        )(
          Impls.mapElement(
            VList(1, 2, VList(3, 4), 5),
            VList(1, 2, VList(VList(3, 4, 6), 5))
          )
        )
      }
    }
  }

  describe("Element R") {
    describe("when given function and iterable") {
      it("should work with singleton lists") {
        given ctx: Context = Context()
        assertResult(1: VNum)(
          Impls.reduction(
            VList(1),
            VFun(Elements.elements("+").impl, 2, List.empty, ctx)
          )
        )
      }
      it("should calculate sum properly") {
        given ctx: Context = Context()
        assertResult(15: VNum)(
          Impls.reduction(
            VNum(5),
            VFun(Elements.elements("+").impl, 2, List.empty, ctx)
          )
        )
      }
    }
  }
}
