package net.andrewresearch.qube.controller

import net.andrewresearch.qube.data.PatentDataRepo
import net.andrewresearch.qube.service.{LensScrape}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * Created by andrew on 19/10/2015.
 */

@RestController
class HomeController {

  @Autowired var lensScrape:LensScrape = null
  @Autowired var pdRepo:PatentDataRepo = null

  def logger = LoggerFactory.getLogger(this.getClass)

  @RequestMapping(value=Array("/home"),method=Array(RequestMethod.GET))
  def home = {
    logger.info("Home Controller")
    "Welcome to QuBE"
  }

  @RequestMapping(value=Array("/scrape/patent/{ref}"),method=Array(RequestMethod.POST))
  def scrapePatent(@PathVariable ref:String) = {
    //logger.info("Testing Selenium Scrape")
    var result = lensScrape.forReference(ref)
    pdRepo.save(result)
    result
  }

  @RequestMapping(value=Array("/scrape/patent/list"),method=Array(RequestMethod.POST))
  def scrapePatent(@RequestBody refs:List[String]) = {
    //logger.info("Testing Selenium Scrape")
    val startTime = System.currentTimeMillis
    refs.foreach { ref =>
      var result = lensScrape.forReference(ref)
      pdRepo.save(result)
    }
    val completed = "Saved "+refs.size+" to database. Finished in " + ((System.currentTimeMillis() - startTime).toDouble / 1000) + " seconds."
    Map("result"->completed,"patents"->refs)
  }

  @RequestMapping(value=Array("/patent/{ref}"),method=Array(RequestMethod.GET))
  def getPatent(@PathVariable ref:String) = {
    pdRepo.findOne(ref)
  }

  @RequestMapping(value=Array("/patent/all"),method=Array(RequestMethod.GET))
  def getAllPatents = {
    pdRepo.findAll()
  }


}