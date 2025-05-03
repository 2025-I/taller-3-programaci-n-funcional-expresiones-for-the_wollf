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

# Conclusiones Integrales del Sistema de Maniobras de Trenes

## Síntesis de Resultados y Ventajas

### 1. Correctitud y Fiabilidad Matemática
- **Garantía de solución**:
  $$
  \forall T_1, T_2 \quad \text{definirManiobra}(T_1, T_2) \neq \bot \iff T_1 \sim_{\text{perm}} T_2
  $$

- **Invariante fundamental**:
  $$
  |P| + |U| + |D| = |T_1| \quad \forall \text{ estado } (P, U, D)
  $$