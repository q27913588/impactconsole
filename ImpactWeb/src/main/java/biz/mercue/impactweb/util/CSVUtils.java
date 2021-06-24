package biz.mercue.impactweb.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;



import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import biz.mercue.impactweb.model.Account;


public class CSVUtils {
	
	private static Logger log = Logger.getLogger(CSVUtils.class.getName());
	


	
	public static final List<Account> readCSVFile(String csvFile) throws IOException {

		
			List<Account> list = new ArrayList<>();
	        CSVReader reader = null;
	        try {
	            reader = new CSVReaderBuilder(new FileReader(csvFile)).withSkipLines(1).build();
	            String[] row;
	            
	            while ((row = reader.readNext()) != null) {
	            	Account account = new Account();
	            	account.setAccount_id(row[0]);
	            	account.setAccount_name(row[1]);
	            	account.setAccount_email(row[1]);
	            	account.setAccount_password(row[2]);
	            	account.setAccount_country_code(row[3]);
	            	list.add(account);
	            }
	        } catch (IOException e) {
	        	log.error("IOException :"+e.getMessage());
	        }
	        return list;
	}
	
	public static final int writeCSVFile(List<Account> list,String csvFile) throws IOException {
		log.info("writeCSVFile");
		log.info("csvFile :"+csvFile);
		Writer writer = Files.newBufferedWriter(Paths.get(csvFile));
		 CSVWriter csvWriter = new CSVWriter(writer,
                 CSVWriter.DEFAULT_SEPARATOR,
                 CSVWriter.NO_QUOTE_CHARACTER,
                 CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                 CSVWriter.DEFAULT_LINE_END);
		 String[] HEADERS = { "PBS", "Login Email","PWD","Country Code","Operation","Result Code","Result"};
		 csvWriter.writeNext(HEADERS); 
		 for(Account account : list) {
			 csvWriter.writeNext(new String[] {account.getAccount_id(),account.getAccount_email(),account.getAccount_password(),account.getAccount_country_code(),account.getOperation(),account.getResult_code(),account.getResult()}); 
		 }
		 csvWriter.close();
		 
	
		return 0;
	}
	
	

	

}
