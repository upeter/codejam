package org.xebia.robots

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ BeforeAndAfterEach, FlatSpec }
import java.io.OutputStream

@RunWith(classOf[JUnitRunner])
class RobotsTests extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {
 
  override def beforeEach() {
  }
  it should "calc steps to next button correctly" in {
    val seq = List(5, 4, 6, 9)
    val r = Robot("O", 1, seq)
    r.stepsToNextButton should be(4)
  }
  it should "correctly change state for move" in {
    val buttonPos = List(5, 2, 6, 9)
    val r = Robot("O", 1, buttonPos)
    r.move(3).currentPos should be(4)
    r.moveAndPress(3).currentPos should be(5)
    r.move(1).currentPos should be(4)
    r.move(10).currentPos should be(2)
  }
  it should "correctly change state for moveAndPressButton" in {
    val buttonPos = List(5, 1)
    val r = Robot("O", 1, buttonPos)
    intercept[IllegalArgumentException] {
      r.moveAndPress(2)
    }
    r.moveAndPress(4)
    r.nextBtnPos should be(1)
    r.moveAndPress(10)
    r.nextBtnPos should be(-1)
  }
  it should "init robots correctly" in {
    val plan = ("O" -> 2) :: ("B" -> 1) :: ("B" -> 2) :: ("O" -> 4) :: Nil
    val robots = Robots(plan)
    val rO = robots.hasColor("O")
    rO.nextBtnPos should be(2)
    rO.btnPos should be(List(4))
    val rB = robots.hasNotColor("O").head
    rB.nextBtnPos should be(1)
    rB.btnPos should be(List(2))
  }

  it should "solve puzzel" in {
    val plan1 = ("O" -> 2) :: ("B" -> 1) :: ("B" -> 2) :: ("O" -> 4) :: Nil
    val plan2 = ("B" -> 2) :: ("B" -> 1) :: Nil
    val plan3 = ("O" -> 5) :: ("O" -> 8) :: ("B" -> 100) :: Nil
    Robots(plan1).solve should be(6)
    Robots(plan2).solve should be(4)
    Robots(plan3).solve should be(100)
  }
  it should "Parse file" in {
    TestFileParser.parse("/A-small-practice.in", System.out) {plan => Robots(plan).solve}
    TestFileParser.parse("/A-large-practice.in", System.out){plan => Robots(plan).solve}
    true
  }

}


