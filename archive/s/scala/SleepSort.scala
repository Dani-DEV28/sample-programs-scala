import scala.collection.mutable.ListBuffer
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._

object SleepSort {
  def main(args: Array[String]): Unit = {
    var result = invalidChecker(args)
      
    if(!result.contains("true")){ // After going through checker, it will output result to procede with SleepSort or not
      println(result)
    } else {
      val numbers = args.flatMap(_.split(",")).map(_.trim).filter(_.nonEmpty).map(_.toInt)
      println(sleepSort(numbers))
    }
  }
    

  // Checking for formating, empty array, and Non-numeric values
  def invalidChecker(args: Array[String]): String = args match {
    case null | Array() => "No Input"
    case arr if arr.forall(_.isEmpty) =>  "Empty Input"
    case arr if arr.forall(_.length == 1) && arr.length == 1 => "Invalid Input: Not A List"
    case arr if !arr.exists(_.contains(",")) => "Invalid Input: Wrong Format"
    case _ => {
      val numbers = args.flatMap(_.split(",")).map(_.trim).filter(_.nonEmpty)
      
      if (numbers.forall(n => n.forall(_.isDigit))) {
        "true" // <- passing true to procede with SleepSort
      } else {
        "Invalid Input: Non-numeric Values"
      }
    }
  }

  // delaying time to add a value to list base on it weight
  def sleepSort(args: Array[Int]): String = {
    val delayTimer: Long = 1000L
    val outputArray = ListBuffer[String]()

    val futures = args.map { num =>
      Future {
        Thread.sleep(num * delayTimer)
        synchronized { // Ensure thread safety
          outputArray += num.toString
        }
      }
    }

    Await.result(Future.sequence(futures), Duration.Inf) // intializing the data into outputArray
    outputArray.mkString(", ")
  }
}
