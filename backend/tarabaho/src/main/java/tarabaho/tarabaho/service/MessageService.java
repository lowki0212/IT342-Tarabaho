package tarabaho.tarabaho.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.entity.Booking;
import tarabaho.tarabaho.entity.BookingStatus;
import tarabaho.tarabaho.entity.Message;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.BookingRepository;
import tarabaho.tarabaho.repository.MessageRepository;
import tarabaho.tarabaho.repository.UserRepository;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    public Message sendMessage(Long bookingId, Long senderId, boolean isUser, String content) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking not found"));
        if (booking.getStatus() != BookingStatus.ACCEPTED && booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new Exception("Chat is only available for accepted or in-progress bookings");
        }

        Message message = new Message();
        message.setBooking(booking);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());

        if (isUser) {
            User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("User not found"));
            if (!sender.equals(booking.getUser())) {
                throw new Exception("User not authorized for this booking");
            }
            message.setSenderUser(sender);
        } else {
            Worker sender = workerRepository.findById(senderId)
                .orElseThrow(() -> new Exception("Worker not found"));
            if (!sender.equals(booking.getWorker())) {
                throw new Exception("Worker not authorized for this booking");
            }
            message.setSenderWorker(sender);
        }

        return messageRepository.save(message);
    }

    public List<Message> getBookingMessages(Long bookingId, Long requesterId, boolean isUser) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new Exception("Booking not found"));
        if (isUser && !booking.getUser().getId().equals(requesterId)) {
            throw new Exception("User not authorized for this booking");
        }
        if (!isUser && (booking.getWorker() == null || !booking.getWorker().getId().equals(requesterId))) {
            throw new Exception("Worker not authorized for this booking");
        }
        return messageRepository.findByBookingOrderBySentAtAsc(booking);
    }
}