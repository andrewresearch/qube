package net.andrewresearch.qube.config

import java.util

import com.mongodb.{Mongo, ServerAddress, MongoCredential, MongoClient}

//import com.mongodb.{MongoCredential, ServerAddress, Mongo, MongoClient}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * Created by andrew on 24/01/15.
 */
@Configuration
class MongoConfig extends AbstractMongoConfiguration {

  @Autowired private var env: Environment = null

  def logger = LoggerFactory.getLogger(this.getClass)

  @Override
  protected def getDatabaseName: String = {
    //logger.debug("Getting database name")
    env.getProperty("db.database")
  }

  @Override
  @Bean
  def mongo: Mongo = {
    val server = new ServerAddress(env.getProperty("db.host"), Integer.valueOf(env.getProperty("db.port")))
    var credentials: util.ArrayList[MongoCredential] = new util.ArrayList()
    credentials.add(MongoCredential.createCredential(env.getProperty("db.username"), env.getProperty("db.database"), env.getProperty("db.password").toCharArray))
    new MongoClient(server, credentials)
  }
}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class JsonConfig {

  def logger = LoggerFactory.getLogger(this.getClass)

  @Bean
  def objectMapper:MappingJackson2HttpMessageConverter = {
    val mapping = new MappingJackson2HttpMessageConverter
    mapping.setObjectMapper(new ScalaObjectMapper)
    mapping
  }

  class ScalaObjectMapper extends ObjectMapper  {
    {
      registerModule(DefaultScalaModule)
    }
  }

}



