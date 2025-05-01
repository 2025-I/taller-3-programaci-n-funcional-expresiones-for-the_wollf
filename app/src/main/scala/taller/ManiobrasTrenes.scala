package taller

object ManiobrasTrenes {
  type Vagon = Char
  type Tren = List[Vagon]
  type Estado = (Tren, Tren, Tren)
  type Maniobra = List[Movimiento]

  sealed trait Movimiento
  case class Uno(n: Int) extends Movimiento
  case class Dos(n: Int) extends Movimiento

  // Función faltante que causaba el error
  def aplicarMovimientos(e: Estado, movs: Maniobra): List[Estado] = {
    @annotation.tailrec
    def loop(movs: Maniobra, current: Estado, acc: List[Estado]): List[Estado] = {
      movs match {
        case Nil => (current :: acc).reverse
        case m :: ms =>
          val nuevoEstado = aplicarMovimiento(current, m)
          loop(ms, nuevoEstado, current :: acc)
      }
    }
    loop(movs, e, Nil)
  }

  def aplicarMovimiento(e: Estado, m: Movimiento): Estado = m match {
    case Uno(n) if n > 0 =>
      val (principal, uno, dos) = e
      val (movidos, restantes) = principal.splitAt(n)
      (restantes, movidos.reverse ++ uno, dos)

    case Dos(n) if n > 0 =>
      val (principal, uno, dos) = e
      val (movidos, restantes) = principal.splitAt(n)
      (restantes, uno, movidos.reverse ++ dos)

    case _ => e
  }

  def definirManiobra(t1: Tren, t2: Tren): Maniobra = {
    require(t1.sorted == t2.sorted, "Los trenes deben contener los mismos vagones")

    case class Nodo(estado: Estado, movimientos: List[Movimiento])

    @annotation.tailrec
    def bfs(queue: Vector[Nodo], visited: Set[Estado]): Maniobra = {
      if (queue.isEmpty) Nil
      else {
        val nodo = queue.head
        val (principal, uno, dos) = nodo.estado

        if (dos == t2 && principal.isEmpty && uno.isEmpty) nodo.movimientos.reverse
        else if (visited.contains(nodo.estado)) bfs(queue.tail, visited)
        else {
          val posiblesMovimientos =
            if (principal.length > 100) List(Uno(1), Dos(1))
            else (1 to principal.length).flatMap(n => List(Uno(n), Dos(n))).toList

          val nuevosNodos = posiblesMovimientos.iterator
            .map(m => Nodo(aplicarMovimiento(nodo.estado, m), m :: nodo.movimientos))
            .filterNot(n => visited.contains(n.estado))
            .toVector

          bfs(queue.tail ++ nuevosNodos, visited + nodo.estado)
        }
      }
    }

    if (t1 == t2) Nil
    else bfs(Vector(Nodo((t1, Nil, Nil), Nil)), Set.empty)
  }
}