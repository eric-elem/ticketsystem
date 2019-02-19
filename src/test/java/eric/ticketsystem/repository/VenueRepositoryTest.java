package eric.ticketsystem.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eric.ticketsystem.model.Reservation;
import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.model.SeatStatus;
import eric.ticketsystem.repository.impl.VenueRepositoryImpl;
import eric.ticketsystem.utility.Utility;

public class VenueRepositoryTest {
	final int NUM_OF_SEATS = 20;
	final String CUSTOMER_EMAIL = "customer@mail.com";
	VenueRepository venueRepository;

	@Before
	public void setUp() {
		venueRepository = new VenueRepositoryImpl(NUM_OF_SEATS);
	}

	@Test
	public void generatesCorrectNumberOfSeats() {
		assertEquals(NUM_OF_SEATS, venueRepository.getNumOfAvailableSeats());
	}

	@Test
	public void savesSeatHold() {
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);

		venueRepository.saveSeatHold(seatHold);

		assertEquals(seatHold, venueRepository.findSeatHold(seatHold.getId()));
	}

	@Test
	public void expiresSeatHold() throws InterruptedException {
		Utility.HOLD_DURATION = 100;
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);
		Seat seat = venueRepository.getBestSeats(1).get(0);
		seat.setStatus(SeatStatus.HELD);
		seatHold.getSeats().add(seat);

		venueRepository.saveSeatHold(seatHold);
		Thread.sleep(Utility.HOLD_DURATION);

		assertEquals(SeatStatus.FREE, seat.getStatus());
	}

	@Test
	public void doesNotExpireSeatHoldWithReservedSeats() throws InterruptedException {
		Utility.HOLD_DURATION = 100;
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);
		Seat seat = venueRepository.getBestSeats(1).get(0);
		seat.setStatus(SeatStatus.RESERVED);
		seatHold.getSeats().add(seat);

		venueRepository.saveSeatHold(seatHold);
		Thread.sleep(Utility.HOLD_DURATION);

		assertNotEquals(SeatStatus.FREE, seat.getStatus());
	}

	@Test
	public void generatesSeatHoldId() {
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);
		assertEquals(-1, seatHold.getId());

		venueRepository.saveSeatHold(seatHold);

		assertNotEquals(-1, seatHold.getId());
	}

	@Test
	public void getsBestSeats() {
		Seat seat = venueRepository.getBestSeats(1).get(0);

		assertTrue(Seat.QUALITY_BEST == seat.getQuality());
	}

	@Test
	public void savesReservation() {
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);
		venueRepository.saveSeatHold(seatHold);
		Reservation reservation = new Reservation(seatHold);

		venueRepository.saveReservation(reservation);

		assertEquals(reservation, venueRepository.findReservation(reservation.getCode()));
	}

	@Test
	public void generatesReservationCode() {
		SeatHold seatHold = new SeatHold(CUSTOMER_EMAIL);
		venueRepository.saveSeatHold(seatHold);
		Reservation reservation = new Reservation(seatHold);
		assertNull(reservation.getCode());

		venueRepository.saveReservation(reservation);

		assertNotNull(reservation.getCode());
	}

	@Test
	public void updatesSeats() {
		List<Seat> seats = venueRepository.getBestSeats(1);
		Seat seat1 = seats.get(0);
		seat1.setStatus(SeatStatus.RESERVED);

		venueRepository.addSeats(seats);

		Seat seat2 = venueRepository.getBestSeats(1).get(0);
		assertNotEquals(seat1, seat2);
	}

}
