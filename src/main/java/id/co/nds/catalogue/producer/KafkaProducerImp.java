package id.co.nds.catalogue.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerImp {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final static String TOPIC_NAME = "CatalogueTopic";

    public void sendMessage(String userName) {
        kafkaTemplate.send(TOPIC_NAME, userName);
    }
}
