package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OpenApiSchemaTest {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void testOpenApiSchema() throws IOException {
		// Предполагаем, что есть конечная точка для генерации схемы OpenAPI
		String generatedSchema = restTemplate.getForObject("http://localhost:8080/v3/api-docs", String.class);

		// Загрузка сохранённой текущей схемы OpenAPI
		Path savedSchemaPath = Paths.get("current-openapi-schema.yaml");
		assert Files.exists(savedSchemaPath) : "Сохранённая схема OpenAPI не найдена!";
		String savedSchema = Files.readString(savedSchemaPath);

		// Сравнение схем
		assertEquals(savedSchema, generatedSchema, "Схемы OpenAPI не совпадают!");
	}
}
