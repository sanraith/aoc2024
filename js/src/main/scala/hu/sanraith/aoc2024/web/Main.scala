package hu.sanraith.aoc2024.web

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.*
import hu.sanraith.aoc2024.solution.Greeter

// import typescript logo from "/typescript.svg"
@js.native @JSImport("/typescript.svg", JSImport.Default)
val typescriptLogo: String = js.native

@main
def Main(): Unit =
  val helloClass = Greeter("Sanraith")
  dom.document.querySelector("#app").innerHTML = s"""
    <div>
      <a href="https://vitejs.dev" target="_blank">
        <img src="/vite.svg" class="logo" alt="Vite logo" />
      </a>
      <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank">
        <img src="$typescriptLogo" class="logo vanilla" alt="JavaScript logo" />
      </a>
      <h1>${helloClass}</h1>
      <div class="card">
        <button id="counter" type="button"></button>
      </div>
      <p class="read-the-docs">
        Click on the Vite logo to learn more
      </p>
    </div>
  """

  setupCounter(dom.document.getElementById("counter"))

def setupCounter(element: dom.Element): Unit =
  var counter = 0

  def setCounter(count: Int): Unit =
    counter = count
    element.innerHTML = s"count is $counter"

  element.addEventListener("click", e => setCounter(counter + 1))
  setCounter(0)
