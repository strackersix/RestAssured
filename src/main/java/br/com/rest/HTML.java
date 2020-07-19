package br.com.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;

public class HTML {

	@Test
	public void DeveFazerBuscasComHTML () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr[2].td[2]", is("20"))
			.body("tr[0].td[2]", is("30"))
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
						
		;
		
	}
	
	@Test
	public void DeveFazerBuscasComXpathHTML () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(hasXPath("count(//table/tr)", is("4")))
						
		;
		
	}
	
	
}
