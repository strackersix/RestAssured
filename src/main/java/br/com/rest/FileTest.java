package br.com.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {

	
	@Test
	public void DeveObrigarEnvioArquivo() {
		
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // Deveria ser 400
			.body("error", is("Arquivo não enviado"))
		;
		
	}
		
	@Test
	public void DeveFazerUploadArquivo () {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/RestAssuredUpload.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("RestAssuredUpload.pdf"))
			.body("md5", is("52b6bc387a1761dd42a37bd4b9370649"))
			.body("size", is(27320))

		;
		
	}
	
	@Test
	public void NaoDeveFazerUploadArquivoGrande () {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/1Mb.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(20000L))
			.statusCode(413)
			.body("html.head.title", is("413 Request Entity Too Large"))
			.body("html.body.center.h1", is("413 Request Entity Too Large"))
			.body("html.body.center[1]", is("nginx/1.12.1 (Ubuntu)"))
		
		;
		
	}
	
	@Test
	public void DeveBaixarArquivo () throws IOException {
		
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray()
		
		;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
		
	}
	
	
	
}
