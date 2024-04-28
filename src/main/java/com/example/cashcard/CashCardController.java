package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    ResponseEntity<CashCard> findById(@PathVariable("requestedId") Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest,
            UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);
        URI locationOfNewCashCard =
                ucb.path("cashcards/{id}").buildAndExpand(savedCashCard.id()).toUri();

        return ResponseEntity.created((locationOfNewCashCard)).build();
    }

    // @GetMapping
    // private ResponseEntity<Iterable<CashCard>> findAll() {
    // System.out.println("findAll: " + cashCardRepository.findAll());
    // return ResponseEntity.ok(cashCardRepository.findAll());
    // }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        // PageRequest is a basic Java Bean implementation of Pageable. Things that want paging and
        // sorting implementation often support this, such as some types of Spring Data
        // Repositories.
        // The getSort() method extracts the sort query parameter from the request URI.
        Page<CashCard> page = cashCardRepository.findAll(PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
        return ResponseEntity.ok(page.getContent());
    }
}
