package id.co.nds.catalogue.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerImp {
    @KafkaListener(topics = "CatalogueTopic", groupId = "Group100")
    public void listen(String name) {
        System.out.println("Received '" + name + "' from the CatalogueTopic.");
    }
}
