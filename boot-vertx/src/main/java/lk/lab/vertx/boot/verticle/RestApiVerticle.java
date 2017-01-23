package lk.lab.vertx.boot.verticle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

@Component
public class RestApiVerticle extends AbstractVerticle {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Value("${lab.vertx.http-port:8080}")
	private int httpPort;
	
	@Override
	public void start() {
		logger.info("[" + Thread.currentThread().getName() + "] "
				+ getClass() + "#start() called.");
		
		Router router = Router.router(vertx);
		router.route(HttpMethod.GET, "/api/hello").handler(routingContext -> {
			logger.info("[" + Thread.currentThread().getName() + "] "
					+ getClass() + "#routeHandler called.");
			
			HttpServerResponse response = routingContext.response();
			
			vertx.eventBus().send("say.hello", "aaa", result -> {
				if (result.succeeded()) {
					response.setChunked(true)
							.putHeader("content-type", "application/json")
							.setStatusCode(200)
							.write((String) result.result().body())
							.end();
				} else {
					response.setChunked(true)
							.putHeader("content-type", "application/json")
							.setStatusCode(500)
							.write((String) result.result().body())
							.end();
				}
			});
		});
		
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(httpPort);
	}
	
}
