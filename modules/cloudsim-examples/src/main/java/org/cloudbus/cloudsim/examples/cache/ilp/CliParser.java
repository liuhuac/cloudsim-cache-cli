package org.cloudbus.cloudsim.examples.cache.ilp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.cloudbus.cloudsim.examples.cache.ExpConstants;

public class CliParser {

	public void parse(String[] args){
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "n", "nvms", true, "number of VMs" );
		options.addOption( "m", "npms", true, "number of PMs" );
		options.addOption( "t", "trace", true, "trace folder, require [-P] being set" );
		options.addOption( "P", "trace-profile", false, "use tracefiles for simulation"
														+ " default use random profiles");
		options.addOption( "M", "map", false, "output VM to PM mapping" );
		options.addOption( "h", "help", false, "print usage" );

		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    if( line.hasOption( "n" ) ) {
		    	ExpConstants.NUMBER_OF_VMS = Integer.valueOf(line.getOptionValue("n"));
		    }
		    if( line.hasOption( "m" ) ) {
		    	ExpConstants.NUMBER_OF_HOSTS = Integer.valueOf(line.getOptionValue("m"));
		    }
		    if( line.hasOption( "t" ) ) {
		    	if(!line.hasOption( "P" )){
		    		System.out.println("flag -P must be set for using -t trace file simulation");
		    		HelpFormatter formatter = new HelpFormatter();
		    		formatter.printHelp( "CloudSimCacheVm", options );
		    		System.exit(0);
		    	}
		    	ExpConstants.TRACE_FOLDER = line.getOptionValue("t");
		    }
		    if( line.hasOption( "P" ) ) {
		    	ExpConstants.PROFILE_TYPE = 1;
		    }

		    if( line.hasOption( "M" ) ) {
		    	ExpConstants.OUTPUT_MAP = true;
		    }

		    if( line.hasOption( "h" ) ) {
		    	HelpFormatter formatter = new HelpFormatter();
	    		formatter.printHelp( "CloudSimCacheVm", options );
	    		System.exit(0);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		    HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp( "CloudSimCacheVm", options );
    		System.exit(0);
		}
	}
	
}
