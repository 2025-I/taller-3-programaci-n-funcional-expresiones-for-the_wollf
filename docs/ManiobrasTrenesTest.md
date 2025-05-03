# Sistema de Maniobras de Trenes: Análisis Formal

## 1. Modelado Matemático del Problema

### 1.1 Definiciones Fundamentales

Sea:
- **Vagón**: $V = \{c \mid c \in \text{Char} \land c \neq \emptyset\}$
- **Tren**: Secuencia ordenada $T = [v_1, v_2, \ldots, v_n]$ donde $v_i \in V$
- **Estado**: Tripleta $E = (P, U, D)$ donde:
    - $P = [p_1, p_2, \ldots, p_k]$ (Vía principal, orden original)
    - $U = [u_1, u_2, \ldots, u_m]$ (Vía auxiliar 1, LIFO)
    - $D = [d_1, d_2, \ldots, d_o]$ (Vía auxiliar 2, LIFO)

### 1.2 Operaciones de Movimiento ($\Sigma$)

Para cada operación $M \in \Sigma$ definimos:
$$
\delta: E \times \Sigma \to E
$$

$$
\delta((P, U, D), M) =
\begin{cases}
(P_{[n:]}, \ \text{reverse}(P_{[0:n]}) \oplus U, D) & \text{si } M = \text{Uno}(n>0) \\
(\text{reverse}(U_{[0:|n|]}) \oplus P, U_{[|n|:]}, D) & \text{si } M = \text{Uno}(n<0) \\
(P_{[n:]}, U, \text{reverse}(P_{[0:n]}) \oplus D) & \text{si } M = \text{Dos}(n>0) \\
(\text{reverse}(D_{[0:|n|]}) \oplus P, U, D_{[|n|:]}) & \text{si } M = \text{Dos}(n<0)
\end{cases}
$$

### 1.3 Problema de Transformación

Dados dos trenes $T_1, T_2$, encontrar secuencia $S = [M_1, M_2, \ldots, M_k]$ tal que:
$$
\delta^*(E_0, S) = E_f \quad \text{donde} \quad
\begin{cases}
E_0 = (T_1, \emptyset, \emptyset) \\
E_f = (\emptyset, \emptyset, T_2)
\end{cases}
$$

**Notación:**
- $\oplus$: Concatenación de listas
- $\text{reverse}(L)$: Inversión de orden de la lista $L$
- $[a:b]$: Sublista desde índice $a$ hasta $b$

---

## Síntesis de Resultados y Ventajas

### 2. Correctitud y Fiabilidad Matemática
- **Garantía de solución**:
  $$
  \forall T_1, T_2 \quad \text{definirManiobra}(T_1, T_2) \neq \bot \iff T_1 \sim_{\text{perm}} T_2
  $$

- **Invariante fundamental**:
  $$
  |P| + |U| + |D| = |T_1| \quad \forall \text{ estado } (P, U, D)
  $$

## 3. Informe de Proceso

### 3.1 Ejecución de los Algoritmos

El algoritmo `definirManiobra` implementa una estrategia de **búsqueda en anchura (BFS)** que explora estados alcanzables desde una configuración inicial $(P_0, \emptyset, \emptyset)$ hasta alcanzar un estado final $(\emptyset, \emptyset, T_2)$, donde todo el tren destino está en la vía dos.

Los movimientos posibles son:
- $\text{Uno}(n)$: mueve $n$ vagones de la vía principal a la auxiliar uno (si $n > 0$) o de la auxiliar uno a la vía principal (si $n < 0$)
- $\text{Dos}(n)$: análogo, pero con la vía auxiliar dos.

Cada transición $\delta$ se registra, y se evita la exploración de estados ya visitados. Se asegura la completitud del algoritmo siempre que $T_1$ y $T_2$ sean permutaciones.

**Ejemplo de ejecución:**
Sea $T_1 = [a, b]$ y $T_2 = [b, a]$, la salida será:
$$
S = [\text{Dos}(1), \text{Dos}(1)]
$$

### 3.2 Diseño Funcional y Recursivo

El sistema se diseñó usando técnicas de programación funcional en Scala:
- Uso de **inmutabilidad** en estructuras como `List` y `Vector`
- Definición de tipos algebraicos:
  ```scala
  sealed trait Movimiento
  case class Uno(n: Int) extends Movimiento
  case class Dos(n: Int) extends Movimiento
  ```

- Uso de recursión de cola (`@tailrec`) para mantener la eficiencia y evitar desbordamiento de pila:
  ```scala
  @annotation.tailrec
  def loop(movs: Maniobra, current: Estado, acc: List[Estado]): List[Estado]
  ```

- Transformaciones puras de estados sin efectos colaterales, lo que facilita pruebas y razonamiento formal.

### 3.3 Pruebas de Software

Se implementaron pruebas unitarias y de integración con **ScalaTest**, cubriendo:

- Correctitud individual de cada operación $\delta$
- Composición de movimientos: $\delta^*(E, S)$
- Comprobación del resultado final:
  ```scala
  def validarFinal(estado: Estado, esperado: Tren): Unit = {
    assert(estado._3 == esperado)
    assert(estado._1.isEmpty && estado._2.isEmpty)
  }
  ```

Se validaron casos con tamaños de tren:
- Pequeños: 2, 3 elementos (permuta simples)
- Medianos: 10, 100
- Grandes: 500, 1000 (con límites de memoria en algunos entornos)

Estas pruebas garantizan que el sistema es sólido, funcional y escalable dentro de los límites razonables de complejidad computacional.
