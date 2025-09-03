package com.zoo.api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Egg;
import com.zoo.api.entities.Ostrich;
import com.zoo.api.enums.Gender; // ✅ IMPORTANT
import com.zoo.api.repositories.EggRepository;
import com.zoo.api.repositories.OstrichRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EggService {

    private final EggRepository eggRepository;
    private final OstrichRepository ostrichRepository;

    // --- petite méthode utilitaire pour vérifier le sexe ---
    private void assertFemale(Ostrich ostrich) {
        if (ostrich == null || ostrich.getGender() == null || ostrich.getGender() != Gender.FEMALE) {
            throw new IllegalStateException("Seules les autruches femelles peuvent pondre : " +
                    (ostrich != null ? ostrich.getName() : "autruche inconnue"));
        }
    }

    // Lier un œuf à une autruche
    public Egg assignEggToOstrich(Egg egg, String ostrichName) {
        Ostrich ostrich = ostrichRepository.findByName(ostrichName)
                .orElseThrow(() -> new IllegalArgumentException("Autruche non trouvée : " + ostrichName));

        // Vérification enum (plus de equalsIgnoreCase sur String)
        assertFemale(ostrich);

        egg.setOstrich(ostrich);
        return eggRepository.save(egg);
    }

    public Egg createEggForOstrich(String ostrichName, LocalDate dateLaid) {
        Ostrich ostrich = ostrichRepository.findByName(ostrichName)
                .orElseThrow(() -> new IllegalArgumentException("Autruche non trouvée : " + ostrichName));

        // Vérification enum (plus de equalsIgnoreCase sur String)
        assertFemale(ostrich);

        Egg egg = new Egg();
        egg.setOstrich(ostrich);
        egg.setDateLaid(dateLaid);
        egg.setActive(true);
        egg.setUsed(false);
        egg.setValidatedByVet(false);

        return eggRepository.save(egg);
    }

    public Egg saveEgg(Egg egg) { return eggRepository.save(egg); }

    public boolean existsEggById(Long id) { return eggRepository.existsById(id); }

    public Optional<Egg> getEggById(Long id) { return eggRepository.findById(id); }

    public List<Egg> getAvailableEggsOrderedByDate(int quantity) {
        List<Egg> availableEggs = eggRepository.findByUsedFalseAndActiveTrueOrderByDateLaidAsc();
        if (availableEggs.size() < quantity) {
            throw new IllegalArgumentException("Pas assez d'œufs disponibles.");
        }
        return availableEggs.subList(0, quantity);
    }

    public boolean hasAvailableEgg() { return !eggRepository.findByUsedFalseAndActiveTrue().isEmpty(); }

    public Optional<Egg> getOneAvailableEgg() {
        return eggRepository.findByUsedFalseAndActiveTrue().stream().findFirst();
    }

    public List<Egg> reserveEggs(int quantity) {
        List<Egg> eggsToReserve = getAvailableEggsOrderedByDate(quantity);
        eggsToReserve.forEach(egg -> egg.setUsed(true));
        return eggRepository.saveAll(eggsToReserve);
    }

    public void releaseEggs(List<Egg> eggs) {
        eggs.forEach(egg -> egg.setUsed(false));
        eggRepository.saveAll(eggs);
    }

    public Egg validateEggById(Long eggId) {
        Egg egg = eggRepository.findById(eggId)
                .orElseThrow(() -> new IllegalArgumentException("Œuf non trouvé"));

        if (egg.getDateLaid().plusDays(10).isBefore(LocalDate.now())) {
            throw new IllegalStateException("L'œuf a plus de 10 jours, il n'est plus valide.");
        }

        egg.setValidatedByVet(true);
        egg.setValidationDate(LocalDate.now());
        return eggRepository.save(egg);
    }

    public List<Egg> getEggsToValidate() {
        LocalDate today = LocalDate.now();
        return eggRepository.findByUsedFalseAndActiveTrueAndValidatedByVetFalse()
                .stream()
                .filter(egg -> ChronoUnit.DAYS.between(egg.getDateLaid(), today) <= 10)
                .toList();
    }

    public List<Egg> getValidEggsForAtelier() {
        return eggRepository.findByUsedFalseAndActiveTrueAndValidatedByVetTrue();
    }

    public List<Egg> getAllActiveEggs() { return eggRepository.findByActiveTrue(); }

    public Optional<Egg> deactivateEgg(Long id) {
        return eggRepository.findById(id).map(egg -> {
            egg.setActive(false);
            return eggRepository.save(egg);
        });
    }

    public void deleteEgg(Long id) { eggRepository.deleteById(id); }

    public Egg updateEgg(Long id, Egg updatedEgg) {
        Egg egg = eggRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Œuf non trouvé avec l'id : " + id));

        egg.setDateLaid(updatedEgg.getDateLaid());
        egg.setUsed(updatedEgg.isUsed());

        // Si on change l'autruche, on vérifie aussi qu'elle est femelle
        if (updatedEgg.getOstrich() != null) {
            assertFemale(updatedEgg.getOstrich());
            egg.setOstrich(updatedEgg.getOstrich());
        }

        egg.setActive(updatedEgg.isActive());
        egg.setValidatedByVet(updatedEgg.isValidatedByVet());

        return eggRepository.save(egg);
    }
}
