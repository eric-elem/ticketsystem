package eric.ticketsystem.util;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import eric.ticketsystem.utility.Utility;

public class UtilityTest {

	@Test
	public void rejectsNullAsInvalidEmailAddress() {
		assertFalse(Utility.isValidEmail(null));
	}

}
