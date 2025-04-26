package tarabaho.tarabaho.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tarabaho.tarabaho.entity.Message;
import tarabaho.tarabaho.service.MessageService;
import tarabaho.tarabaho.service.UserService;
import tarabaho.tarabaho.service.WorkerService;

@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Message Controller", description = "Handles chat messages for bookings")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @Operation(summary = "Send message", description = "Sends a message in a booking chat")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Message sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "User or worker not authenticated"),
        @ApiResponse(responseCode = "404", description = "Booking or sender not found")
    })
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageRequest request,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated.");
            }
            String username = authentication.getName();
            boolean isUser = userService.findByUsername(username).isPresent();
            Long senderId = isUser ? userService.findByUsername(username).get().getId() :
                workerService.findByUsername(username).get().getId();

            Message message = messageService.sendMessage(
                request.getBookingId(),
                senderId,
                isUser,
                request.getContent()
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("⚠️ " + e.getMessage());
        }
    }

    @Operation(summary = "Get booking messages", description = "Retrieves all messages for a booking")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of messages"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "User or worker not authenticated"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getBookingMessages(
            @PathVariable Long bookingId,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated.");
            }
            String username = authentication.getName();
            boolean isUser = userService.findByUsername(username).isPresent();
            Long requesterId = isUser ? userService.findByUsername(username).get().getId() :
                workerService.findByUsername(username).get().getId();

            List<Message> messages = messageService.getBookingMessages(bookingId, requesterId, isUser);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("⚠️ " + e.getMessage());
        }
    }

    static class MessageRequest {
        private Long bookingId;
        private String content;

        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}