package eric.ticketsystem.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import eric.ticketsystem.model.Reservation;
import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.repository.VenueRepository;
import eric.ticketsystem.service.TicketService;
import eric.ticketsystem.service.impl.TicketServiceImpl;

public class TicketServiceTest {
	final int NUM_OF_SEATS = 20;
	final String customerEmail = "customer@mail.com";
	final String anotheCustomersEmail = "another.customer@mail.com";
	final String invalidCustomerEmail = "someinvalidemail";
	VenueRepository venuRepository;
	TicketService ticketService;

	@Before
	public void setUp() {
		venuRepository = mock(VenueRepository.class);
		ticketService = new TicketServiceImpl(venuRepository);
	}

	@Test
	public void getsNumberOfSeats() {
		when(venuRepository.getNumOfAvailableSeats()).thenReturn(NUM_OF_SEATS);

		assertTrue(ticketService.numSeatsAvailable() == NUM_OF_SEATS);
		verify(venuRepository).getNumOfAvailableSeats();
	}

	@Test
	public void findsAndHoldsSeatsWhenNumOfSeatsAndCustomerEmailValid() {
		when(venuRepository.getNumOfAvailableSeats()).thenReturn(NUM_OF_SEATS);

		ticketService.findAndHoldSeats(NUM_OF_SEATS, customerEmail);

		verify(venuRepository).getBestSeats(NUM_OF_SEATS);
	}

	@Test
	public void doesNotFindAndHoldZeroSeats() {
		SeatHold seatHold = ticketService.findAndHoldSeats(0, customerEmail);

		assertNull(seatHold);
		verify(venuRepository, never()).findSeatHold(0);
	}

	@Test
	public void doesNotFindAndHoldsSeatsIfRequiredSeatsMoreThanAvailable() {
		int numOfSeatsToHold = NUM_OF_SEATS + 1;

		SeatHold seatHold = ticketService.findAndHoldSeats(numOfSeatsToHold, customerEmail);

		assertNull(seatHold);
		verify(venuRepository, never()).findSeatHold(numOfSeatsToHold);
	}

	@Test
	public void doesNotFindAndHoldSeatsIfCustomerEmailIsInvalid() {
		SeatHold seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, invalidCustomerEmail);

		assertNull(seatHold);
		verify(venuRepository, never()).findSeatHold(NUM_OF_SEATS);

		seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, null);

		assertNull(seatHold);
		verify(venuRepository, never()).findSeatHold(NUM_OF_SEATS);
	}

	@Test
	public void reservesHeldSeatsWithValidSeatHoldIdAndCustomerEmail() {
		int seatHoldId = 123;
		SeatHold seatHold = new SeatHold(customerEmail);
		when(venuRepository.findSeatHold(seatHoldId)).thenReturn(seatHold);

		ticketService.reserveSeats(seatHoldId, customerEmail);

		verify(venuRepository).saveReservation(any(Reservation.class));
	}

	@Test
	public void doesNotReserveSeatsWithUnknownSeatHoldId() {
		int randomUnknownSeatHoldId = -1213;

		ticketService.reserveSeats(randomUnknownSeatHoldId, customerEmail);

		verify(venuRepository, never()).saveReservation(any(Reservation.class));
	}

	@Test
	public void doesNotReserveSeatsWithInvalidCustomerEmail() {
		int seatHoldId = 123;
		SeatHold seatHold = new SeatHold(customerEmail);
		when(venuRepository.findSeatHold(seatHoldId)).thenReturn(seatHold);

		ticketService.reserveSeats(seatHoldId, invalidCustomerEmail);

		verify(venuRepository, never()).saveReservation(any(Reservation.class));
	}

	@Test
	public void doesNotReserveSeatsWithUnmatchingCustomerEmail() {
		int seatHoldId = 123;
		SeatHold seatHold = new SeatHold(customerEmail);
		when(venuRepository.findSeatHold(seatHoldId)).thenReturn(seatHold);

		ticketService.reserveSeats(seatHoldId, anotheCustomersEmail);

		verify(venuRepository, never()).saveReservation(any(Reservation.class));
	}

}
