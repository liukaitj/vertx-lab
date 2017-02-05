package lk.lab.vertx.boot.verticle;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import lk.lab.vertx.boot.service.disruptor.DisruptorProducer;

@Component
public class RestApiVerticle extends AbstractVerticle {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Value("${lab.vertx.http-port:8080}")
	private int httpPort;
	
	@Autowired
	private DisruptorProducer producer;
	
	@Override
	public void start() {
		logger.info("[" + Thread.currentThread().getName() + "] "
				+ getClass() + "#start() called.");
		
		Router router = Router.router(vertx);
		router.route(HttpMethod.GET, "/api/hello").handler(routingContext -> {
			logger.info("[" + Thread.currentThread().getName() + "] "
					+ getClass() + "#routeHandler called.");
			
			HttpServerResponse response = routingContext.response();
			
			// eventbus send的超时时间默认为30秒，更改此值以支持超长的线程等待
			DeliveryOptions opts = new DeliveryOptions().setSendTimeout(1800000);
			
			vertx.eventBus().send("say.hello", "aaa", opts, result -> {
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
							.write(result.cause().getMessage())
							.end();
				}
			});
		});
		
		router.route(HttpMethod.GET, "/api/hello2").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.setChunked(true)
					.putHeader("content-type", "application/json")
					.setStatusCode(200)
					.write("{\"hello\":\"ccc\"}")
					.end();
		});
		
		router.route(HttpMethod.GET, "/api/disruptor/publish").handler(routingContext -> {
			Random ran = new Random();
			int value = ran.nextInt(10);
			logger.info("[" + Thread.currentThread().getName() + "] "
					+ "#disruptor random value:" + value);
			producer.publishData(value);
			
			HttpServerResponse response = routingContext.response();
			response.setChunked(true)
					.putHeader("content-type", "application/json")
					.setStatusCode(200)
					.write("{\"hello\":\"disruptor/publish\"}")
					.end();
		});
		
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(httpPort);
	}
	
}
