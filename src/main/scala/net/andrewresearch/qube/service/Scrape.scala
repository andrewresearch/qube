package net.andrewresearch.qube.service

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.{WebDriver, By, WebElement}
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._

/**
 * Created by andrew on 19/10/2015.
 */

@Component
class LensScrape {

  def logger = LoggerFactory.getLogger(this.getClass)

  def forReference(reference:String) = {
    logger.info("Starting Lens Scrape for: "+reference)
    val summaryDoc = new WebDoc("https://www.lens.org/lens/patent/"+reference)
    val title = summaryDoc.title
    val summary = summaryDoc.elementWithId("patentFrontPage")
      .firstElementWithClassName("last")
      .firstElementWithTagName("p")
      .text

    summaryDoc.close

    val fullTextDoc = new WebDoc("https://www.lens.org/lens/patent/"+reference+"/fulltext")
    val fullText = fullTextDoc.elementWithId("contents")
      .allElementsAtPath("div[@class='para_text']").map(_.getText).toList
    PatentData(reference,title,summary,fullText)
  }

  case class PatentData(reference:String,title:String,summary:String,fullText:List[String])
}

class WebDoc(url:String) {
  var driver:WebDriver = new HtmlUnitDriver()
  driver.get(url)
  var currentElement:WebElement = driver.findElement(By.tagName("html"))

  def text = currentElement.getText
  def title = driver.getTitle
  def element = currentElement
  def close = driver.quit()

  def elementWithId(id:String) = {
    currentElement = driver.findElement(By.id(id))
    this
  }

  def firstElementWithTagName(tagName:String) = {
    currentElement = currentElement.findElement(By.tagName(tagName))
    this
  }
  def firstElementWithClassName(className:String) = {
    currentElement = currentElement.findElement(By.className(className))
    this
  }
  def firstElementAtPath(path:String) = {
    currentElement = currentElement.findElement(By.xpath(path))
    this
  }

  def allElementsWithTagName(tagName:String) = {
    currentElement.findElements(By.tagName(tagName)).asScala
  }
  def allElementsWithClassName(className:String) = {
    currentElement.findElements(By.className(className)).asScala
  }
  def allElementsAtPath(path:String) = {
    currentElement.findElements(By.xpath(path)).asScala
  }

}
