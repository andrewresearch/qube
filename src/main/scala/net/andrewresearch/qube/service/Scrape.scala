package net.andrewresearch.qube.service

import com.mongodb.Mongo
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait, ExpectedCondition}
import org.openqa.selenium.{WebDriver, By, WebElement}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._
/**
 * Created by andrew on 19/10/2015.
 */

@Component
class SeleniumScrape {

  @Autowired var mongo:Mongo = null

  val driver = new HtmlUnitDriver()

  def logger = LoggerFactory.getLogger(this.getClass)

  def getPage(url:String) = driver.get(url)
  def getTitle = driver.getTitle

  def getElementById(elementId:String):WebElement = driver.findElementById(elementId)

  def typeInElement(element:WebElement,text:String) = element.sendKeys(text)
  def submitForm(element:WebElement) = element.submit()

  def close = driver.quit()

  def lensTest(reference:String) = {
    logger.info("Starting Lens Test")
    getPage("https://www.lens.org/lens/patent/"+reference)
    val frontPage = getElementById("patentFrontPage")
    val abs = frontPage.findElement(By.className("last")).findElements(By.xpath("p")).asScala.map(_.getText)
    logger.debug("Found "+abs.size+" elements")
    val summary = abs.mkString(" \r")
    val title = getTitle
    logger.info("Page title is: "+title)
    //close
    val newData = PatentData(reference,title,summary)

  }

  case class PatentData(reference:String,title:String,summary:String)

}
