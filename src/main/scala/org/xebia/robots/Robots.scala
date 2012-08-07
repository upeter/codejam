package org.xebia.robots
import java.io.OutputStream


 object TestFileParser {
  def parse(fileIn: String, fileOut:OutputStream)(handlePlan:List[(String, Int)] => Int) = {
    val source = scala.io.Source.fromInputStream(getClass().getResourceAsStream(fileIn)).getLines
    source.drop(1)
    val results = source.takeWhile(_ != "").map { line =>
      val plan = parseLine(line)
      handlePlan(plan)
    }
    val result = results.toList.zipWithIndex.map{case (res, tst) => "Case #%s: %s" format (tst + 1, res)}.mkString("\n")
    fileOut.write(result.getBytes())
  }

  private def parseLine(line: String) = line.split(" ").tail.sliding(2, 2).toList.map(a => (a(0), a(1).toInt))

}

case class Robots(plan: List[(String, Int)], robots: Robot*) {

  def solve = plan.foldLeft(0) { case (moves, (color, nextPos)) => moves + moveToAndPress(color, nextPos) }

  private def moveToAndPress(color: String, nextPos: Int) = {
    val botToMove = hasColor(color)
    val steps = botToMove.stepsToNextButton
    val otherBots = hasNotColor(color)
    botToMove.moveAndPress(steps)
    val totalSteps = steps + 1
    otherBots.foreach(_.move(totalSteps))
    totalSteps
  }

  val colorToBotMap = robots.map(r => (r.color, r)).toMap
  def hasColor(color: String): Robot = colorToBotMap(color)
  def hasNotColor(color: String) = colorToBotMap.values.filterNot(_.color == color)
}

object Robots {
  def apply(plan: List[(String, Int)]): Robots = {
    val robots = plan.groupBy(_._1).values.map(robotPlan => Robot(robotPlan.head._1, 1, robotPlan.map(_._2))).toArray
    Robots(plan, robots: _*)
  }
}

case class Robot(color: String, var currentPos: Int, var btnPos: List[Int]) {
  var nextBtnPos = calcNextBtnPos
  this.btnPos = calcRemainingPos

  def moveAndPress(steps:Int) = {
	require(calcNewCurrentPos(steps) == nextBtnPos)
    move(steps)
    this.nextBtnPos = calcNextBtnPos
    this.btnPos = calcRemainingPos
    this
  }

  def move(steps: Int) = {
    this.currentPos = calcNewCurrentPos(steps)
    this
  }

  def stepsToNextButton = (currentPos - nextBtnPos).abs

  private def calcNextBtnPos = btnPos.headOption.getOrElse(-1)
  private def calcRemainingPos = btnPos.drop(1)
  private def calcNewCurrentPos(steps: Int) = if (currentPos < nextBtnPos) {
    if (currentPos + steps > nextBtnPos) nextBtnPos else currentPos + steps
  } else if (currentPos > nextBtnPos) {
    if (currentPos - steps < nextBtnPos) nextBtnPos else currentPos - steps
  } else nextBtnPos

}

