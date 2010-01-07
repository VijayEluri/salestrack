package com.tort.replicator;

import java.io.IOException;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.tort.replicator.SchemaUpdater;

@Test(groups = {"integration"})
public class SchemaUpdaterIT {
	public void schemaUpdate() throws IOException{
		assertNotNull(this.getClass().getClassLoader().getResourceAsStream("trade.script"));		
		new SchemaUpdater().updateSchema(this.getClass().getClassLoader().getResource("trade.script").getFile());
	}
}
