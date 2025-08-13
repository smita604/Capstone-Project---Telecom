package Telecom;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
public class UserManagement {

	String token;
	
	
String baseUrl = "https://thinking-tester-contact-list.herokuapp.com";

@BeforeClass
public void setup() {
    RestAssured.baseURI = baseUrl;
}
	
	@Test(priority=1)
	 public void createNewUser() {
		
		 String payload = """
	                {
	                    "firstName": "Aruna",
	                    "lastName": "Gutthal",
	                    "email": "Gutthal14273@gmail.com",
	                    "password": "smart@123"
	                }
	                """;
		
		Response res =  given().header("Content-Type", "application/json").body(payload).when().post("/users");
	
		Assert.assertEquals(res.getStatusCode(), 201);
		Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 201 Created");
		  System.out.println("Response code"+ res.getStatusCode());
		  System.out.println("Response Message is "+res.getStatusLine());
		  res.then().log().body();
		  System.out.println("New user added successfully");		  
		   token = res.jsonPath().getString("token");
	     //  System.out.println("Token: " + token);

}
	@Test(priority=2, dependsOnMethods = "createNewUser")
	public void getUserProfile() {
		System.out.println(token);
		
		Response res = given()
                .header("Authorization", "Bearer " + token)
                .get("/users/me");
		Assert.assertEquals(res.getStatusCode(), 200);
		Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
		  System.out.println("Response code"+ res.getStatusCode());
		  System.out.println("Response Message is "+res.getStatusLine());
		  res.then().log().body();
		
	}
	@Test(priority=3, dependsOnMethods = "createNewUser")
	public void UpdateUser() {
		 String payload = """
	                {
		 		"firstName": "Vinod1",
		 		 "lastName": "Kumat",
		 		"email": "kamat111@gmail.com",
		 		"password": "smart@123"
}
	                """;
		 
		 System.out.println(token);

	        Response res = given()
	                .header("Authorization", "Bearer " + token)
	                .header("Content-Type", "application/json")
	                .body(payload)
	                .patch("/users/me");
	        
	         Assert.assertEquals(res.getStatusCode(), 200);
			 Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
			  System.out.println("Response code"+ res.getStatusCode());
			  System.out.println("Response Message is "+res.getStatusLine());
			  res.then().log().body();
			  System.out.println("User details updated successfully");
	}
}