package band.kessokuteatime.bounced;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class BouncedTest {
	@Test
	void startsWithTheFirstVisibleLogoFrame() {
		assertFalse(Bounced.shouldStartIntro(true, false, 0.0F));
		assertTrue(Bounced.shouldStartIntro(true, false, 0.01F));
		assertTrue(Bounced.shouldStartIntro(true, true, 0.0F));
		assertTrue(Bounced.shouldStartIntro(false, false, 0.0F));
	}

	@Test
	void preservesOriginalScreenArmingLifecycle() {
		Bounced.init(true, 1_000);
		Bounced.update(3_000, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(3_000));

		Bounced.resetWhen(true, 3_000);
		Bounced.update(3_000, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(3_000));

		Bounced.update(3_016, 100);
		assertTrue(Bounced.primaryPosition() > -100);

		Bounced.update(3_937, 100);
		assertEquals(0, Bounced.primaryPosition());
		assertFalse(Bounced.isIntro(3_937));

		Bounced.push();
		Bounced.init(true, 3_000);
		Bounced.resetWhen(true, 3_500);
		Bounced.update(3_500, 100);
		assertEquals(-100, Bounced.primaryPosition());
		Bounced.update(4_437, 100);

		Bounced.init(true, 5_000);
		Bounced.resetWhen(true, 5_500);
		Bounced.update(5_500, 100);
		assertEquals(0, Bounced.primaryPosition());

		Bounced.push();
		Bounced.init(true, 6_000);
		Bounced.resetWhen(true, 6_000);
		Bounced.update(6_000, 100);
		assertEquals(-100, Bounced.primaryPosition());
		assertTrue(Bounced.isIntro(6_000));
	}
}
