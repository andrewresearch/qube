package net.andrewresearch.qube.service

import net.andrewresearch.qube.data.{PatentData, PatentDataRepo}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.{WebDriver, By, WebElement}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._
import scala.collection.mutable.{ListBuffer, Buffer}
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
    val fullTextElements = fullTextDoc.elementWithId("contents")
      .allElementsAtPath("div[@class='para_text']")
    val fullTextWithHeaders:Map[String,java.util.List[String]] = getTextWithHeaders(fullTextElements)
    PatentData(reference,title,summary, fullTextWithHeaders.asJava) //fullTextElements.map(_.getText).toList.asJava)


  }

  def getTextWithHeaders(elements:Buffer[WebElement]) = {
    var textWithHeaders = Map[String,java.util.List[String]]()
    var currentHeader = ""
    var currentLines = ListBuffer[String]()
    elements.foreach { element =>
      val header = element.findElements(By.xpath("preceding-sibling::h2")).asScala.last.getText
      if (!currentHeader.contentEquals(header)) {
        if (currentLines.nonEmpty) {
          //logger.debug("Adding list to map: "+currentLines.toString)
          textWithHeaders += currentHeader -> currentLines.toList.asJava
        }
        //logger.debug("New Header: "+header)
        currentHeader = header
        currentLines = ListBuffer.empty
      }
      currentLines += element.getText
    }
    if (currentLines.nonEmpty) textWithHeaders += currentHeader -> currentLines.toList.asJava
    textWithHeaders
  }

  def getClaims = {

    //Check for table_data class - the <h3> tag has the name of this claim
    //Get the para_text then <a name=""> for claim number and class="claim_text" for the text of the claim

  }


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
