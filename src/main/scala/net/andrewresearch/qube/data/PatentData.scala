package net.andrewresearch.qube.data

/**
 * Created by andrew on 23/10/2015.
 */

import java.util

//import org.bson.types.ObjectId
import org.springframework.data.repository.PagingAndSortingRepository

case class PatentData(id:String,title:String,summary:String,fullText:util.Map[String,util.List[String]])

trait PatentDataRepo extends PagingAndSortingRepository[PatentData,String]