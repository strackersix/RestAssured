package br.com.rest;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

	@Test
	public void TestOlaMundo() {

		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");

		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);

		Assert.assertEquals("Ola Mundo!", response.getBody().asString());
		Assert.assertEquals(200, response.getStatusCode());

		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		System.out.println(response.statusCode() == 200);

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

	}

	@Test
	public void DevoConhecerOutrasFormasRestAssured() {

		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me/ola").then().statusCode(200);

		RestAssured.given() //Pré condições. 
			.when() //Ação
				.get("http://restapi.wcaquino.me/ola")
			.then() //Assertivas
				.statusCode(200);
		
	}
	
	@Test
	public void DevoConhecerMatchersHamcrest () {
		
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128d, Matchers.greaterThan(120d));
		Assert.assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		Assert.assertThat(impares, Matchers.hasSize(5));
		Assert.assertThat(impares, Matchers.contains(1, 3, 5, 7, 9));
		Assert.assertThat(impares, Matchers.containsInAnyOrder(3, 1, 9, 7, 5));
		Assert.assertThat(impares, Matchers.hasItem(3));
		Assert.assertThat(impares, Matchers.hasItems(3, 1, 9));
		Assert.assertThat("Maria", Matchers.is(("Maria")));
		Assert.assertThat("Maria", Matchers.not(("João")));
			
		
	}
	
	@Test
	public void DevoValidarBody() {
		
		RestAssured.given() //Pré condições. 
		.when() //Ação
			.get("http://restapi.wcaquino.me/ola")
		.then() //Assertivas
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"))
			.body(Matchers.containsString("Mundo"));
			
		
	}
	
	
	
	
	
	
	

}
