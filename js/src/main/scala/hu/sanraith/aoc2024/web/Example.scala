package hu.sanraith.aoc2024.web

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.*
import hu.sanraith.aoc2024.solution.Day01
import hu.sanraith.aoc2024.solution.Context
import com.raquo.laminar.api.L.{*, given}
import scala.util.Random

// import typescript logo from "/typescript.svg"
@js.native
@JSImport("/typescript.svg", JSImport.Default)
val typescriptLogo: String = js.native

object Example {
  val model = new Model
  import model.*

  def appElement(): Element =
    val day01 = Day01()
    val result = day01.part2(SimpleContext("4nineeightseven2")).toString()
    div(h1("Live Chart"), h2("Result: ", result), renderDataTable(), renderDataList())

  def renderDataList(): Element = ul(
    children <--
      dataSignal.split(_.id) { (id, initial, itemSignal) =>
        li(child.text <-- itemSignal.map(item => s"${item.count} ${item.label}"))
      }
  )
  end renderDataList

  def renderDataTable(): Element = table(
    thead(tr(th("Label"), th("Price"), th("Count"), th("Full price"), th("Action"))),
    tbody(
      children <--
        dataSignal.split(_.id) { (id, initial, itemSignal) =>
          renderDataItem(id, itemSignal)
        }
    ),
    tfoot(
      tr(
        td(button("➕", onClick --> (_ => addDataItem(DataItem())))),
        td(),
        td(),
        td(child.text <-- dataSignal.map(data => "%.2f".format(data.map(_.fullPrice).sum)))
      )
    )
  )
  end renderDataTable

  def renderDataItem(id: DataItemID, itemSignal: Signal[DataItem]): Element = tr(
    td(
      inputForString(
        itemSignal.map(_.label),
        makeDataItemUpdater(
          id,
          { (item, newLabel) =>
            item.copy(label = newLabel)
          }
        )
      )
    ),
    td(
      inputForDouble(
        itemSignal.map(_.price),
        makeDataItemUpdater(
          id,
          { (item, newPrice) =>
            item.copy(price = newPrice)
          }
        )
      )
    ),
    td(
      inputForInt(
        itemSignal.map(_.count),
        makeDataItemUpdater(
          id,
          { (item, newCount) =>
            item.copy(count = newCount)
          }
        )
      )
    ),
    td(child.text <-- itemSignal.map(item => "%.2f".format(item.fullPrice))),
    td(button("🗑️", onClick --> (_ => removeDataItem(id))))
  )
  end renderDataItem

  def inputForString(valueSignal: Signal[String], valueUpdater: Observer[String]): Input = input(
    typ := "text",
    value <-- valueSignal,
    onInput.mapToValue --> valueUpdater
  )
  end inputForString

  def inputForDouble(valueSignal: Signal[Double], valueUpdater: Observer[Double]): Input = {
    val strValue = Var[String]("")
    input(
      typ := "text",
      value <-- strValue.signal,
      onInput.mapToValue --> strValue,
      valueSignal -->
        strValue.updater[Double] { (prevStr, newValue) =>
          if prevStr.toDoubleOption.contains(newValue) then prevStr
          else {
            newValue.toString
          }
        },
      strValue.signal --> { valueStr =>
        valueStr.toDoubleOption.foreach(valueUpdater.onNext)
      }
    )
  }
  end inputForDouble

  def inputForInt(valueSignal: Signal[Int], valueUpdater: Observer[Int]): Input = input(
    typ := "text",
    controlled(
      value <-- valueSignal.map(_.toString),
      onInput.mapToValue
        .map(_.toIntOption)
        .collect { case Some(newCount) =>
          newCount
        } --> valueUpdater
    )
  )
  end inputForInt

  def counterButton(): Element = {
    val counter = Var(0)
    button(
      tpe := "button",
      "count is ",
      child.text <-- counter,
      onClick --> { event =>
        counter.update(c => c + 1)
      }
    )
  }
}

/** Model */

final class DataItemID

case class DataItem(id: DataItemID, label: String, price: Double, count: Int):
  def fullPrice: Double = price * count

object DataItem:
  def apply(): DataItem = DataItem(DataItemID(), "?", Random.nextDouble(), Random.nextInt(5) + 1)
end DataItem

type DataList = List[DataItem]

final class Model:
  val dataVar: Var[DataList] = Var(List(DataItem(DataItemID(), "one", 1.0, 1)))
  val dataSignal = dataVar.signal

  def addDataItem(item: DataItem): Unit = dataVar.update(data => data :+ item)

  def removeDataItem(id: DataItemID): Unit = dataVar.update(data => data.filter(_.id != id))

  def makeDataItemUpdater[A](id: DataItemID, f: (DataItem, A) => DataItem): Observer[A] = dataVar
    .updater { (data, newValue) =>
      data.map { item =>
        if item.id == id then f(item, newValue)
        else {
          item
        }
      }
    }
  end makeDataItemUpdater
end Model
