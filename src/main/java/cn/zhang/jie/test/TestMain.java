package cn.zhang.jie.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class TestMain {
	
	public static void main(String[] args) {
//		m1();
//		m2();
		m3();
	}
	
	
	/**
	 * 插入单个文档、文档中包含嵌套文档
	 */
	public static void m1() {
		//没有开启数据库认证的情况下
		MongoClient mongoClient = new MongoClient("192.168.1.45", 27017);
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("user");
		
		//插入数据（单个文档）
		Document document = new Document("name","user0")
				.append("contact", new Document("phone","228-55-0149"))		//包含嵌套文档
				.append("email", "user@example.com")
				.append("location",Arrays.asList(-73.92502,40.8279556))
				.append("age", 18)
				.append("likeNum", Arrays.asList(8,10));
		collection.insertOne(document);
		mongoClient.close();
	}
	
	
	/**
	 * 插入多个文档
	 */
	public static void m2() {
		//没有开启数据库认证的情况下
		MongoClient mongoClient = new MongoClient("192.168.1.45", 27017);
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("user");
		
		List<Document> list = new ArrayList<Document>();
		for (int i = 0; i < 10; i++) {
			String userName = "user"+i;
			Document document = new Document("name",userName)
					.append("contact", new Document("phone","228-555-0149"+i))
					.append("email",userName+"@example.com")
					.append("location", Arrays.asList(-72566+i,40.23551))
					.append("age",18+i);
			list.add(document);
		};
		collection.insertMany(list);
		mongoClient.close();
	}
	
	
	/**
	 * 查询数据
	 */
	public static void m3() {
		//没有开启数据库认证的情况下
		MongoClient mongoClient = new MongoClient("192.168.1.45", 27017);
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("user");
		
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> iterable = findIterable.iterator();
		System.out.println("---------查询所有---------");
		while(iterable.hasNext()) {
			Document doc = iterable.next();
			System.out.println(doc.get("name"));
		}
		System.out.println("---------与查询---------");
		findIterable = collection.find(new Document("age",new Document("$gte",20).append("$lt",21)).append("name", "user2"));
		iterable = findIterable.iterator();
		while(iterable.hasNext()) {
			Document doc = iterable.next();
			System.out.println(doc.get("name"));
		}
		System.out.println("---------与查询2---------");
		findIterable = collection.find(Filters.and(Filters.gte("age", 20),Filters.lt("age", 21),Filters.eq("name", "user2")));
		iterable = findIterable.iterator();
		while(iterable.hasNext()) {
			Document doc = iterable.next();
			System.out.println(doc.get("name"));
		}
		System.out.println("---------或查询---------");
		findIterable = collection.find(Filters.or(Filters.eq("age",18),Filters.eq("age",19)));
		iterable = findIterable.iterator();
		while(iterable.hasNext()) {
			Document doc = iterable.next();
			System.out.println(doc.get("name"));
		}
		System.out.println("---------模糊查询---------");
		Pattern pattern = Pattern.compile("user");
		BasicDBObject query = new BasicDBObject("name",pattern);
		findIterable = collection.find(query);
		iterable = findIterable.iterator();
		while(iterable.hasNext()) {
			Document doc = iterable.next();
			System.out.println(doc.get("name"));
		}		
		mongoClient.close();
	}
	
	
	/**
	 * 更新数据
	 */
	public static void m4() {
		
	}
}
