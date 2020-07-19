package br.com.rest;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.internal.path.xml.NodeImpl;

public class UserXMLTest {
	
	
	@BeforeClass
	public static void SetupAmbiente () {
		
		RestAssured.baseURI = "http://restapi.wcaquino.me";
//		RestAssured.port = 443;
//		RestAssured.basePath = "";
		
	}
		
	@Test
	public void devoTrabalharComXML() {
				
		given()
		.when()
			.get("usersXML/3")
		.then()
			.statusCode(200)
			
			.rootPath("user")
			.body("name", Matchers.is("Ana Julia"))
			.body("@id", Matchers.is("3"))
			
			.rootPath("users.filhos")
			.body("name.size()", Matchers.is(2))
			
			.detachRootPath("filhos")
			.body("filhos.name[0]", Matchers.is("Zezinho"))
			.body("filhos.name[1]", Matchers.is("Luizinho"))
			
			.appendRootPath("filhos")
			.body("name", Matchers.hasItem("Luizinho"))
			.body("name", Matchers.hasItems("Luizinho" , "Zezinho"))
		;
		
	}
	
	@Test
	public void DevoFazerPesquisasAvancadasComXML() {
		
		given()
		.when()
			.get("usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", Matchers.is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", Matchers.is(2))
			.body("users.user.@id", Matchers.hasItems("1", "2", "3"))
			.body("users.user.find{it.age == 25}.name", Matchers.is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", Matchers.is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2}", Matchers.hasItems(40, 50, 60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", Matchers.is("MARIA JOAQUINA"))
						
		;
		
	}
	
	@Test
	public void DevoFazerPesquisasAvancadasComXMLEJava() {
		
		ArrayList<NodeImpl> nomes = given()
		.when()
			.get("usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}")
								
		;
		
		System.out.println(nomes);
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
		
	}
	

	
	
}
