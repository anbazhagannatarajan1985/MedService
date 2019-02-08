package com.medicub.service.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {
	@Value("${document.contact-email}")
	private String contactEmail;
	@Bean
	public Docket defaultApi() {
		Parameter p = new ParameterBuilder()
        .name("Authorization")
        .description("Bearer \"access-token\"")
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .required(true)
        .build();
		
		List<Parameter> paramList = new ArrayList<>();
		paramList.add(p);
		
		Set<String> producesSet = new HashSet<String>();
		producesSet.add("application/json");
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ads.ticket.api.controller"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
				.globalOperationParameters(paramList)
				.pathMapping("/")
				.produces(new HashSet<String>(Arrays.asList(MediaType.APPLICATION_JSON.toString())))
				.consumes(new HashSet<String>(Arrays.asList(MediaType.APPLICATION_JSON.toString())))
				.protocols(new HashSet<String>(Arrays.asList("https")))
				.apiInfo(apiInfo());
	}
	private ApiInfo apiInfo() {
		
	    ApiInfo apiInfo = new ApiInfo("ADS Ticket System APIs",
	    	      "ADS ticket management system API Documentation",
	    	      "1.0.0",
	    	      "",
	    	      new Contact("ADS-ADMIN@alldirectionsource.com", "", contactEmail),
	    	      "",
	    	      "");
	    return apiInfo;
	}
}
