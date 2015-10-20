package net.andrewresearch.qube.controller

import net.andrewresearch.qube.service.{LensScrape}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, RequestMethod, RequestMapping, RestController}

/**
 * Created by andrew on 19/10/2015.
 */

@RestController
class HomeController {

  @Autowired var lensScrape:LensScrape = null

  def logger = LoggerFactory.getLogger(this.getClass)

  @RequestMapping(value=Array("/home"),method=Array(RequestMethod.GET))
  def home = {
    logger.info("Home Controller")
    "Welcome to QuBE"
  }

  @RequestMapping(value=Array("/scrape/{ref}"),method=Array(RequestMethod.GET))
  def scrape(@PathVariable ref:String) = {
    logger.info("Testing Selenium Scrape")
    lensScrape.forReference(ref)
  }

}