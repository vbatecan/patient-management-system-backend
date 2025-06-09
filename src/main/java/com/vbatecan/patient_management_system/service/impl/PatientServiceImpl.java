package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.PatientService;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public PatientDTO createPatient(PatientDTO patientDto) {
        Patient patient = toEntity(patientDto);
        Patient saved = patientRepository.save(patient);
        return toDto(saved);
    }

    @Override
    public PatientDTO getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return toDto(patient);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PatientDTO updatePatient(Integer id, PatientDTO patientDto) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        // Update fields
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setGender(patientDto.getGender());
        patient.setContactNumber(patientDto.getContactNumber());
        patient.setEmail(patientDto.getEmail());
        patient.setAddress(patientDto.getAddress());
        patient.setEmergencyContact(patientDto.getEmergencyContact());
        patient.setUpdatedAt(patientDto.getUpdatedAt());
        Patient updated = patientRepository.save(patient);
        return toDto(updated);
    }

    @Override
    public void deletePatient(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> searchPatients(String keyword) {
        // Implement custom search logic if needed
        return getAllPatients().stream()
            .filter(p -> p.getFirstName().contains(keyword) || p.getLastName().contains(keyword))
            .collect(Collectors.toList());
    }

    private PatientDTO toDto(Patient patient) {
        return new PatientDTO(
            patient.getId(),
            patient.getUserAccount() != null ? patient.getUserAccount().getId() : null,
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth(),
            patient.getGender(),
            patient.getContactNumber(),
            patient.getEmail(),
            patient.getAddress(),
            patient.getEmergencyContact(),
            patient.getCreatedAt(),
            patient.getUpdatedAt()
        );
    }

    private Patient toEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        // Set userAccount if needed
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setContactNumber(dto.getContactNumber());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setCreatedAt(dto.getCreatedAt());
        patient.setUpdatedAt(dto.getUpdatedAt());
        return patient;
    }
} 