package com.example.product;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
	@Bean
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();
		config.setInstanceName("hazelcast-instance")
				.addMapConfig(new MapConfig()
						.setName("products")
						.setTimeToLiveSeconds(3600)); // TTL of 1 hour
		return Hazelcast.newHazelcastInstance(config);
	}
}
