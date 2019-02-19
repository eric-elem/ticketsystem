package eric.ticketsystem.utility;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utility {
	public static int HOLD_DURATION = 30*1000;

	public static boolean isValidEmail(String email) {
		boolean isValid = true;

		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
		} catch (AddressException exception) {
			isValid = false;
		} catch (NullPointerException exception) {
			isValid = false;
		}

		return isValid;
	}
}
