package eric.ticketsystem.comparator;

import java.util.Comparator;

import eric.ticketsystem.model.Seat;
import eric.ticketsystem.model.SeatStatus;

public class SeatComparator implements Comparator<Seat> {

	public int compare(Seat seat1, Seat seat2) {
		if(seat1 == null || seat2 == null) {
			throw new IllegalArgumentException();
		}
		
		if (seat1.getStatus() == SeatStatus.FREE && seat2.getStatus() != SeatStatus.FREE) {
			return -1;
		} else if (seat1.getStatus() == SeatStatus.FREE && seat2.getStatus() == SeatStatus.FREE) {
			if(seat1.getQuality() != seat2.getQuality()) {
				return seat2.getQuality() - seat1.getQuality();
			}
			
		}
		
		return Integer.compare(seat1.getNumber(), seat2.getNumber());
	}

}
