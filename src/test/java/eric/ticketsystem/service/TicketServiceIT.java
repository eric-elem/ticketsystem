package eric.ticketsystem.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.model.SeatStatus;
import eric.ticketsystem.repository.VenueRepository;
import eric.ticketsystem.repository.impl.VenueRepositoryImpl;
import eric.ticketsystem.service.TicketService;
import eric.ticketsystem.service.impl.TicketServiceImpl;

public class TicketServiceIT {
	final int NUM_OF_SEATS = 20;
	final String customerEmail = "customer@mail.com";
	final String anotheCustomersEmail = "another.customer@mail.com";
	final String invalidCustomerEmail = "someinvalidemail";
	VenueRepository venuRepository;
	TicketService ticketService;

	@Before
	public void setUp() {
		venuRepository = new VenueRepositoryImpl(NUM_OF_SEATS);
		ticketService = new TicketServiceImpl(venuRepository);
	}

	@Test
	public void getsCorrectNumberOfSeats() {
		assertTrue(ticketService.numSeatsAvailable() == NUM_OF_SEATS);
	}

	@Test
	public void findsAndHoldsSeats() {
		int numOfSeatsToHold = NUM_OF_SEATS - 1;

		ticketService.findAndHoldSeats(numOfSeatsToHold, customerEmail);

		assertEquals(NUM_OF_SEATS - numOfSeatsToHold, ticketService.numSeatsAvailable());
	}

	@Test
	public void doesNotCreateSeatHoldWithZeroSeats() {
		SeatHold seatHold = ticketService.findAndHoldSeats(0, customerEmail);

		assertNull(seatHold);
	}

	@Test
	public void doesNotHoldsSeatsIfRequiredSeatsMoreThanAvailable() {
		int numOfSeatsToHold = NUM_OF_SEATS + 1;

		ticketService.findAndHoldSeats(numOfSeatsToHold, customerEmail);

		assertEquals(NUM_OF_SEATS, ticketService.numSeatsAvailable());
	}

	@Test
	public void doesNotHoldSeatsIfCustomerEmailIsInvalid() {
		ticketService.findAndHoldSeats(NUM_OF_SEATS, invalidCustomerEmail);
		ticketService.findAndHoldSeats(NUM_OF_SEATS, null);

		assertNotEquals(NUM_OF_SEATS, 0);
	}

	@Test
	public void reservesHeldSeatsWithValidSeatHoldIdAndCustomerEmail() {
		SeatHold seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, customerEmail);
		Seat seat = seatHold.getSeats().get(0);
		assertFalse(seat.getStatus() == SeatStatus.RESERVED);

		ticketService.reserveSeats(seatHold.getId(), customerEmail);

		assertTrue(seat.getStatus() == SeatStatus.RESERVED);
	}

	@Test
	public void doesNotReserveSeatsWithUnknownSeatHoldId() {
		SeatHold seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, customerEmail);
		Seat seat = seatHold.getSeats().get(0);
		int randomUnknownSeatHoldId = -1213;

		ticketService.reserveSeats(randomUnknownSeatHoldId, customerEmail);

		assertFalse(seat.getStatus() == SeatStatus.RESERVED);
	}

	@Test
	public void doesNotReserveSeatsWithInvalidCustomerEmail() {
		SeatHold seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, customerEmail);
		Seat seat = seatHold.getSeats().get(0);

		ticketService.reserveSeats(seatHold.getId(), invalidCustomerEmail);

		assertFalse(seat.getStatus() == SeatStatus.RESERVED);
	}

	@Test
	public void doesNotReserveSeatsWithUnmatchingCustomerEmail() {
		SeatHold seatHold = ticketService.findAndHoldSeats(NUM_OF_SEATS, customerEmail);
		Seat seat = seatHold.getSeats().get(0);

		ticketService.reserveSeats(seatHold.getId(), anotheCustomersEmail);

		assertFalse(seat.getStatus() == SeatStatus.RESERVED);
	}

}
