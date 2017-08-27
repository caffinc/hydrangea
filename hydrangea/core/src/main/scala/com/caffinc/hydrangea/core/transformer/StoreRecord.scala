package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.mongodb.{MongoClient, MongoClientURI}
import org.bson.Document
import org.json4s.mongo.JObjectParser

/**
  * Stores the KafkaRecord into a persistent storage (MongoDB)
  *
  * @author Sriram
  */
object StoreRecord extends Transformer[KafkaRecord, (String, String, String)] {
  private val client: MongoClient = new MongoClient(new MongoClientURI(transformerConfig.getString("storerecord.mongo.uri")))

  def apply(record: KafkaRecord): (String, String, String) = transform(record)

  override def transform(record: KafkaRecord): (String, String, String) = {
    val document = getDocument(record)
    val db = record.topic
    val collection = record.recordType
    store(db, collection, document)
    (db, collection, record.key)
  }

  /**
    * Transforms a KafkaRecord into a Document to write to the DB
    *
    * @param record KafkaRecord to transform
    * @return Document object
    */
  def getDocument(record: KafkaRecord): Document = {
    // TODO Use Mongo Scala Drivers
    val obj = new Document()
    obj.putAll(JObjectParser.parse(record.value).asInstanceOf[java.util.Map[String, _]])
    obj.put("_id", record.key)
    obj.put("ts", record.timestamp)
    obj
  }

  /**
    * Stores the document into the database
    *
    * @param db         Database to store into
    * @param collection Collection to store into
    * @param document   Document to store
    */
  def store(db: String, collection: String, document: Document): Unit = {
    // TODO: ConcurrentPeriodicBuffered storage
    client.getDatabase(db)
      .getCollection(collection).insertOne(document)
  }
}
