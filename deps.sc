

/** find all the deps */
def deps[T](d : (T,Seq[T])*) : Seq[T] = {
  Seq.empty
}




deps((1 -> Seq(2)), 2 -> Seq())