package farayan.commans;

import org.junit.Assert;
import org.junit.Test;

import farayan.commons.Commons.Rial;

import static java.lang.Double.compare;


public class RialTests
{
	@Test
	public void correct() {
		Rial rial = null;

		rial = new Rial(10000.0,0);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.HezarTooman);
		Assert.assertEquals(0, compare(rial.Displayable, 1d));

		rial = new Rial(12000.0,0);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.Tooman);
		Assert.assertEquals(0, compare(rial.Displayable, 1200d));

		rial = new Rial(12300.0,0);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.Tooman);
		Assert.assertEquals(0, compare(rial.Displayable, 1230d));

		rial = new Rial(12340.0,0);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.Tooman);
		Assert.assertEquals(0, compare(rial.Displayable, 1234d));

		rial = new Rial(12345.0,0);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.Rial);
		Assert.assertEquals(0, compare(rial.Displayable, 12345d));

		rial = new Rial(125000, 3);
		Assert.assertSame(rial.Coefficient, Rial.Coefficients.HezarTooman);
		Assert.assertEquals(0, compare(rial.Displayable, 12.5d));
	}
}
