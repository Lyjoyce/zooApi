package com.zoo.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Child;
import com.zoo.api.entities.Egg;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Workshop;
import com.zoo.api.entities.WorkshopType;
import com.zoo.api.mappers.ReservationMapper;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.ChildRepository;
import com.zoo.api.repositories.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EggService eggService;
    private final AdultRepository adultRepository;
    private final ChildRepository childRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository,
                              EggService eggService,
                              AdultRepository adultRepository,
                              ChildRepository childRepository,
                              ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.eggService = eggService;
        this.adultRepository = adultRepository;
        this.childRepository = childRepository;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public ReservationDTO saveReservation(ReservationDTO dto) {
        // Récupérer l'adulte qui crée la réservation
        Adult adult = adultRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("Adult not found with id: " + dto.getCreatedById()));

        // Récupérer la liste des enfants participants
        List<Child> children = dto.getParticipantIds().stream()
                .map(id -> childRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Child not found with id: " + id)))
                .collect(Collectors.toList());

        // Validation ratio adulte / enfants
        if (children.size() > 6) {
            throw new IllegalArgumentException("Chaque adulte ne peut surveiller que 6 enfants maximum.");
        }

        // Construire l'entité Reservation à partir du DTO
        Reservation reservation = reservationMapper.toEntity(dto);

        // Remplacer les relations avec les entités complètes
        reservation.setCreatedBy(adult);
        reservation.setParticipants(children);

        // Gestion des œufs pour ateliers omelette
        if (reservation.getWorkshops() != null) {
            List<Workshop> omeletteWorkshops = reservation.getWorkshops().stream()
                    .filter(w -> w.getType() == WorkshopType.OMELETTE)
                    .toList();

            if (!omeletteWorkshops.isEmpty()) {
                List<Egg> eggsToUse = eggService.getAvailableEggsOrderedByDate(omeletteWorkshops.size());

                if (eggsToUse.size() < omeletteWorkshops.size()) {
                    throw new IllegalStateException("Pas assez d'œufs disponibles pour les ateliers omelette.");
                }

                for (int i = 0; i < omeletteWorkshops.size(); i++) {
                    Egg egg = eggsToUse.get(i);
                    egg.setUsed(true);
                    eggService.saveEgg(egg);

                    omeletteWorkshops.get(i).setUsedEgg(egg);
                }
            }
        }

        // Sauvegarder la réservation
        Reservation saved = reservationRepository.save(reservation);

        // Retourner un DTO mis à jour
        return reservationMapper.toDto(saved);
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("Réservation introuvable.");
        }

        Reservation reservation = optionalReservation.get();

        if (reservation.getReservationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible d’annuler une réservation déjà passée.");
        }

        if (reservation.getWorkshops() != null) {
            for (Workshop workshop : reservation.getWorkshops()) {
                if (workshop.getType() == WorkshopType.OMELETTE && workshop.getUsedEgg() != null) {
                    Egg egg = workshop.getUsedEgg();
                    egg.setUsed(false); // libérer l'œuf
                    eggService.saveEgg(egg);
                }
            }
        }

        reservationRepository.delete(reservation);
    }

}
