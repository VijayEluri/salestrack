package com.tort.replicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SchemaUpdater {	
	public static void main(String[] args) throws IOException {
		new SchemaUpdater().updateSchema(args[0]);
	}

	public void updateSchema(String path) throws IOException {		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path + ".tmp"))));
		
		String line;
		while(true){
			line = reader.readLine();
				if(line == null)
					break;
			if(line.length() > 1000)
				System.out.println(line.length());
			line = line.replaceAll("SEQ_M", "ID");
			line = line.replaceAll("DEP_SEQ", "ID");
			line = line.replaceAll("DEP_NAME", "NAME");
			line = line.replaceAll("TRD_SEQ", "ID");
			line = line.replaceAll("TRD_FROM", "FROM");
			line = line.replaceAll("TRD_TO", "TO");
			line = line.replaceAll("TRD_PRICE", "PRICE");
			line = line.replaceAll("TRD_QUANT", "QUANT");
			
			line = line.replaceAll("TRADE_SRC", "TRANSITION");
			line = line.replaceAll("MAT", "GOOD");
			line = line.replaceAll("DEP", "SALES");
			writer.write(line);
			writer.newLine();
		}
		
		reader.close();
		writer.close();
		
		new File(path + ".tmp").renameTo(new File(path));
	}
}
