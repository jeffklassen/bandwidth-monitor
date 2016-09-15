package monapi;

import junit.framework.Assert;
import net.jklass.monapi.dao.ESBandwidthDao;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ESBandwidthAggTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		ESBandwidthDao dao = new ESBandwidthDao();
		dao.getDetails(DateTime.now().minusDays(5), DateTime.now());
		Assert.assertTrue(true);
	}

}
