package tarabaho.tarabaho.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.entity.Booking;
import tarabaho.tarabaho.entity.BookingStatus;
import tarabaho.tarabaho.entity.BookingType;
import tarabaho.tarabaho.entity.Category;
import tarabaho.tarabaho.entity.PaymentMethod;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.BookingRepository;
import tarabaho.tarabaho.repository.CategoryRepository;
import tarabaho.tarabaho.repository.UserRepository;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Booking createUrgentBooking(Long userId, String categoryName, Double latitude, Double longitude, Double radius, String paymentMethod, String jobDetails) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        if (!user.getIsVerified()) {
            throw new Exception("User not verified");
        }

        // Check for existing bookings
        List<Booking> existingBookings = bookingRepository.findByUserAndStatusIn(user, Arrays.asList(BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.IN_PROGRESS));
        if (!existingBookings.isEmpty()) {
            throw new Exception("User already has an active or pending booking");
        }

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new Exception("Category not found");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCategory(category);
        booking.setType(BookingType.URGENT);
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        booking.setLatitude(latitude);
        booking.setLongitude(longitude);
        booking.setRadius(radius);
        booking.setJobDetails(jobDetails);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking createCategoryBooking(Long userId, Long workerId, String categoryName, String paymentMethod, String jobDetails) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        if (!user.getIsVerified()) {
            throw new Exception("User not verified");
        }

        // Check for existing bookings
        List<Booking> existingBookings = bookingRepository.findByUserAndStatusIn(user, Arrays.asList(BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.IN_PROGRESS));
        if (!existingBookings.isEmpty()) {
            throw new Exception("User already has an active or pending booking");
        }

        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new Exception("Worker not found"));
        if (!worker.getIsAvailable() || !isWorkerAvailable(worker)) {
            throw new Exception("Worker is not available");
        }

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new Exception("Category not found");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setWorker(worker);
        booking.setCategory(category);
        booking.setType(BookingType.CATEGORY);
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        booking.setJobDetails(jobDetails);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking acceptBooking(Long bookingId, Long workerId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking not found"));
        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new Exception("Worker not found"));

        if (booking.getType() == BookingType.URGENT && booking.getWorker() != null) {
            throw new Exception("Urgent booking already assigned");
        }
        if (booking.getType() == BookingType.CATEGORY && !worker.equals(booking.getWorker())) {
            throw new Exception("Worker not assigned to this booking");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new Exception("Booking is not pending");
        }
        if (!worker.getIsAvailable() || !isWorkerAvailable(worker)) {
            throw new Exception("Worker is not available");
        }

        booking.setWorker(worker);
        booking.setStatus(BookingStatus.ACCEPTED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long bookingId, Long workerId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking not found"));
        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new Exception("Worker not found"));

        if (booking.getType() == BookingType.CATEGORY && !worker.equals(booking.getWorker())) {
            throw new Exception("Worker not assigned to this booking");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new Exception("Booking is not pending");
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getWorkerBookings(Long workerId) throws Exception {
        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new Exception("Worker not found"));
        return bookingRepository.findByWorker(worker);
    }

    public List<Booking> getPendingUrgentBookings() {
        return bookingRepository.findByStatuses(Arrays.asList(BookingStatus.PENDING));
    }

    private boolean isWorkerAvailable(Worker worker) {
        List<Booking> activeBookings = bookingRepository.findActiveBookingsByWorker(worker);
        return activeBookings.isEmpty();
    }

    public Booking getBookingById(Long bookingId) throws Exception {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking not found"));
    }
}