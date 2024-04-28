package com.example.cashcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

// Extends pagingandsortingrepository to provide additional methods to retrieve entities using
// pagination and sorting.
interface CashCardRepository
        extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {
}
