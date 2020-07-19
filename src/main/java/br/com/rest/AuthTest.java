package br.com.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class AuthTest {

	
	@Test
	public void DeveAcessarASWAPI () {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
			.body("height", is("172"))
			.body("mass", is("77"))
			.body("hair_color", is("blond"))
			.body("skin_color", is("fair"))
			.body("eye_color", is("blue"))
			.body("birth_year", is("19BBY"))
			.body("gender", is("male"))
			.body("homeworld", is("http://swapi.dev/api/planets/1/"))
		
		;
		
	}
	
//	http://api.openweathermap.org/data/2.5/weather?q=Fortaleza,BR&appid=a2531375567ed1262f9466b85a33bb2d&units=metric
	
	
	@Test
	public void DeveObterClima () {
		
		given()
			.log().all()
			.queryParam("q", "Fortaleza,BR")
			.queryParam("appid", "a2531375567ed1262f9466b85a33bb2d" )
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("coord.lon", is(-38.52f))
			.body("coord.lat", is(-3.72f))
			.body("weather.id", hasItem(801))
			.body("weather.main", hasItem("Rain"))
			.body("weather.description", hasItem("light rain"))
			.body("weather.icon", hasItem("10n"))
			.body("base", is("stations"))
			.body("main.temp", is(26.86f))
			.body("main.feels_like", is(28.6f))
			.body("main.temp_min", is(26.67f))
			.body("main.temp_max", is(27))
			.body("main.pressure", is(1014))
			.body("main.humidity", is(74))
			.body("timezone", is(-10800))
			.body("name", is("Fortaleza"))
			.body("id", is(6320062))
			.body("cod", is(200))
					
		;
		
	}
	
	
	@Test
	public void NaoDeveAcessarSemSenha () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		
		;
				
	}
	
	@Test
	public void DeveFazerAutenticacaoBasica () {
		
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		
		;
				
	}
	
	@Test
	public void DeveFazerAutenticacaoBasica2 () {
		
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
		
		;
		
	}
	
	@Test
	public void DeveFazerAutenticacaoBasicaChallenge () {
		
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
		
		;
		
	}
	
	@Test
	public void DeveFazerAutenticacaoComTokenJWT () {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "gabriel_conrado_@hotmail.com");
		login.put("senha", "66238546");
		
//		Login na API
//		Receber o Token
		
		String token = 
				
		given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(10141))
			.body("nome", is("Gabriel"))
			.body("token", is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAxNDF9.8GvOLUUipnqVWNIQVxeJvMA9R1Sm23EkTDfY9ujom40"))
			.extract().path("token")
		
		;
				
//		Obter as Contas
			
		given()	
			.log().all()
			.header("Authorization" , "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", hasItem(171750))
			.body("nome", hasItem("Conta para alterar"))
			.body("visivel", hasItem(true))
			.body("usuario_id", hasItem(10141))
				
		;
			
	}
	
	@Test
	public void DeveAcessarAplicacaoWeb () {
		
//		Login
		
		given()
			.log().all()
			.formParam("email", "gabriel_conrado_@hotmail.com")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("https://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
					
		;

//		Obter conta
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
