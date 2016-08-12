package uk.ac.soton.ldanalytics.piotre.server;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class CommandLineOptions {
	@Parameter
	private List<String> parameters = new ArrayList<>();
 
	@Parameter(names = { "-D", "-database" }, description = "DB path")
	public String db;
	
	@Parameter(names = { "-U", "-username" }, description = "DB Username")
	public String username;
 
	@Parameter(names = { "-P", "-password" }, description = "DB Password")
	public String password;
}
