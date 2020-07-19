package br.com.rest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;

public class UserJsonTest {

	@Test
	public void DeveVerificarPrimeiroNivel() {
		
		RestAssured.given()
		.when()
			.get("https://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Silva"))
			.body("age", Matchers.greaterThan(18));			
				
	}
	
	@Test
	public void DeveVerificarPrimeiroNivelOutrasFormas() {
		
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
		
		//Path
		System.out.println(response.path("id"));
		System.out.println(response.path("name"));
		System.out.println(response.path("age"));
		System.out.println(response.path("salary"));
		
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path( "%s", "id"));
		Assert.assertEquals(new String("João da Silva") , response.path("name"));
		Assert.assertEquals(new Integer(30), response.path("age"));
		Assert.assertEquals(new Float(1234.5677), response.path("salary"));
				
		//JasonPath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals( 1 , jpath.getInt("id"));
		Assert.assertEquals("João da Silva", jpath.getString("name"));
		Assert.assertEquals(30, jpath.getInt("age"));
				
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
		
		String name = JsonPath.from(response.asString()).getString("name");
		Assert.assertEquals("João da Silva", name);
		
		int age = JsonPath.from(response.asString()).getInt("age");
		Assert.assertEquals(30, age);
		
	}
	
	@Test 
	public void DeveVerificarSegundoNivel() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(2))
			.body("name", Matchers.containsString("Joaquina"))
			.body("endereco.rua", Matchers.containsString("Rua dos bobos"))
			.body("endereco.numero", Matchers.is(0));
		
	}
	
	@Test
	public void DeveVerificarLista () {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(3))
			.body("name", Matchers.containsString("Ana Júlia"))
			.body("age", Matchers.is(20))
			.body("filhos", Matchers.hasSize(2))
			.body("filhos[0].name", Matchers.is("Zezinho"))
			.body("filhos[1].name", Matchers.is("Luizinho"))
			.body("filhos.name", Matchers.hasItem("Luizinho"))
			.body("filhos.name", Matchers.hasItems("Luizinho" , "Zezinho"));
									
	}
	
	@Test
	public void DeveRetornarErroUsuarioInexistente () {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", Matchers.containsString("Usuário inexistente"));
		
	}
	
	@Test 
	public void DeveVerificarListaRaiz () {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))
			.body("name", Matchers.hasItems("João da Silva" , "Maria Joaquina" , "Ana Júlia"))
			.body("age[1]", Matchers.is(25))
			.body("age[0]", Matchers.is(30))
			.body("id[0]", Matchers.is(1))
			.body("id[2]", Matchers.is(3))
			.body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho" , "Luizinho")))
			.body("salary", Matchers.contains(1234.5678f, 2500, null));
		
	}
	
	@Test 
	public void DevoFazerVerificacoesAvancadas () {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/")
		.then()
			.statusCode(200)
			.log().all()
			.body("$" , Matchers.hasSize(3))
			.body("age.findAll{it <= 25}.size", Matchers.is(2))
			.body("age.findAll{it <= 25 && it > 20}.size", Matchers.is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", Matchers.hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", Matchers.is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[-1].name", Matchers.is("Ana Júlia"))
			.body("find{it.age <= 25}.name", Matchers.is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina" , "Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", Matchers.hasItems("João da Silva" , "Maria Joaquina"))
			.body("name.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
			.body("age.collect{it * 2}", Matchers.hasItems(60, 50, 40))
			.body("id.max()", Matchers.is(3))
			.body("salary.min()", Matchers.is(1234.5677f))
			
			
		;
		
	}
	
	@Test
	public void DevoUnirJsonPathComJAVA () {
		
		ArrayList<String> names = 
			given()
			.when()
				.get("https://restapi.wcaquino.me/users/")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}")
							
			;
		
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina"));
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
	}
	
	
	
	
}
