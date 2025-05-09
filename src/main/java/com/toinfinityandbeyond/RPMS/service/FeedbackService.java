package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Feedback;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AlertService alertService;

    public List<Feedback> getAllFeedback()
    {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id)
    {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }

    public Feedback getFeedbackByAppointment(Appointment appointment)
    {
        return feedbackRepository.findByAppointment(appointment)
                .orElse(null);
    }

    public List<Feedback> getFeedbackByDoctor(Doctor doctor)
    {
        return feedbackRepository.findByDoctor(doctor);
    }

    public List<Feedback> getFeedbackByPatient(Patient patient)
    {
        return feedbackRepository.findByPatient(patient);
    }

    public Feedback createFeedback(Feedback feedback) {
        // Check if feedback already exists for this appointment
        Optional<Feedback> existingFeedback = feedbackRepository.findByAppointment(feedback.getAppointment());
        if (existingFeedback.isPresent()) {
            throw new BadRequestException("Feedback already exists for this appointment");
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return savedFeedback;
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails)
    {
        Feedback feedback = getFeedbackById(id);

        // Update feedback details
        feedback.setDiagnosis(feedbackDetails.getDiagnosis());
        feedback.setComments(feedbackDetails.getComments());
        feedback.setRecommendations(feedbackDetails.getRecommendations());
        feedback.setNextSteps(feedbackDetails.getNextSteps());

        Feedback updatedFeedback = feedbackRepository.save(feedback);

        return updatedFeedback;
    }

    public void deleteFeedback(Long id)
    {
        Feedback feedback = getFeedbackById(id);
        feedbackRepository.delete(feedback);
    }

    public boolean hasFeedback(Appointment appointment)
    {
        return feedbackRepository.existsByAppointment(appointment);
    }

    public List<Appointment> getAppointmentsWithoutFeedback(Doctor doctor)
    {
        return feedbackRepository.findAppointmentsWithoutFeedbackByDoctor(doctor);
    }
}