package net.andrewresearch.qube

/**
 * Created by andrew on 19/10/2015.
 */


import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application

object Application extends App {

  SpringApplication.run(classOf[Application])
}