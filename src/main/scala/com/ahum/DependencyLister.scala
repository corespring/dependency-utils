package com.ahum

object DependencyLister {

  /** find all the deps */
  def dependencyList[T](d: T, graph: Seq[(T, Seq[T])]): Seq[T] = {

    def find(deps: Seq[T], acc: Seq[T]): Seq[T] = {

      deps match {
        case Nil => acc
        case head :: rest => {
          if (acc.contains(head)) {
            find(depsFor(head), acc)  ++ find(rest, acc)
          } else {
            find(depsFor(head), (acc :+ head)) ++ find(rest, acc)
          }
        }
      }
    }

    def depsFor(dep: T): Seq[T] = {
      graph.find(_._1 == dep).map(_._2).getOrElse(Seq.empty)
    }

    find(depsFor(d), Seq(d)).distinct
  }

}
