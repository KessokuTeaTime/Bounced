package band.kessokuteatime.bounced;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class BouncedTest {
	@Test
	void preservesOneImmediateLaunchBounce() {
		Bounced.startTitleIntro(1_000);
		Bounced.update(1_000, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(1_000));

		Bounced.update(1_937, 100);
		assertEquals(0, Bounced.primaryPosition());
		assertFalse(Bounced.isIntro(1_937));

		Bounced.startOnboardingIntro(3_000);
		Bounced.update(3_000, 100);
		assertEquals(-100, Bounced.primaryPosition());

		Bounced.update(3_937, 100);
		Bounced.startOnboardingIntro(5_000);
		Bounced.update(5_000, 100);
		assertEquals(0, Bounced.primaryPosition());

		Bounced.startTitleIntro(6_000);
		Bounced.update(6_000, 100);
		assertEquals(0, Bounced.primaryPosition());
		assertFalse(Bounced.isIntro(6_000));

		Bounced.startTitleIntro(7_000);
		Bounced.update(7_000, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(7_000));
	}
}
