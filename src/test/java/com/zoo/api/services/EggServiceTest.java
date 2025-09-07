package com.zoo.api.services;

import com.zoo.api.entities.Egg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EggServiceTest {

    @Autowired
    private EggService eggService;

    @Test
    void testConserveEggValidated() {
        // ⚠️ Assure-toi que l’œuf avec ID = 1 est validé en base (validatedByVet = true)
        Egg conservedEgg30 = eggService.conserveEgg(1L, 30);
        System.out.println("Œuf 1 conservé jusqu’au : " + conservedEgg30.getConservationEndDate());

        // ⚠️ Assure-toi que l’œuf avec ID = 2 est validé en base (validatedByVet = true)
        Egg conservedEgg60 = eggService.conserveEgg(2L, 60);
        System.out.println("Œuf 2 conservé jusqu’au : " + conservedEgg60.getConservationEndDate());
    }

    @Test
    void testConserveEggNotValidated() {
        // ⚠️ Assure-toi que l’œuf avec ID = 3 n’est PAS validé en base (validatedByVet = false)
        assertThrows(IllegalStateException.class, () -> {
            eggService.conserveEgg(3L, 30);
        });
        System.out.println("Test OK : tentative de conservation d’un œuf non validé → exception attendue");
    }
}
