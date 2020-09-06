package com.sag.trade;

import com.sag.trade.entity.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import process.ProcessData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class TradeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void teststartTradeProcess()
	{
		String st = TradeApplication.startTradeProcess();
		assertEquals(st,"SUCCESS");
	}
	@Test
	void testProcessDataToDeceideifWriteAndOverrite()
	{
		Trade trdata = new Trade();
		trdata.setTradeid("T6");
		trdata.setVersion("1");
		trdata.setCouterpartyid("CP6");
		trdata.setBookid("B1");
		trdata.setMaturityDate("20-05-2021");
		List<Trade> trList = new ArrayList<Trade>();
		trList.add(trdata);
		ProcessData pr = new ProcessData();
		String st  = pr.writeProcessedDataToStoreFile(trList);
		assertEquals(st,"SUCCESS");


	}
	@Test
	void testprocessAndSaveDataToStrore()
	{
		Trade trdata = new Trade();
		trdata.setTradeid("T6");
		trdata.setVersion("1");
		trdata.setCouterpartyid("CP6");
		trdata.setBookid("B1");
		trdata.setMaturityDate("20-05-2021");
		ProcessData pr = new ProcessData();
		Trade trdataprocessed = pr.processAndSaveDataToStrore(trdata);
		assertNotNull(trdataprocessed.getValid());

	}
	@Test
	void testExpireStoreRecsThatCrossedMaturity()
	{
		ProcessData pr = new ProcessData();
		pr.ExpireStoreRecsThatCrossedMaturity();
		assertNotNull(pr);
	}

}
