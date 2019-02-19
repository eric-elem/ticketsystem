package eric.ticketsystem.model;

public class Reservation {
	private String code;
	private SeatHold seatHold;

	public Reservation(SeatHold seatHold) {
		super();
		this.seatHold = seatHold;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SeatHold getSeatHold() {
		return seatHold;
	}

	public void setSeatHold(SeatHold seatHold) {
		this.seatHold = seatHold;
	}

}
