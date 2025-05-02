package taller

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner
import taller.ManiobrasTrenes._

@RunWith(classOf[JUnitRunner])
class ManiobrasTrenesTest extends AnyFunSuite {
  val a = 'a'
  val b = 'b'
  val c = 'c'
  val d = 'd'

  def validarFinal(estado: Estado, esperado: Tren): Unit = {
    assert(estado._3 == esperado)
    assert(estado._1.isEmpty && estado._2.isEmpty)
  }

  test("aplicarMovimiento: Uno(2) en (abcd, Nil, Nil) debe dar (cd, ba, Nil)") {
    val estadoInicial = (List(a, b, c, d), Nil, Nil)
    assert(aplicarMovimiento(estadoInicial, Uno(2)) == (List(c, d), List(b, a), Nil))
  }

  test("aplicarMovimiento: Dos(3) en (abcd, Nil, Nil) debe mover 3 vagones a dos") {
    val estadoInicial = (List(a, b, c, d), Nil, Nil)
    assert(aplicarMovimiento(estadoInicial, Dos(3)) == (List(d), Nil, List(c, b, a)))
  }

  test("aplicarMovimientos: secuencia Uno(1), Dos(1)") {
    val estadoInicial = (List(a, b, c), Nil, Nil)
    val movimientos = List(Uno(1), Dos(1))
    val resultados = aplicarMovimientos(estadoInicial, movimientos)
    assert(resultados == List(
      (List(a, b, c), Nil, Nil),
      (List(b, c), List(a), Nil),
      (List(c), List(a), List(b))
    ))
  }

  test("definirManiobra: tren ya en orden correcto") {
    val tren = List(a, b, c)
    assert(definirManiobra(tren, tren) == Nil)
  }

  test("definirManiobra: revertir tren simple") {
    val t1 = List(a, b)
    val t2 = List(b, a)
    val movimientos = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movimientos)
    validarFinal(estados.last, t2)
  }

  // Pruebas de rendimiento
  test("Prueba juguete: 10 vagones reversa") {
    val t1 = ('a' to 'j').toList
    val t2 = t1.reverse
    val movs = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movs)
    validarFinal(estados.last, t2)
  }

  test("Prueba pequeña: 100 vagones reversa") {
    val t1 = List.tabulate(100)(i => (i % 256).toChar)
    val t2 = t1.reverse
    val movs = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movs)
    validarFinal(estados.last, t2)
  }

  test("Prueba mediana: 500 vagones reversa") {
    val t1 = List.tabulate(500)(i => (i % 256).toChar)
    val t2 = t1.reverse
    val movs = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movs)
    validarFinal(estados.last, t2)
  }

  test("Prueba grande: 1000 vagones reversa") {
    val t1 = List.tabulate(1000)(i => (i % 256).toChar)
    val t2 = t1.reverse
    val movs = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movs)
    validarFinal(estados.last, t2)
  }

  private def testManiobra(t1: Tren, t2: Tren): Unit = {
    val movimientos = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movimientos)
    val estadoFinal = estados.last

    assert(estadoFinal._3 == t2)
    assert(estadoFinal._1.isEmpty)
    assert(estadoFinal._2.isEmpty)
  }

  private def timed[T](block: => T): (T, Long) = {
    val start = System.nanoTime()
    val result = block
    val end = System.nanoTime()
    (result, (end - start) / 1_000_000)
  }
}