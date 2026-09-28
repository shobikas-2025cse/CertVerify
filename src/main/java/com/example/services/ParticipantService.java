package com.example.services;

import com.example.exception.ResourceNotFoundException;
import com.example.models.Participant;
import com.example.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
    }

    public Participant updateParticipant(Long id, Participant updatedParticipant) {
        Participant existing = getParticipantById(id);
        existing.setName(updatedParticipant.getName());
        existing.setEmail(updatedParticipant.getEmail());
        existing.setPhone(updatedParticipant.getPhone());
        return participantRepository.save(existing);
    }

    public void deleteParticipant(Long id) {
        Participant existing = getParticipantById(id);
        participantRepository.delete(existing);
    }

    public long countTotalParticipants() {
        return participantRepository.count();
    }
}
