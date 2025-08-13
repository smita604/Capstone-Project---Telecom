package Telecom;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
public class ContactManagement {

	String token;
	List<String> contactIds;
	String contactID;
	String baseUrl = "https://thinking-tester-contact-list.herokuapp.com";

@BeforeClass
public void setup() {
    RestAssured.baseURI = baseUrl;
}
	
	
	@Test(priority=1)
	public void loginUser() {
		
		 String payload = """
	                {
	                    "email": "npatil604@gmail.com",
	                    "password": "smart@123"
	                }
	                """;

	        Response res = given()
	                .header("Content-Type", "application/json")
	                .body(payload)
	                .post("/users/login");
	    	Assert.assertEquals(res.getStatusCode(), 200);
			Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
			  System.out.println("Response code"+ res.getStatusCode());
			  System.out.println("Response Message is "+res.getStatusLine());
			  token = res.jsonPath().getString("token");
			  System.out.println(token);
			  res.then().log().body();
			  System.out.println("User loggedin successfully");
	}
		 

	 @Test(priority = 2, dependsOnMethods = "loginUser")
	    public void addContact() {
	        String payload = """
	                {
	                    "firstName": "Suphie",
	                    "lastName": "Mark",
	                    "birthdate": "1990-07-15",
	                    "email": "Suphie@example.com",
	                    "phone": "12378567890",
	                    "street1": "1234 Main St",
	                    "city": "Metropolis1",
	                    "stateProvince": "NY",
	                    "postalCode": "12345",
	                    "country": "USA"
	                }
	                """;

	        Response res = given()
	                .header("Authorization", "Bearer " + token)
	                .header("Content-Type", "application/json")
	                .body(payload)
	                .post("/contacts");

	        Assert.assertEquals(res.getStatusCode(), 201);
			Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 201 Created");
			  System.out.println("Response code"+ res.getStatusCode());
			  System.out.println("Response Message is "+res.getStatusLine());
			  res.then().log().body();
			  System.out.println("New contact added successfully");		  
			   
	    }
	 
	 @Test(priority =3, dependsOnMethods = "loginUser")
	    public void getContactList() {
	        Response res = given()
	                .header("Authorization", "Bearer " + token)
	                .get("/contacts");
	        Assert.assertEquals(res.getStatusCode(), 200);
			Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
			  System.out.println("Response code"+ res.getStatusCode());
			  System.out.println("Response Message is "+res.getStatusLine());
			  contactIds = res.jsonPath().getList("_id");
			  contactID=contactIds.get(0);
			 System.out.println(contactID);
			  System.out.println("Contact list are:");
			  res.then().log().body();
}
	 

@Test(priority = 4, dependsOnMethods = {"loginUser","getContactList"}) 
 public void getContact()
{ 
	System.out.println("ContactID is"+contactID);
	Response res = 
			given() .header("Authorization", "Bearer "+ token)
			.get("/contacts/" + contactID);
  Assert.assertEquals(res.getStatusCode(), 200);
 Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
 System.out.println("Response code"+ res.getStatusCode());
 System.out.println("Response Message is "+res.getStatusLine());
 res.then().log().body();
   
  }
  

 @Test(priority = 5, dependsOnMethods = {"loginUser","getContactList"})
 public void updateFullContact() {
     String payload = """
             {
                 "firstName": "Robert",
                 "lastName": "Stump",
                 "birthdate": "1990-05-15",
                 "email": "john.smith@example.com",
                 "phone": "99987899999",
                 "street1": "456 Elm St",
                 "city": "Gotham",
                 "stateProvince": "NJ",
                 "postalCode": "54321",
                 "country": "USA"
             }
             """;

     Response res = given()
             .header("Authorization", "Bearer " + token)
             .header("Content-Type", "application/json")
             .body(payload)
             .put("/contacts/" + contactID);
     	 Assert.assertEquals(res.getStatusCode(), 200);
     	 Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
		  System.out.println("Response code"+ res.getStatusCode());
		  System.out.println("Response Message is "+res.getStatusLine());
		  Assert.assertEquals(res.jsonPath().getString("lastName"), "Stump", "lastName does not match");
		  System.out.println("Full contact updated successfully");
		  res.then().log().body();
}
 
 @Test(priority = 6, dependsOnMethods = {"loginUser","getContactList"} )
 public void updatePartialContact() {
     String payload = """
            {
     			"firstName": "Alice"
     		}
             """;

     Response res = given()
             .header("Authorization", "Bearer " + token)
             .header("Content-Type", "application/json")
             .body(payload)
             .patch("/contacts/" + contactID);
     Assert.assertEquals(res.getStatusCode(), 200);
 	 Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
	  System.out.println("Response code"+ res.getStatusCode());
	  System.out.println("Response Message is "+res.getStatusLine());
	  Assert.assertEquals(res.jsonPath().getString("firstName"), "Alice", "firstName does not match");
	  System.out.println("FirstName updated successfully");
	  res.then().log().body();
}
 
 @Test(priority = 7, dependsOnMethods = "loginUser")
 public void logoutUser() {
     Response res = given()
             .header("Authorization", "Bearer " + token)
             .post("/users/logout");
     Assert.assertEquals(res.getStatusCode(), 200);
 	 Assert.assertEquals(res.getStatusLine(), "HTTP/1.1 200 OK");
	  System.out.println("Response code"+ res.getStatusCode());
	  System.out.println("Response Message is "+res.getStatusLine());
	  System.out.println("User logged out successfully");
 }
}


