package org.cloudbus.cloudsim.examples.cache;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		options.addOption( "H", "threshold", true, "overload threshold" +
														" default 1");
		options.addOption( "a", true, "coefficient a for random model" +
														" default 200");
		options.addOption( "b", true, "coefficient b1 for random model" +
														" default 50" );
		options.addOption( "v", true, "coefficient b2 for random model" +
														" default 50" );
		options.addOption( "c", true, "coefficient c for random model" +
														"default 200");
		options.addOption( "l", "limit", true, "length for simulation" +
														" default 301 (sec) for initial placement experiment" +
														" set it to 3600 (sec) for 1 hour simulation");	
		options.addOption( "M", "map", false, "output VM to PM mapping" );
		options.addOption( "h", "help", false, "print usage" );
		
		options.addOption( "f", "cpu-mhz", true, "cpu frequency of each core default 3160" );
		options.addOption( "u", "cpu-cores", true, "cpu number of cores default 4" );
		options.addOption( "r", "mem-size", true, "total memory size (Mb) default 4096" );

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
		    if( line.hasOption( "H" ) ) {
		    	ExpConstants.THRESHOLD = line.getOptionValue("H");
		    }
		    if( line.hasOption( "a" ) ) {
		    	ExpConstants.PROFILE_MODEL_A = Integer.valueOf(line.getOptionValue("a"));
		    }
		    if( line.hasOption( "b" ) ) {
		    	ExpConstants.PROFILE_MODEL_B_1 = Integer.valueOf(line.getOptionValue("b"));
		    }
		    if( line.hasOption( "v" ) ) {
		    	ExpConstants.PROFILE_MODEL_B_2 = Integer.valueOf(line.getOptionValue("v"));
		    }
		    if( line.hasOption( "c" ) ) {
		    	ExpConstants.PROFILE_MODEL_C = Integer.valueOf(line.getOptionValue("c"));
		    }
		    if( line.hasOption( "l" ) ) {
		    	ExpConstants.SIMULATION_LIMIT = Double.valueOf(line.getOptionValue("l"));
		    }
		    if( line.hasOption( "M" ) ) {
		    	ExpConstants.OUTPUT_MAP = true;
		    }
		    if( line.hasOption( "f" ) ) {
		    	ExpConstants.HOST_MIPS[0] = Integer.valueOf(line.getOptionValue("f"));
		    }
		    if( line.hasOption( "u" ) ) {
		    	ExpConstants.HOST_PES[0] = Integer.valueOf(line.getOptionValue("u"));
		    }
		    if( line.hasOption( "r" ) ) {
		    	ExpConstants.HOST_RAM[0] = Integer.valueOf(line.getOptionValue("r"));
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
