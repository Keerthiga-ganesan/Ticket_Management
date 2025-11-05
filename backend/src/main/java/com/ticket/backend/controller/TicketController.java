package com.ticket.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ticket.backend.entity.Status;
import com.ticket.backend.entity.Ticket;
import com.ticket.backend.repository.TicketRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;

    // STAFF - create ticket
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        ticket.setStatus(Status.NEW);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }

    // MANAGER - get all tickets
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // STAFF - get own tickets
    @GetMapping("/staff/{id}")
    public List<Ticket> getStaffTickets(@PathVariable Long id) {
        return ticketRepository.findByCreatedBy(id);
    }

    // TECHNICIAN - get assigned tickets
    @GetMapping("/technician/{id}")
    public List<Ticket> getTechnicianTickets(@PathVariable Long id) {
        return ticketRepository.findByAssignedTo(id);
    }

    // MANAGER - assign technician
    @PutMapping("/{id}/assign")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Long id, @RequestParam Long technicianId) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setAssignedTo(technicianId);
        ticket.setStatus(Status.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }

    // TECHNICIAN - update status
    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long id, @RequestParam Status status) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }

    // MANAGER - close ticket
    @PutMapping("/{id}/close")
    public ResponseEntity<Ticket> closeTicket(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setStatus(Status.CLOSED);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }
}
