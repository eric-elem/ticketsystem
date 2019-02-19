package eric.ticketsystem.model;

public class Seat {
	private int number;
	private int quality;
	private SeatStatus status;
	public static final int QUALITY_BEST = 3;
	public static final int QUALITY_BETTER = 2;
	public static final int QUALITY_GOOD = 1;

	public Seat(int number, int quality) {
		super();
		this.number = number;
		this.quality = quality;
		this.status = SeatStatus.FREE;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public SeatStatus getStatus() {
		return status;
	}

	public void setStatus(SeatStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Seat [number=" + number + ", quality=" + quality + ", status=" + status + "]";
	}
}
