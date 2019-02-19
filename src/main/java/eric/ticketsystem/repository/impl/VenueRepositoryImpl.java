package eric.ticketsystem.repository.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import eric.ticketsystem.comparator.SeatComparator;
import eric.ticketsystem.model.Reservation;
import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.model.SeatStatus;
import eric.ticketsystem.repository.VenueRepository;

public class VenueRepositoryImpl implements VenueRepository {
	PriorityQueue<Seat> seats;
	Map<Integer, SeatHold> seatHolds = new HashMap<Integer, SeatHold>();
	Map<String, Reservation> reservations = new HashMap<String, Reservation>();
	int availableSeats;

	public VenueRepositoryImpl(int numOfSeats) {
		seats = new PriorityQueue<Seat>(new SeatComparator());
		generateSeats(numOfSeats);
		availableSeats = numOfSeats;
	}

	public int getNumOfAvailableSeats() {
		return availableSeats;
	}

	public void saveSeatHold(SeatHold seatHold) {
		int seatHoldId = generateSeatHoldId();

		seatHold.setId(seatHoldId);
		seatHolds.put(seatHoldId, seatHold);
		availableSeats -= seatHold.getSeats().size();
		triggerSeatHoldExpiration(seatHold);
	}

	private int generateSeatHoldId() {
		Random random = new Random();

		int seatHoldId = random.nextInt(Integer.MAX_VALUE);
		while (seatHolds.containsKey(seatHoldId)) {
			seatHoldId = random.nextInt(Integer.MAX_VALUE);
		}

		return seatHoldId;
	}

	public List<Seat> getBestSeats(int numOfSeats) {
		List<Seat> bestSeats = new LinkedList<Seat>();

		for (int i = 0; i < numOfSeats; i++) {
			bestSeats.add(seats.poll());
		}

		return bestSeats;
	}

	private void generateSeats(int numOfSeats) {
		for (int i = 1; i <= numOfSeats; i++) {
			Seat seat = null;

			if (i % 5 == 0) {
				seat = new Seat(i, Seat.QUALITY_BETTER);
			} else if (i % 9 == 0) {
				seat = new Seat(i, Seat.QUALITY_BEST);
			} else {
				seat = new Seat(i, Seat.QUALITY_GOOD);
			}

			seats.add(seat);
		}
	}

	private String getReservationCode(SeatHold seatHold) {
		String code = seatHold.getCustomerEmail().substring(0, 2) + seatHold.getId();

		return code;
	}

	public SeatHold findSeatHold(int seatHoldId) {
		SeatHold seatHold = null;

		if (seatHolds.containsKey(seatHoldId)) {
			seatHold = seatHolds.get(seatHoldId);
		}

		return seatHold;
	}

	public void saveReservation(Reservation reservation) {
		String code = getReservationCode(reservation.getSeatHold());

		reservation.setCode(code);
		reservations.put(code, reservation);
	}

	public Reservation findReservation(String reservationCode) {
		Reservation reservation = null;

		if (reservations.containsKey(reservationCode)) {
			reservation = reservations.get(reservationCode);
		}

		return reservation;
	}

	public void addSeats(List<Seat> changedSeats) {
		for (Seat seat : changedSeats) {
			seats.add(seat);
		}
	}

	private void triggerSeatHoldExpiration(final SeatHold seatHold) {
		Thread handleExpiration = new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(seatHold.getDuration());
					if (seatHold.getSeats().size() > 0 && seatHold.getSeats().get(0).getStatus() == SeatStatus.HELD) {
						for (Seat seat : seatHold.getSeats()) {
							seat.setStatus(SeatStatus.FREE);
							seats.remove(seat);
						}
						addSeats(seatHold.getSeats());
						availableSeats += seatHold.getSeats().size();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});

		handleExpiration.start();
	}
}
