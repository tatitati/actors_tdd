package threading

import org.scalatest.FunSuite

class Synchronized2Spec extends FunSuite {
  test("A Thread.sleep inside of a synchronized doesnt produce a change to another thread becauase it owns the lock still") {
    var num = 0
    def workerIncrement(): Int = {
      this.synchronized{ // we changed only this line
        println("Thread Id: " + Thread.currentThread.getId +" started")
        Thread.sleep(7700)
        println("Thread Id: " + Thread.currentThread.getId +" finished")
        for (_ <- 1 to 100000) {
          num = num + 1
        }
        num
      }
    }

    val t1 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t2 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t3 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t4 = new Thread{
      override def run(): Unit = workerIncrement()
    }

    val threads = List(t1, t2, t3, t4)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // this Thread id before waiting: 155
    // this Thread id after waiting: 155
    // this Thread id before waiting: 159
    // this Thread id after waiting: 159
    // this Thread id before waiting: 158
    // this Thread id after waiting: 158
    // this Thread id before waiting: 157
    // this Thread id after waiting: 157
    // this Thread id before waiting: 156
    // this Thread id after waiting: 156
    // 400000
  }
}