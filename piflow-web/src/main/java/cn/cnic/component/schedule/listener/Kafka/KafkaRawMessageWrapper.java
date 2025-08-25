package cn.cnic.component.schedule.listener.Kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class KafkaRawMessageWrapper {

    private ConsumerRecord<String, String> rawRecord;

}