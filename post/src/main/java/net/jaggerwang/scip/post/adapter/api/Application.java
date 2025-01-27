package net.jaggerwang.scip.post.adapter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.post")
@EntityScan("net.jaggerwang.scip.post.adapter.dao.jpa.entity")
@EnableJpaRepositories("net.jaggerwang.scip.post.adapter.dao.jpa")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
