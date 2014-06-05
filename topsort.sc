import scala.annotation.tailrec

def topologicalSort[T](nodes: (T, Seq[T])*): Seq[(T, Seq[T])] = {

  type DepNode = (T, Seq[T])

  @tailrec
  def innerSort(raw: Seq[DepNode], acc: Seq[DepNode]): Seq[DepNode] = {

    def onEdge(t: DepNode) = {

      println(s"-- onEdge? $t")

      def depsAreOnEdge(deps: Seq[T]) = {

        val names = acc.map(_._1)
        val allOnEdge = deps.forall{ d =>
          val isOnEdge = names.contains(d)
          println(s" is $d on edge? $isOnEdge --> ${names}")
          isOnEdge
        }

        println(s"all on edge? $allOnEdge deps: $deps")
        allOnEdge
      }

      val (_, deps) = t

      deps match {
        case Nil => "edge"
        case d if depsAreOnEdge(d) => "edge"
        case _ => "inner"
      }
    }

    def prettyPrint(nodes:Seq[DepNode]) = {
      nodes.map{n => s"${n._1} -> ${n._2.mkString(",")}"}.mkString("\n")
    }

    if (raw.length == 0) {
      acc
    } else {
      val mapped: Map[String, Seq[DepNode]] = raw.groupBy(onEdge)

      println(s"mapped: $mapped")
      val edge = mapped.get("edge").getOrElse {

        println("error -------------")
        lazy val msg =
          s"""
             |Can't find edge of graph with remaining nodes.
             |Check that there are no cyclical dependencies:
             |${prettyPrint(raw)}""".stripMargin

        throw new RuntimeException(msg)
      }
      println(s"edge... $edge")
      innerSort(mapped.get("inner").getOrElse(Seq.empty), acc ++ edge)
    }
  }

  def addNodesForUndefinedDeps(node: DepNode, acc:Seq[DepNode]) = {
    val (_, deps) = node
    val undefinedDeps = deps.filter{ d =>
      (!nodes.map(_._1).contains(d) && !acc.map(_._1).contains(d))
    }
    val newDeps = undefinedDeps.map{ s => (s -> Seq.empty) }
    acc ++ Seq(node) ++ newDeps
  }
  val expandedNodes = nodes.foldRight[Seq[DepNode]](Seq())(addNodesForUndefinedDeps)
  println(s"prepped: $expandedNodes")
  innerSort(expandedNodes, Seq())
}
//edTopSort(1 -> Seq(2), 2 -> Seq(3), 3 -> Seq.empty)
//diamond
//topologicalSort(1 -> Seq(2, 3), 2 -> Seq(4), 3 -> Seq(4), 4 -> Seq.empty)
//topologicalSort(1 -> Seq(2), 2 -> Seq(1))
//topologicalSort(1 -> Seq(2), 2 -> Seq(3,1), 3 -> Seq())
topologicalSort(1 -> Seq(2), 2 -> Seq(3,1), 3 -> Seq())
//edTopSort("A" -> Seq("B", "C"), "B" -> Seq("D"), "C" -> Seq("D"), "D" -> Seq.empty)
//1 - if DepNode not defined for dep assume its a DepNode with no deps
//2 - handle edge fail
//edTopSort(1 -> Seq(2, 3))

