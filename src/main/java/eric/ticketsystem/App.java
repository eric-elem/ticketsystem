package eric.ticketsystem;

import java.util.Scanner;

import eric.ticketsystem.model.SeatHold;
import eric.ticketsystem.repository.VenueRepository;
import eric.ticketsystem.repository.impl.VenueRepositoryImpl;
import eric.ticketsystem.service.TicketService;
import eric.ticketsystem.service.impl.TicketServiceImpl;
import eric.ticketsystem.utility.Utility;

public class App {
	static Scanner scanner;
	static TicketService ticketService;

	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		int numOfSeats = -1;

		System.out.println("\n\nWelcome To Our Ticketing System :-)\n\n");

		while (numOfSeats < 1) {
			System.out.print("Enter the number of seats available at your venue: ");
			String numOfSeatsStr = scanner.nextLine();

			try {
				numOfSeats = Integer.parseInt(numOfSeatsStr);
			} catch (NumberFormatException exception) {
				continue;
			}
		}

		VenueRepository venuRepository = new VenueRepositoryImpl(numOfSeats);
		ticketService = new TicketServiceImpl(venuRepository);

		System.out.println();

		while (true) {
			System.out.print("Enter 1 to view available seats, 2 to hold and 3 to reserve seats, or 4 to exit: ");
			String selectedOptionStr = scanner.nextLine();
			int selectedOption = -1;

			try {
				selectedOption = Integer.parseInt(selectedOptionStr);
				if (selectedOption < 1 || selectedOption > 4) {
					continue;
				}
			} catch (NumberFormatException exception) {
				continue;
			}

			if (selectedOption == 1) {
				System.out.println("\nAvailable seats: " + ticketService.numSeatsAvailable() + "\n");
			} else if (selectedOption == 2) {
				holdSeats();
			} else if (selectedOption == 3) {
				reserveSeats();
			} else {
				break;
			}
		}

		scanner.close();
	}

	private static void holdSeats() {
		String customerEmail = null;

		System.out.println();

		while (!Utility.isValidEmail(customerEmail)) {
			System.out.print("Enter customer's email: ");
			customerEmail = scanner.nextLine();

			if (Utility.isValidEmail(customerEmail)) {
				int numOfSeatsToHold = 0;
				while (numOfSeatsToHold == 0) {
					System.out.print("Enter number of seats to hold: ");
					String numOfSeatsToHoldStr = scanner.nextLine();

					try {
						numOfSeatsToHold = Integer.parseInt(numOfSeatsToHoldStr);
						SeatHold seatHold = ticketService.findAndHoldSeats(numOfSeatsToHold, customerEmail);
						if (seatHold == null) {
							numOfSeatsToHold = 0;
							continue;
						}

						System.out.println("\nSuccess. Seat hold id is " + seatHold.getId() + " with customer email "
								+ seatHold.getCustomerEmail() + "\n");

					} catch (NumberFormatException exception) {
						continue;
					}
				}
			}

		}
	}

	private static void reserveSeats() {
		String customerEmail = null;

		System.out.println();

		while (!Utility.isValidEmail(customerEmail)) {
			System.out.print("Enter customer's email: ");
			customerEmail = scanner.nextLine();

			if (Utility.isValidEmail(customerEmail)) {
				int seatHoldId = -1;
				
				while (seatHoldId < 0) {
					System.out.print("Enter seat hold id: ");
					String seatHoldIdStr = scanner.nextLine();

					try {
						seatHoldId = Integer.parseInt(seatHoldIdStr);
						String reservationId = ticketService.reserveSeats(seatHoldId, customerEmail);
						if (reservationId == null) {
							customerEmail = null;
							continue;
						}

						System.out.println("\nSuccess. Your reservation id is " + reservationId + "\n");

					} catch (NumberFormatException exception) {
						continue;
					}
				}
			}

		}
	}
}
