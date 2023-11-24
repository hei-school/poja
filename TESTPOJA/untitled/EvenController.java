// EvenController.java
@RestController
public class EvenController {

    @GetMapping("/even")
    public ResponseEntity<Integer> getEvenNumber() {
        int evenNumber = generateEvenNumber();
        return ResponseEntity.ok(evenNumber);
    }

    private int generateEvenNumber() {
        Random random = new Random();
        int evenNumber = random.nextInt(Integer.MAX_VALUE / 2) * 2;
        return evenNumber;
    }
}
