package es.us.dp1.chess.tournament.practice.x.transactions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@Service
public class EnrollmentService {

    private final WorkshopRepository workshopRepository;
    private final ReceiptRepository receiptRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(WorkshopRepository workshopRepository, ReceiptRepository receiptRepository,
            EnrollmentRepository enrollmentRepository) {
        this.workshopRepository = workshopRepository;
        this.receiptRepository = receiptRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // WorkshopFullException is checked: with a plain @Transactional the transaction would COMMIT
    // when it is thrown, and the receipt saved in step 1 would stay in the database
    @Transactional(rollbackFor = WorkshopFullException.class)
    public Enrollment enroll(Integer workshopId, String attendee, int price) throws WorkshopFullException {
        Workshop workshop = workshopRepository.findById(workshopId)
            .orElseThrow(() -> new ResourceNotFoundException("Workshop", "id", workshopId));
        // 1. The attendee pays
        Receipt receipt = new Receipt();
        receipt.setConcept("Enrollment of " + attendee + " in " + workshop.getName());
        receipt.setAmount(price);
        receipt = receiptRepository.save(receipt);
        // 2. Business rule
        if (workshop.getEnrolled() >= workshop.getCapacity())
            throw new WorkshopFullException();
        // 3. The attendee is enrolled
        workshop.setEnrolled(workshop.getEnrolled() + 1);
        workshopRepository.save(workshop);
        Enrollment enrollment = new Enrollment();
        enrollment.setAttendee(attendee);
        enrollment.setWorkshop(workshop);
        enrollment.setReceipt(receipt);
        return enrollmentRepository.save(enrollment);
    }

    // One transaction for the whole loop. The calls to enroll go through "this", not through the
    // Spring proxy, so the annotation of enroll does not apply to them: atomicity comes from here.
    @Transactional(rollbackFor = WorkshopFullException.class)
    public List<Enrollment> enrollAll(Integer workshopId, List<String> attendees, int price)
            throws WorkshopFullException {
        List<Enrollment> result = new ArrayList<>();
        for (String attendee : attendees)
            result.add(enroll(workshopId, attendee, price));
        return result;
    }

}
