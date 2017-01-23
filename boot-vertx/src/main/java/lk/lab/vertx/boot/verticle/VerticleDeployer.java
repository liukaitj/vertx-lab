package lk.lab.vertx.boot.verticle;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;

@Component
public class VerticleDeployer {
	
	@Autowired
	private RestApiVerticle restVerticle;
	
	@Autowired
	private ServiceVerticle serviceVerticle;
	
	@PostConstruct
	public void deployVerticles() {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(restVerticle);
		vertx.deployVerticle(serviceVerticle);
	}

}
