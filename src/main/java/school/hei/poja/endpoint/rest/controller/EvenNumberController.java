package main.java.school.hei.poja.endpoint.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class EvenNumberController {

    @GetMapping("/even")
    public int getRandomEvenNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE);
        // Assure que le nombre est pair en le rendant pair s'il est impair
        return randomNumber % 2 == 0 ? randomNumber : randomNumber + 1;
    }
}
