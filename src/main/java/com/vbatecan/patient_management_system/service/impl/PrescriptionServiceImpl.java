package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment;
import com.vbatecan.patient_management_system.model.Prescription;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.PrescriptionRepository;
import com.vbatecan.patient_management_system.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = convertToEntity(prescriptionDTO);
        prescription.setCreatedAt(LocalDateTime.now());
        prescription.setUpdatedAt(LocalDateTime.now());
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToDTO(savedPrescription);
    }

    @Override
    public Optional<PrescriptionDTO> getPrescriptionById(Integer id) {
        return prescriptionRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByAppointmentId(Integer appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        }
        return prescriptionRepository.findByAppointmentId(appointmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrescriptionDTO update(Integer id, PrescriptionDTO prescriptionDTO) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

        if (prescriptionDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(prescriptionDTO.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + prescriptionDTO.getAppointmentId()));
            existingPrescription.setAppointment(appointment);
        } else {
             throw new IllegalArgumentException("AppointmentId cannot be null when updating a Prescription.");
        }

        existingPrescription.setMedication(prescriptionDTO.getMedication());
        existingPrescription.setDosage(prescriptionDTO.getDosage());
        existingPrescription.setInstructions(prescriptionDTO.getInstructions());
        existingPrescription.setUpdatedAt(LocalDateTime.now());

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        return convertToDTO(updatedPrescription);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prescription not found with id: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        if (prescription.getAppointment() != null) {
            dto.setAppointmentId(prescription.getAppointment().getId());
        }
        dto.setMedication(prescription.getMedication());
        dto.setDosage(prescription.getDosage());
        dto.setInstructions(prescription.getInstructions());
        dto.setCreatedAt(prescription.getCreatedAt());
        dto.setUpdatedAt(prescription.getUpdatedAt());
        return dto;
    }

    private Prescription convertToEntity(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription();

        if (prescriptionDTO.getAppointmentId() == null) {
            throw new IllegalArgumentException("AppointmentId is required for a Prescription.");
        }
        Appointment appointment = appointmentRepository.findById(prescriptionDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + prescriptionDTO.getAppointmentId()));
        prescription.setAppointment(appointment);

        prescription.setMedication(prescriptionDTO.getMedication());
        prescription.setDosage(prescriptionDTO.getDosage());
        prescription.setInstructions(prescriptionDTO.getInstructions());
        return prescription;
    }
}
