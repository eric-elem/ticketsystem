package eric.ticketsystem.model;

import java.util.LinkedList;
import java.util.List;

public class SeatHold {
	private int id = -1;
	private String customerEmail;
	private List<Seat> seats;
	private int duration;

	public SeatHold(String customerEmail) {
		super();
		this.customerEmail = customerEmail;
		seats = new LinkedList<Seat>();
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
