package lk.lab.vertx.boot.service.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

@Configuration
public class DisruptorConfiguration {
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Bean
	public Disruptor<DisruptorData> disruptor() {
		Executor executor = Executors.newCachedThreadPool();
		DisruptorDataFactory factory = new DisruptorDataFactory();
		int bufferSize = 1024;
		Disruptor<DisruptorData> disruptor = new Disruptor<DisruptorData>(
				factory, bufferSize, executor,
				ProducerType.MULTI,
				new BlockingWaitStrategy()
//				new SleepingWaitStrategy()
			);
		disruptor.handleEventsWithWorkerPool(
				new DisruptorConsumer(),
				new DisruptorConsumer(),
				new DisruptorConsumer(),
				new DisruptorConsumer());
		return disruptor;
	}
	
	@Bean
	public DisruptorProducer producer(Disruptor<DisruptorData> disruptor) {
		RingBuffer<DisruptorData> ringBuffer = disruptor.getRingBuffer();
		DisruptorProducer producer =  new DisruptorProducer();
		producer.setRingBuffer(ringBuffer);
		return producer;
	}
	
	@Bean
	public SquareDisruptor squareDisruptor(Disruptor<DisruptorData> disruptor) {
		SquareDisruptor squareDisruptor = new SquareDisruptor();
		squareDisruptor.setDisruptor(disruptor);
		return squareDisruptor;
	}

}
