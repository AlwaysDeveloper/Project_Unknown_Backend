package databases;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    MongoDatabase mongoDatabase;
    public MongoConnection(String database) {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&ssl=false");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).retryWrites(true).build();
        MongoClient mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public MongoCollection getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }
}
