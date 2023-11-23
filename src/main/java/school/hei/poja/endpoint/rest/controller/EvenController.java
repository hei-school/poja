package school.hei.poja.endpoint.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import school.hei.poja.repository.DummyRepository;
import school.hei.poja.repository.DummyUuidRepository;
import school.hei.poja.endpoint.event.EventProducer;
import java.util.Random;
public class EvenController {
    private DummyRepository dummyRepository;
    private DummyUuidRepository dummyUuidRepository;
    private EventProducer eventProducer;

    @GetMapping("/even")
    public int getEvenNumber() {
        int evenNumber = generateEvenNumber();
        return evenNumber;
    }

    private int generateEvenNumber() {
        Random random = new Random();
        int randomEven = random.nextInt(Integer.MAX_VALUE / 2) * 2;
        return randomEven;
    }
}