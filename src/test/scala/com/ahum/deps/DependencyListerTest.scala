package com.ahum.deps

import org.specs2.mutable.Specification

class DependencyListerTest extends Specification {

  "DependencyLister" should {

    "work" in {


      val graph = Seq(
        (1 -> Seq(2)),
        (2 -> Seq(3, 4)),
        (3 -> Seq()),
        (4 -> Seq(5)),
        (5 -> Seq(6)),
        (6 -> Seq(7))
      )
      DependencyLister.dependencyList(1, graph) === List(1, 2, 3, 4, 5, 6, 7)
      DependencyLister.dependencyList(4, graph) === List(4, 5, 6, 7)
      DependencyLister.dependencyList(6, graph) === List(6, 7)
      DependencyLister.dependencyList(7, graph) === List(7)
      DependencyLister.dependencyList(8, graph) === List(8)
    }

    "list" in {


      DependencyLister.list[String](Branch("1", Seq(Leaf("2")))) === Seq("1", "2")
    }

    "list 2" in {
      DependencyLister.list[String](
        Branch("1",
          Seq(
            Branch("2",
              Seq(
                Leaf("3"),
                Branch("4",
                  Seq(
                    Leaf("5")
                  )
                )
              )
            )
          )
        )
      ).sorted === Seq("1", "2", "3", "4", "5")
    }
  }
}
