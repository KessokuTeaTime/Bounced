package band.kessokuteatime.bounced;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class BouncedTest {
	@Test
	void preservesOneImmediateLaunchBounce() {
		Bounced.startTitleIntro();
		Bounced.update(1_600, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(1_600));

		Bounced.update(2_537, 100);
		assertEquals(0, Bounced.primaryPosition());
		assertFalse(Bounced.isIntro(2_537));

		Bounced.startOnboardingIntro();
		Bounced.update(3_200, 100);
		assertEquals(-100, Bounced.primaryPosition());

		Bounced.update(4_137, 100);
		Bounced.startOnboardingIntro();
		Bounced.update(5_000, 100);
		assertEquals(0, Bounced.primaryPosition());

		Bounced.startTitleIntro();
		Bounced.update(6_000, 100);
		assertEquals(0, Bounced.primaryPosition());
		assertFalse(Bounced.isIntro(6_000));

		Bounced.startTitleIntro();
		Bounced.update(7_600, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(7_600));
	}
}
