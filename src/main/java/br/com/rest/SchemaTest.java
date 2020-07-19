package br.com.rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.xml.sax.SAXParseException;

public class SchemaTest {

	@Test
	public void DeveValidarSchemaXML () {
		
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;		;
				
	}
	
	@Test(expected=SAXParseException.class)
	public void NaoDeveValidarSchemaXMLInvalido () {
		
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/invalidUsersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;		;
				
	}
	
	@Test
	public void DeveValidarSchemaJson () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
		;		
				
	}
	
	
}
