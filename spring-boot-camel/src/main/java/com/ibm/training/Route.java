package com.ibm.training;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Configuration

public class Route extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// move data from one file to another

		// moveAllFile();
		// moveSpecificFile();
		// moveSpecificFileWithBody("Java Techie");

		// fileProcess();
		// multiFileProcessor();

		csvToJson();
		jsonToCsvFromSystem();
		jsonToCsvFromDb();

		
	}

	public void moveAllFile() {
		from("file:C:/Users/RahulDusaje/Desktop/a?noop=true").to("file:C:/Users/RahulDusaje/Desktop/b");

	}

	public void moveSpecificFile() {
		from("file:C:/Users/RahulDusaje/Desktop/a?noop=true").filter(header(Exchange.FILE_NAME).startsWith("myFile"))
				.to("file:C:/Users/RahulDusaje/Desktop/b");

	}

	public void csvToJson() {

		BindyDataFormat csv = new BindyDataFormat();
		csv.setClassType(Shipper.class);
		csv.setType(BindyType.Csv);

		from("file:source?noop=true")

				.unmarshal(csv).marshal().json(JsonLibrary.Jackson) // using Jackson library

				.to("file://destination?FileName=rahul.json");

	}

	public void jsonToCsvFromSystem() throws IOException {
		JsonNode jsonTree = new ObjectMapper().readTree(new File(
				"C:\\Users\\RahulDusaje\\Documents\\workspace-spring-tool-suite-4-4.5.1.RELEASE\\spring-boot-camel\\destination\\rahul.json"));

		com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
		JsonNode firstObject = jsonTree.elements().next();
		firstObject.fieldNames().forEachRemaining(fieldName -> {
			csvSchemaBuilder.addColumn(fieldName);
		});
		CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

		CsvMapper csvMapper = new CsvMapper();
		csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(new File("destination_2/orderLines.csv"),
				jsonTree);
	}

	public void jsonToCsvFromDb() throws Exception {
	

		
	}
		
//		JsonNode jsonTree = new ObjectMapper().readTree(new File("http://localhost:8110/getAllOrders"));
//
//		com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
//		JsonNode firstObject = jsonTree.elements().next();
//		firstObject.fieldNames().forEachRemaining(fieldName -> {
//			csvSchemaBuilder.addColumn(fieldName);
//		});
//		CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
//
//		CsvMapper csvMapper = new CsvMapper();
//		csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(new File("destination_3/orders.csv"),
//				jsonTree);
	}

//	public void moveSpecificFileWithBody(String content) {
//		from("file:C:/Users/RahulDusaje/Desktop/a?noop=true").filter(body().startsWith(content))
//				.to("file:C:/Users/RahulDusaje/Desktop/b");
//
//	}

//	public void fileProcess() {
//		from("file:source?noop=true").process(p -> {
//
//			String body = p.getIn().getBody(String.class);
//			StringBuilder sb = new StringBuilder();
//
//			Arrays.stream(body.split(" ")).forEach(s -> {
//				sb.append(s + ",");
//			});
//
//			p.getIn().setBody(sb);
//		}).to("file:destination?FileName=records.csv");
//
//	}

//	public void textToCsv() {
//		from("file:source?noop=true").unmarshal().csv().split(body().tokenize(",")).choice()
//				.when(body().contains("closed")).to("file:destination?FileName=closedrecords.csv")
//				.when(body().contains("Pending")).to("file:destination?FileName=pendingrecords.csv")
//				.when(body().contains("Interest")).to("file:destination?FileName=Interestrecords.csv");
//
//	}

