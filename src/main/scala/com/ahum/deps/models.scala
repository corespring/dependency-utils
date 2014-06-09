package com.ahum.deps


sealed trait Node[T]

case class EdgeNode[T](id: T) extends Node[T]

case class DepNode[T](id: T, dependencies: Seq[Node[T]])
