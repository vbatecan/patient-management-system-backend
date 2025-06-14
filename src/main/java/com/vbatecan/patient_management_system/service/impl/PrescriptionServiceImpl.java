package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.PrescriptionDTO;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.entities.Prescription;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.PrescriptionRepository;
import com.vbatecan.patient_management_system.service.interfaces.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

	private final PrescriptionRepository prescriptionRepository;
	private final AppointmentRepository appointmentRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Prescription save(PrescriptionDTO prescriptionDTO) {
		Prescription prescription = mapper.convertValue(prescriptionDTO, Prescription.class);
		prescription.setCreatedAt(LocalDateTime.now());
		prescription.setUpdatedAt(LocalDateTime.now());
		return prescriptionRepository.save(prescription);
	}

	@Override
	public Optional<Prescription> findById(Integer id) {
		return prescriptionRepository.findById(id);
	}

	@Override
	public Page<Prescription> findAll(Pageable pageable) {
		return prescriptionRepository.findAll(pageable);
	}

	@Override
	public Page<Prescription> findByAppointmentId(Integer appointmentId, Pageable pageable) {
		if ( !appointmentRepository.existsById(appointmentId) ) {
			throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
		}
		return prescriptionRepository.findByAppointmentId(appointmentId, pageable);
	}

	@Override
	@Transactional
	public Prescription update(Integer id, PrescriptionDTO prescriptionDTO) {
		Prescription existingPrescription = prescriptionRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

		if ( prescriptionDTO.getAppointment() != null ) {
			Appointment appointment = appointmentRepository.findById(prescriptionDTO.getAppointment().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + prescriptionDTO.getAppointment().getId()));
			existingPrescription.setAppointment(appointment);
		} else {
			throw new IllegalArgumentException("AppointmentId cannot be null when updating a Prescription.");
		}

		existingPrescription.setMedication(prescriptionDTO.getMedication());
		existingPrescription.setDosage(prescriptionDTO.getDosage());
		existingPrescription.setInstructions(prescriptionDTO.getInstructions());
		existingPrescription.setUpdatedAt(LocalDateTime.now());

		return prescriptionRepository.save(existingPrescription);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !prescriptionRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Prescription not found with id: " + id);
		}
		prescriptionRepository.deleteById(id);
	}
}
