package com.ahum.deps

trait Node[T]

case class Leaf[T](id: T) extends Node[T]

case class Branch[T](id: T, deps: Seq[Node[T]]) extends Node[T]

object DependencyLister {


  def list[T](n: Node[T]): Seq[T] = {

    def innerList(node: Node[T], acc: Seq[T]): Seq[T] = {
      node match {
        case Leaf(id) => {
          acc :+ id
        }
        case Branch(id, deps) => {
          (acc :+ id) ++ deps.foldRight[Seq[T]](Seq()) { (innerNode, acc) =>
            innerList(innerNode, acc)
          }
        }
      }
    }
    innerList(n, Seq())
  }

  /** find all the deps */
  def dependencyList[T](d: T, graph: Seq[(T, Seq[T])]): Seq[T] = {

    def find(deps: Seq[T], acc: Seq[T]): Seq[T] = {

      deps match {
        case Nil => acc
        case head :: rest => {
          if (acc.contains(head)) {
            find(depsFor(head), acc) ++ find(rest, acc)
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
