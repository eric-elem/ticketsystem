package eric.ticketsystem.repository;

import java.util.List;

import eric.ticketsystem.model.Reservation;
import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatHold;

public interface VenueRepository {
	int getNumOfAvailableSeats();

	List<Seat> getBestSeats(int numOfSeats);

	void saveSeatHold(SeatHold seatHold);

	SeatHold findSeatHold(int seatHoldId);

	void saveReservation(Reservation reservation);
	
	Reservation findReservation(String reservationCode);
	
	void addSeats(List<Seat> seats);
}
