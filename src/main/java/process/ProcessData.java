package process;

import TradeException.LowerVersionTradeException;
import com.sag.trade.entity.Trade;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class ProcessData {


    public Trade  processAndSaveDataToStrore(Trade trdata)
    {
        //System.out.println("In processAndSaveDataToStrore");
        //System.out.println(trdata.getTradeid());
        //System.out.println(trdata.getMaturityDate());
        Trade trdataprocessed = processDataToDeceideifWriteAndOverrite(trdata);
        return trdataprocessed;

    }

    public void ExpireStoreRecsThatCrossedMaturity()
    {
        String excelFilePath = "C:\\Users\\Admin\\TradeFile_Processed\\store.xls";
        try {
            if (new File(excelFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                HSSFSheet sheet = workbook.getSheetAt(0);
                int rowNum = 0;
                for (Row row : sheet) { // For each Row.
                    if (row.getRowNum() == 0)
                        continue; // Igonore the first row in the store.
                    Cell cellMaturityDate = row.getCell(4); // Get the cell for maturity date
                    Cell cellExpired = row.getCell(6);

                    String maturityDateInStore = cellMaturityDate.getStringCellValue();
                    System.out.println("value of maturityDateInStore:" + maturityDateInStore);
                    Date matdateinStore = new SimpleDateFormat("dd-MM-yyyy").parse(maturityDateInStore);
                    Date todaydate = new Date();
                    if (matdateinStore.compareTo(todaydate) < 0) {
                        // set the Expired ind to Y
                        System.out.println(" set the Expired ind to Y");
                        if(null != cellExpired) {
                            cellExpired.setCellValue("Y");
                        }
                        else {
                            cellExpired = sheet.getRow(cellMaturityDate.getRowIndex()).createCell(6);
                            cellExpired.setCellValue("Y");
                        }

                    } else {
                        // set the Expired ind to N
                        System.out.println(" set the Expired ind to N");
                        if(null != cellExpired) {
                            cellExpired.setCellValue("N");
                        }
                        else {
                            cellExpired = sheet.getRow(cellMaturityDate.getRowIndex()).createCell(6);
                            cellExpired.setCellValue("N");
                        }
                    }
                }
                inputStream.close();
                FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Admin\\TradeFile_Processed\\store.xls");
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            }
        }
        catch (Exception ex)
        {

            ex.printStackTrace();
        }
    }

    public String writeProcessedDataToStoreFile(List<Trade> trdata)
    {
        String excelFilePath = "C:\\Users\\Admin\\TradeFile_Processed\\store.xls";
        String status = "";
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNumMatching = 0;

            for (int i = 0; i < trdata.size(); i++) {
                 System.out.println("trdata.get(i).getValid():" + trdata.get(i).getValid());
                 System.out.println("trdata.get(i).getShouldOverride():" + trdata.get(i).getShouldOverride());
                if (null != trdata.get(i).getValid())
                {
                    if (trdata.get(i).getValid() == true && trdata.get(i).getShouldOverride() == false) {
                        // write new rec at the end of the store sheet.
                        int rowCount = sheet.getLastRowNum();
                        Row row = sheet.createRow(++rowCount);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(rowCount);
                        cell.setCellValue(trdata.get(i).getTradeid());
                        cell = row.createCell(1);
                        cell.setCellValue(trdata.get(i).getVersion());
                        cell = row.createCell(2);
                        cell.setCellValue(trdata.get(i).getCouterpartyid());
                        cell = row.createCell(3);
                        cell.setCellValue(trdata.get(i).getBookid());
                        cell = row.createCell(4);
                        cell.setCellValue(trdata.get(i).getMaturityDate().toString());
                        cell = row.createCell(5);
                        cell.setCellValue(trdata.get(i).getCreateDate().toString());

                    }
                    if(trdata.get(i).getValid() == true && trdata.get(i).getShouldOverride() == true)
                    {
                        System.out.println("Trade rec come for update is:" + trdata.get(i).getTradeid() + " Version is: " + trdata.get(i).getVersion());
                        // when we want to overwrite an existing rec in Store
                        for (Row row : sheet) { // For each Row.
                            if (row.getRowNum() == 0)
                                continue; // ignore the first row in the store
                            //rowNumMatching = rowNumMatching +1;
                            Cell cellTrId = row.getCell(0); // Get the Cell at the Index / Column you want.
                            Cell cellversion = row.getCell(1);
                            cellversion.setCellType(Cell.CELL_TYPE_STRING);
                            String trId = cellTrId.getStringCellValue();
                            String Version = cellversion.getStringCellValue();
                           // int unqInt = (int)Math.round(unqId);
                            System.out.println("value of Version for row to be updated is:" + Version);
                            if(trdata.get(i).getTradeid().equalsIgnoreCase(trId) && trdata.get(i).getVersion().equalsIgnoreCase(Version))
                            {
                                rowNumMatching = cellversion.getRowIndex();
                                Cell cell2Update = sheet.getRow(rowNumMatching).getCell(2);
                                cell2Update.setCellValue(trdata.get(i).getCouterpartyid());
                                cell2Update = sheet.getRow(rowNumMatching).getCell(3);
                                cell2Update.setCellValue(trdata.get(i).getBookid());
                                cell2Update = sheet.getRow(rowNumMatching).getCell(4);
                                cell2Update.setCellValue(trdata.get(i).getMaturityDate());
                                cell2Update = sheet.getRow(rowNumMatching).getCell(5);
                                cell2Update.setCellValue(trdata.get(i).getCreateDate().toString());
                            }
                        }
                    }
            }
            }
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Admin\\TradeFile_Processed\\store.xls");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            status = status + "SUCCESS";
        }
        catch (Exception ex)
        {
            status = status + "FAILURE";
           ex.printStackTrace();
        }
        return status;
    }

    private Trade processDataToDeceideifWriteAndOverrite(Trade trdata)
    {
        String excelFilePath = "C:\\Users\\Admin\\TradeFile_Processed\\store.xls";
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
             System.out.println("value of getPhysicalNumberOfRows:" + sheet.getPhysicalNumberOfRows());
            Cell cell = null;
            if(sheet.getPhysicalNumberOfRows() == 1)
            { // Store file does not have any row,then all the data in the input file need to be added to Store.
                trdata.setValid(true);
                trdata.setShouldOverride(false);
            }
            else {
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    cell = sheet.getRow(i).getCell(0);
                    String trIdInStore = cell.getStringCellValue();
                    cell = sheet.getRow(i).getCell(1);
                    String versioninst = cell.getStringCellValue();


                        if (trdata.getTradeid().equalsIgnoreCase(trIdInStore) && (Integer. parseInt(trdata.getVersion()) >  Integer. parseInt(versioninst)) && trdata.getValid() == null && trdata.getShouldOverride() == null) {
                            // mark the record as valid and new rec
                            trdata.setValid(true);
                            trdata.setShouldOverride(false); // higher version of trade has come and it shud be an insert
                            System.out.println("higher version of trade has come and it shud be an insert");
                            System.out.println("First case value of trdata.getVersion(): " + trdata.getVersion());
                            System.out.println("First Case value of versionst: " + versioninst);
                        }
                        if (trdata.getTradeid().equalsIgnoreCase(trIdInStore) && (Integer. parseInt(trdata.getVersion()) < Integer. parseInt(versioninst)) && trdata.getValid() == null && trdata.getShouldOverride() == null) {
                            // mark the record as not valid
                            System.out.println("Lower version of trade has come which is not allowed");
                            System.out.println("Second case value of trdata.getVersion(): " + trdata.getVersion());
                            System.out.println("Second Case value of versionst: " + versioninst);
                            trdata.setValid(false); // Lower version of trade has come which is not allowed
                            trdata.setShouldOverride(false);
                            throw new LowerVersionTradeException("Lower version of Trade is not allowed");

                        }
                        if (trdata.getTradeid().equalsIgnoreCase(trIdInStore) && (trdata.getVersion().equalsIgnoreCase(versioninst)) && trdata.getValid() == null && trdata.getShouldOverride() == null) {
                            // mark the record as  valid but override it
                             trdata.setValid(true);
                             trdata.setShouldOverride(true); // Equal verion has come which shud be updated in store
                             System.out.println(" Equal verion has come which shud be updated in store");
                             System.out.println("Third case value of trdata.getVersion(): " + trdata.getVersion());
                             System.out.println("Third Case value of versionst: " + versioninst);
                        }

                }
                if( trdata.getValid() == null && trdata.getShouldOverride() == null) {
                    trdata.setValid(true);
                    trdata.setShouldOverride(false);
                    System.out.println("Fourth case when its a new trade rec");
                }
            }
        } catch (IOException | EncryptedDocumentException | LowerVersionTradeException
                 ex) {
            ex.printStackTrace();
        }

        return trdata;
    }



    }

