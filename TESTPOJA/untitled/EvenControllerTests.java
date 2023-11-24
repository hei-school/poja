// EvenControllerTests.java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvenControllerTests {

    @Test
    public void testGetEvenNumber() {
        EvenController evenController = new EvenController();
        int evenNumber = evenController.getEvenNumber().getBody();
        assertTrue(evenNumber % 2 == 0);
    }
}
