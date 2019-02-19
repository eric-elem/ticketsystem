package eric.ticketsystem.service.impl;

import java.util.List;

import eric.ticketsystem.model.Reservation;
import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.model.SeatStatus;
import eric.ticketsystem.repository.VenueRepository;
import eric.ticketsystem.service.TicketService;
import eric.ticketsystem.utility.Utility;

public class TicketServiceImpl implements TicketService {
	VenueRepository venuRepository;

	public TicketServiceImpl(VenueRepository venuRepository) {
		this.venuRepository = venuRepository;
	}

	public int numSeatsAvailable() {
		return venuRepository.getNumOfAvailableSeats();
	}

	public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		SeatHold seatHold = null;

		if (numSeats > 0 && numSeats <= numSeatsAvailable() && Utility.isValidEmail(customerEmail)) {
			List<Seat> bestSeats = venuRepository.getBestSeats(numSeats);

			for (Seat seat : bestSeats) {
				seat.setStatus(SeatStatus.HELD);
			}
			venuRepository.addSeats(bestSeats);

			seatHold = new SeatHold(customerEmail);
			seatHold.setSeats(bestSeats);
			seatHold.setDuration(Utility.HOLD_DURATION);
			venuRepository.saveSeatHold(seatHold);

		}

		return seatHold;
	}

	public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
		String code = null;
		SeatHold seatHold = venuRepository.findSeatHold(seatHoldId);

		if (seatHold != null && Utility.isValidEmail(customerEmail)
				&& customerEmail.equals(seatHold.getCustomerEmail())) {

			for (Seat seat : seatHold.getSeats()) {
				seat.setStatus(SeatStatus.RESERVED);
			}

			Reservation reservation = new Reservation(seatHold);
			venuRepository.saveReservation(reservation);
			code = reservation.getCode();

		}

		return code;
	}

}
