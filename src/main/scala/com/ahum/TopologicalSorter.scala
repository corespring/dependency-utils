package com.ahum

import scala.annotation.tailrec

object TopologicalSorter {

  def sort[T](nodes: (T, Seq[T])*): Seq[(T, Seq[T])] = {

    type DepNode = (T, Seq[T])

    @tailrec
    def innerSort(raw: Seq[DepNode], acc: Seq[DepNode]): Seq[DepNode] = {

      def onEdge(t: DepNode) = {

        def depsAreOnEdge(deps: Seq[T]) = {
          val names = acc.map(_._1)
          deps.forall(names.contains(_))
        }

        val (_, deps) = t

        deps match {
          case Nil => "edge"
          case d if depsAreOnEdge(d) => "edge"
          case _ => "inner"
        }
      }

      def prettyPrint(nodes: Seq[DepNode]) = {
        nodes.map { n => s"${n._1} -> ${n._2.mkString(",")}"}.mkString("\n")
      }

      if (raw.length == 0) {
        acc
      } else {
        val mapped: Map[String, Seq[DepNode]] = raw.groupBy(onEdge)

        val edge = mapped.get("edge").getOrElse {

          lazy val msg =
            s"""
             |Can't find edge of graph with remaining nodes.
             |Check that there are no cyclical dependencies:
             |${prettyPrint(raw)}""".stripMargin

          throw new RuntimeException(msg)
        }
        innerSort(mapped.get("inner").getOrElse(Seq.empty), acc ++ edge)
      }
    }

    def addNodesForUndefinedDeps(node: DepNode, acc: Seq[DepNode]) = {
      val (_, deps) = node
      val undefinedDeps = deps.filter { d =>
        (!nodes.map(_._1).contains(d) && !acc.map(_._1).contains(d))
      }
      val newDeps = undefinedDeps.map { s => (s -> Seq.empty)}
      acc ++ Seq(node) ++ newDeps
    }
    val expandedNodes = nodes.foldRight[Seq[DepNode]](Seq())(addNodesForUndefinedDeps)
    innerSort(expandedNodes, Seq())
  }
}
