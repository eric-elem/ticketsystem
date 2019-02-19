package eric.ticketsystem.comparator;

import static org.junit.Assert.assertTrue;

import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatStatus;

public class SeatComparatorTest {
	SeatComparator seatComparator = new SeatComparator();
	TreeSet<Seat> seats = new TreeSet<Seat>(seatComparator);
	Seat seat1;
	Seat seat2;

	@Before
	public void setUp() {
		seat1 = new Seat(1, Seat.QUALITY_GOOD);
		seat2 = new Seat(2, Seat.QUALITY_GOOD);
	}

	@Test
	public void freeSeatComesBeforeHeldAndReserved() {
		seat1.setStatus(SeatStatus.HELD);

		seats.add(seat1);
		seats.add(seat2);

		Seat firstSeat = seats.first();
		assertTrue(firstSeat == seat2);
	}

	@Test
	public void betterQualityFreeSeatComesFirst() {
		seat2.setQuality(Seat.QUALITY_BETTER);

		seats.add(seat1);
		seats.add(seat2);

		Seat firstSeat = seats.first();
		assertTrue(firstSeat == seat2);

		seat1.setQuality(Seat.QUALITY_BEST);
		seats.clear();
		seats.add(seat1);
		seats.add(seat2);

		firstSeat = seats.first();
		assertTrue(firstSeat == seat1);
	}

	@Test
	public void lowerIdSeatComesFirstIfStatusAndQualityAreTheSame() {
		seats.add(seat2);
		seats.add(seat1);

		Seat firstSeat = seats.first();
		assertTrue(firstSeat == seat1);

		seat1.setQuality(Seat.QUALITY_BEST);
		seat2.setQuality(Seat.QUALITY_BEST);
		seat1.setStatus(SeatStatus.HELD);
		seat2.setStatus(SeatStatus.HELD);

		seats.add(seat2);
		seats.add(seat1);

		firstSeat = seats.first();
		assertTrue(firstSeat == seat1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsIllegalArgumentExceptionIfEitherOrBothSeatsAreNull() {
		seat1 = null;

		seats.add(seat1);
		seats.add(seat2);
	}

}
