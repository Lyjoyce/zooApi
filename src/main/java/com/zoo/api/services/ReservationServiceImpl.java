package com.zoo.api.services;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Egg;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Workshop;
import com.zoo.api.enums.WorkshopType;
import com.zoo.api.mappers.ReservationMapper;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.ReservationRepository;
import com.zoo.api.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EggService eggService;
    private final AdultRepository adultRepository;
    private final WorkshopRepository workshopRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  EggService eggService,
                                  AdultRepository adultRepository,
                                  WorkshopRepository workshopRepository,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.eggService = eggService;
        this.adultRepository = adultRepository;
        this.workshopRepository = workshopRepository;
        this.reservationMapper = reservationMapper;
    }

    // CREATE
    @Override
    @Transactional
    public ReservationDTO saveReservation(ReservationDTO dto) {
        Adult adult = adultRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("Adulte introuvable avec l'id: " + dto.getCreatedById()));

        if (dto.getNbChildren() > dto.getNbAdults() * 6) {
            throw new IllegalArgumentException("Un adulte peut surveiller au maximum 6 enfants. "
                    + "Réservation refusée : " + dto.getNbChildren() + " enfants pour "
                    + dto.getNbAdults() + " adultes.");
        }

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setCreatedBy(adult);

        handleWorkshopsAndEggs(dto, reservation);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    // UPDATE
    @Override
    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable avec l'id: " + id));

        if (dto.getNbChildren() > dto.getNbAdults() * 6) {
            throw new IllegalArgumentException("Un adulte peut surveiller au maximum 6 enfants. "
                    + "Mise à jour refusée : " + dto.getNbChildren() + " enfants pour "
                    + dto.getNbAdults() + " adultes.");
        }

        existing.setNbAdults(dto.getNbAdults());
        existing.setNbChildren(dto.getNbChildren());
        existing.setReservationDate(dto.getReservationDate());

        // Libérer les œufs précédemment utilisés si modification
        if (existing.getWorkshops() != null) {
            existing.getWorkshops().forEach(w -> {
                if (w.getType() == WorkshopType.OMELETTE && w.getAllocatedEgg() != null) {
                    Egg egg = w.getAllocatedEgg();
                    egg.setAllocated(false);
                    eggService.saveEgg(egg);
                    w.setAllocatedEgg(null);
                }
            });
        }

        handleWorkshopsAndEggs(dto, existing);

        Reservation updated = reservationRepository.save(existing);
        return reservationMapper.toDto(updated);
    }
    
    // DELETE
    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable."));

        if (reservation.getReservationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible d’annuler une réservation déjà passée.");
        }

        if (reservation.getWorkshops() != null) {
            reservation.getWorkshops().forEach(workshop -> {
                if (workshop.getType() == WorkshopType.OMELETTE && workshop.getAllocatedEgg() != null) {
                    Egg egg = workshop.getAllocatedEgg();
                    egg.setAllocated(false);
                    eggService.saveEgg(egg);
                }
            });
        }

        reservationRepository.delete(reservation);
    }

 // READ ALL
    @Override
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

 // READ ONE
    @Override
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable avec l'id: " + id));
        return reservationMapper.toDto(reservation);
    }
    
    @Transactional
    private void handleWorkshopsAndEggs(ReservationDTO dto, Reservation reservation) {
        if (dto.getWorkshopIds() != null && !dto.getWorkshopIds().isEmpty()) {
            List<Workshop> workshops = workshopRepository.findAllById(dto.getWorkshopIds());
            if (workshops.size() != dto.getWorkshopIds().size()) {
                throw new IllegalArgumentException("Certains ateliers sont introuvables en base.");
            }

            workshops.forEach(w -> w.setReservation(reservation));
            reservation.setWorkshops(workshops);

            // Gérer œufs pour ateliers omelette
            List<Workshop> omeletteWorkshops = workshops.stream()
                    .filter(w -> w.getType() == WorkshopType.OMELETTE)
                    .collect(Collectors.toList());

            if (!omeletteWorkshops.isEmpty()) {
                List<Egg> eggsToUse = eggService.getAvailableEggsOrderedByDate(omeletteWorkshops.size());

                if (eggsToUse.size() < omeletteWorkshops.size()) {
                    throw new IllegalStateException("Pas assez d'œufs disponibles pour les ateliers omelette.");
                }

                for (int i = 0; i < omeletteWorkshops.size(); i++) {
                    Egg egg = eggsToUse.get(i);
                    egg.setAllocated(true);
                    eggService.saveEgg(egg);
                    omeletteWorkshops.get(i).setAllocatedEgg(egg);
                }
            }
        }
    }
}


