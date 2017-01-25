package lk.lab.vertx.boot.service.disruptor;

import com.lmax.disruptor.RingBuffer;

public class DisruptorProducer {
	
	private RingBuffer<DisruptorData> ringBuffer;
	
	public void setRingBuffer(RingBuffer<DisruptorData> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void publishData(long value) {
		
		long sequence = ringBuffer.next();
		try {
			DisruptorData event = ringBuffer.get(sequence);
			event.setValue(value);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

}
