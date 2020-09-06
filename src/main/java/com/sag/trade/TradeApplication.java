package com.sag.trade;

import com.sag.trade.entity.Trade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import process.ProcessData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootApplication
public class TradeApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(TradeApplication.class, args);
		try {
			do {
				startTradeProcess();
				Thread.sleep(30000);
			} while (1 > 0);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public static String startTradeProcess()
	{
		System.out.println("Process started after sleep");
		String status = "";

		try {
			ProcessData processData = new ProcessData();
			List<Trade> processedList = new ArrayList<Trade>();

			if (new File("C:\\Users\\Admin\\TradeFile_Input\\Input\\TradeFile1.csv").exists()) {
				Scanner sc = new Scanner(new File("C:\\Users\\Admin\\TradeFile_Input\\Input\\TradeFile1.csv"));   // --- C:\\Users\\Admin\\TradeFile_Input\\File1.txt
				sc.useDelimiter(",");
				sc.nextLine(); // to Skip header

				while (sc.hasNextLine()) {
					String oneLine = sc.nextLine();
					StringBuffer oneLineBuf = new StringBuffer(oneLine);
					String trId = oneLineBuf.substring(0, oneLineBuf.indexOf(","));
					oneLineBuf = new StringBuffer(oneLineBuf.substring(oneLineBuf.indexOf(",") + 1));
					String version = oneLineBuf.substring(0, oneLineBuf.indexOf(","));
					oneLineBuf = new StringBuffer(oneLineBuf.substring(oneLineBuf.indexOf(",") + 1));
					String cpId = oneLineBuf.substring(0, oneLineBuf.indexOf(","));
					oneLineBuf = new StringBuffer(oneLineBuf.substring(oneLineBuf.indexOf(",") + 1));
					String bookId = oneLineBuf.substring(0, oneLineBuf.indexOf(","));
					oneLineBuf = new StringBuffer(oneLineBuf.substring(oneLineBuf.indexOf(",") + 1));
					String maturityDate = oneLineBuf.toString();

					Random objGenerator = new Random();
					int randomNumber = objGenerator.nextInt(100);
					Date matdate1 = new SimpleDateFormat("dd-MM-yyyy").parse(maturityDate);
					Date todaydate = new Date();
					if (matdate1.compareTo(todaydate) > 0) {  // condition that store does not allow less maturity date than today
						Trade trdata = new Trade();
						// trdata.setId(randomNumber);
						trdata.setTradeid(trId);
						trdata.setVersion(version);
						trdata.setCouterpartyid(cpId);
						trdata.setBookid(bookId);
						trdata.setMaturityDate(maturityDate);
						LocalDate todaydate1 = LocalDate.now();
						trdata.setCreateDate(todaydate1);
						// Write data to store

						Trade trdataprocessed = processData.processAndSaveDataToStrore(trdata);
						processedList.add(trdataprocessed);
						System.out.println("Size of processedList: " + processedList);
					}
				}
				// once all recs are processed then write data to store file
				String st = processData.writeProcessedDataToStoreFile(processedList);
				// After the file is processed,then move the file to another directory
				if (st.equalsIgnoreCase("SUCCESS")) {
					sc.close();
					Path sourcepath = Paths.get("C:\\Users\\Admin\\TradeFile_Input\\Input\\TradeFile1.csv");
					Path destinationepath = Paths.get("C:\\Users\\Admin\\TradeFile_Input\\Processed\\TradeFile1.csv");
					Files.move(sourcepath, destinationepath, StandardCopyOption.REPLACE_EXISTING);
				}


			}
			// Call function to Expire the recs that have crossed maturity in the store
			processData.ExpireStoreRecsThatCrossedMaturity();
			status = status + "SUCCESS";
		}

		catch (Exception ex)
		{
			status = status + "FAILURE";
			ex.printStackTrace();
		}
		return status;

	}

}
