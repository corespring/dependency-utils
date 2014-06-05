package com.ahum.deps

import org.specs2.mutable.Specification

class TopologicalSorterSpec extends Specification {

  import TopologicalSorter.sort

  "Topological Sorter" should {

    "sort throws an error if it can't find an edge" in {
      sort(1 -> Seq(2), 2 -> Seq(3,1), 3 -> Seq()) must throwA[RuntimeException]
    }

    "simple: sort throws an error if it can't find an edge" in {
      sort(1 -> Seq(2), 2 -> Seq(1)) must throwA[RuntimeException]
    }

    "sorts" in {
      sort(
        1 -> Seq(2),
        2 -> Seq(3),
        3 -> Seq.empty) ===
        List((3,List()), (2,List(3)), (1,List(2)))
    }

    "diamond" in {
      sort(
        1 -> Seq(2, 3),
        2 -> Seq(4),
        3 -> Seq(4),
        4 -> Seq.empty) ===
        List(
          4 -> Seq.empty,
          3 -> Seq(4),
          2 -> Seq(4),
          1 -> Seq(2,3))
    }

    "sorts undefined nodes" in {
      sort(1 -> Seq(2,3)) ===
        List(
          2 -> Seq.empty,
          3 -> Seq.empty,
          1 -> Seq(2,3)
        )
    }
  }
}
