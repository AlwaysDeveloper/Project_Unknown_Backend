package databases;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConnection {

    public MongoClient MongoConnection() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&ssl=false");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).retryWrites(true).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient;
    }
}
