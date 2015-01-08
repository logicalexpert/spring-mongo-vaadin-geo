package com.logicalexpert.places;


import java.util.Arrays;
import java.util.Map;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.GooglePlaces.Param;

import com.logicalexpert.places.domain.GPlace;
import com.logicalexpert.places.service.PlaceRepository;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    GooglePlaces googlePlaces(@Value("${google.places.api.key}") String apiKey) {
    	return new GooglePlaces(apiKey); //Google API Key
    }

    @Bean
    CommandLineRunner init(RestTemplate restTemplate,GooglePlaces googlePlaces,
                           PlaceRepository placeRepository) {
        return args -> {

            String ip = restTemplate.getForObject("http://icanhazip.com", String.class).trim();

            Map<?, ?> loc = (Map<?, ?>) restTemplate.getForObject(
                    "http://geoip.nekudo.com/api/{ip}",
                    Map.class, ip).get("location");

            double longitude = Double.valueOf(""+loc.get("longitude"));
            double latitude = Double.valueOf(""+loc.get("latitude"));
            
//        	double latitude = 17.3990570;
//        	double longitude = 78.5099280;
        	double radius = 5000;

            logger.info("the IP of the current machine is: " + ip);

            placeRepository.deleteAll();
            Arrays.asList("park","food","movie_theater","hospital","bank","school","police").forEach(tfilter->
            googlePlaces.getNearbyPlaces(latitude, longitude, radius, 50, Param.name("types").value(tfilter))
            		.stream().map(p->placeRepository.save(new GPlace(p,tfilter)))
            		.forEach(System.out::println)
            		)
            		;
        };
    }


    @Bean
    EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return servletContainer -> ((TomcatEmbeddedServletContainerFactory) servletContainer)
                .addConnectorCustomizers(connector -> {
                    AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector
                            .getProtocolHandler();
                    httpProtocol.setCompression("on");
                    httpProtocol.setCompressionMinSize(256);
                    String mimeTypes = httpProtocol.getCompressableMimeTypes();
                    String mimeTypesWithJson = mimeTypes + ","
                            + MediaType.APPLICATION_JSON_VALUE
                            + ",application/javascript";
                    httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
                });
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
