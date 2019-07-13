/*
 BSD 3-Clause License
 
 Copyright (c) 2019, Udaybhaskar Sarma Seetamraju
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 * Neither the name of the copyright holder nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.ASUX.AWSSDK;

import java.util.ArrayList;

import org.apache.commons.cli.*;

import static org.junit.Assert.*;

/** <p>This class is a typical use of the org.apache.commons.cli package.</p>
 *  <p>This class has No other function - other than to parse the commandline arguments and handle user's input errors.</p>
 *  <p>For making it easy to have simple code generate debugging-output, added a toString() method to this class.</p>
 *  <p>Typical use of this class is: </p>
 *<pre>
 public static void main(String[] args) {
 cmdLineArgs = new CmdLineArgsAWS(args);
 .. ..
 *</pre>
 *
 *  <p>See full details of how to use this, in {@link org.ASUX.yaml.Cmd} as well as the <a href="https://github.com/org-asux/org.ASUX.cmdline">org.ASUX.cmdline</a> GitHub.com project.</p>
 * @see org.ASUX.yaml.Cmd
 */
public class CmdLineArgsAWS extends org.ASUX.yaml.CmdLineArgsCommon {

    private static final long serialVersionUID = 412L;

    public static final String CLASSNAME = CmdLineArgsAWS.class.getName();

    public static final String[] LISTREGIONS = { "lr", "list-regions", "list all the AWS regions" };
    public static final String[] LISTAZS = { "lz", "list-AZs", "List all the AZs in a specified region" };
    public static final String[] DESCRIBEAZS = { "dz", "describe-AZs", "describe all the AZs in a specified region" };
    public static final String[] CREATEKEYPAIR = { "ck", "create-key-pair", "create a new keypair within a specified region, with the provided-name of the keypair" };
    public static final String[] DELETEKEYPAIR = { "dk", "delete-key-pair", "delete an existing keypair in a specified region and the name of the keypair" };
    public static final String[] LISTKEYPAIR = { "lk", "describe-key-pairs", "show all keypair within a specified region, matching the provided-name of the keypair" };

    //------------------------------------
    public Enums.SDKCommands cmdType = Enums.SDKCommands.Undefined;

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  <p>Add cmd-line argument definitions (using apache.commons.cli.Options) for the instance-variables defined in this class.</p>
     *  @param options a Non-Null instance of org.apache.commons.cli.Options
     */
    @Override
    protected void defineAdditionalOptions()
    {
        OptionGroup grp = new OptionGroup();
        Option listRegionsCmdOpt = new Option( LISTREGIONS[0], LISTREGIONS[1], false, LISTREGIONS[2] );
        Option listAZsCmdOpt = new Option( LISTAZS[0], LISTAZS[1], false, LISTAZS[2] );
            listAZsCmdOpt.setArgs(1);
            listAZsCmdOpt.setOptionalArg(false);
            listAZsCmdOpt.setArgName("region");
        Option descAZsCmdOpt = new Option( DESCRIBEAZS[0], DESCRIBEAZS[1], false, DESCRIBEAZS[2] );
            descAZsCmdOpt.setArgs(1);
            descAZsCmdOpt.setOptionalArg(false);
            descAZsCmdOpt.setArgName("region");
        Option createKeyPairCmdOpt = new Option( CREATEKEYPAIR[0], CREATEKEYPAIR[1], false, CREATEKEYPAIR[2] );
            createKeyPairCmdOpt.setArgs(2);
            createKeyPairCmdOpt.setOptionalArg(false);
            createKeyPairCmdOpt.setArgName("region> <ExistingSSHKeyPair-Name");
        Option delKeyPairCmdOpt = new Option( DELETEKEYPAIR[0], DELETEKEYPAIR[1], false, DELETEKEYPAIR[2] );
            delKeyPairCmdOpt.setArgs(2);
            delKeyPairCmdOpt.setOptionalArg(false);
            delKeyPairCmdOpt.setArgName("region> <New-SSHKeyPair-Name");
        Option listKeyPairCmdOpt = new Option( LISTKEYPAIR[0], LISTKEYPAIR[1], false, LISTKEYPAIR[2] );
            listKeyPairCmdOpt.setArgs(2);
            listKeyPairCmdOpt.setOptionalArg(false);
            listKeyPairCmdOpt.setArgName("region> <New-SSHKeyPair-Name");

        grp.addOption(listRegionsCmdOpt);
        grp.addOption(listAZsCmdOpt);
        grp.addOption(descAZsCmdOpt);
        grp.addOption(createKeyPairCmdOpt);
        grp.addOption(delKeyPairCmdOpt);
        grp.addOption(listKeyPairCmdOpt);
        grp.setRequired(true);

        this.options.addOptionGroup(grp);
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  @see org.ASUX.yaml.CmdLineArgsCommon#parseAdditionalOptions
     */
    @Override
    protected void parseAdditionalOptions( String[] _args, final org.apache.commons.cli.CommandLine _apacheCmdProcessor )
                    throws MissingOptionException, ParseException, Exception
    {
        final String HDR = CLASSNAME + ": parseCmdLineForNewOptions([]],..): ";

        if ( _apacheCmdProcessor.hasOption(LISTREGIONS[1]) ) {
            this.cmdType = Enums.SDKCommands.listRegions;
        }
        if ( _apacheCmdProcessor.hasOption(LISTAZS[1]) ) {
            this.cmdType = Enums.SDKCommands.listAZs;
        }
        if ( _apacheCmdProcessor.hasOption(DESCRIBEAZS[1]) ) {
            this.cmdType = Enums.SDKCommands.describeAZs;
        }
        if ( _apacheCmdProcessor.hasOption(CREATEKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.createKeyPair;
        }
        if ( _apacheCmdProcessor.hasOption(DELETEKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.deleteKeyPair;
        }
        if ( _apacheCmdProcessor.hasOption(LISTKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.listKeyPairs;
        }

        assertTrue( this.cmdType != Enums.SDKCommands.Undefined );
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /** For making it easy to have simple code generate debugging-output, added this toString() method to this class.
     */
    @Override
    public String toString() {
        return
        super.toString()
        +" cmdType="+cmdType
        ;
    }

    //------------------------------------
    /**
     * This object reference is either to a CmdLineArgs class (for READ, LIST and DELETE commands), or subclasses of CmdLineArgs (for INSERT, REPLACE, TABLE, MACRO, BATCH commands)
     * @return either an instance of CmdLineArgs or one of it's subclasses (depends on this.cmdType {@link #cmdType})
     */
    public Enums.SDKCommands getSpecificCmd() {
        return this.cmdType;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * This returns an object of this class/type, but as a pointer to the parent class (for use in a generic way, by org.ASUX.yaml.BatchCmdProcessor.java)
     * @param args the command-line args as passed to main()
     * @return a non-null instance of this class, but as a reference of parent/super-class.
     * @throws Exception if any issue parsing or validating the cmdline arguments passed to this method
     */
    public static final org.ASUX.AWSSDK.CmdLineArgsAWS create( final String[] args )
                                        throws Exception
    {
        final String HDR = CLASSNAME + ": create("+ args.length +"): ";
        CmdLineArgsAWS cmdlineargs = null;
        // if ( true ) { System.out.print( HDR +": ");  for( String s: args) System.out.print(s+"\t"); System.out.println(); }

        try {
            cmdlineargs = new CmdLineArgsAWS();
            cmdlineargs.define();
            cmdlineargs.parse( args );
            // Until we get past the above statement, we don't know about 'verbose'
            if (cmdlineargs.verbose) { System.out.print( HDR +" >>>>>>>>>>>>> "); for( String s: cmdlineargs.getArgs()) System.out.print(s);  System.out.println(); }
            if (cmdlineargs.verbose) System.out.println( HDR +" cmdlineargs=["+ cmdlineargs +"]" );

            return cmdlineargs;

        } catch (Exception e) {
            if ( cmdlineargs == null || cmdlineargs.verbose ) e.printStackTrace(System.err);
            if ( cmdlineargs == null || cmdlineargs.verbose ) System.err.println( "Internal ERROR: Cmdline arguments provided are: " + cmdlineargs + "\n"+ e );
            throw(e);
        }
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    // For unit-testing purposes only
//    public static void main(String[] args) {
//        new CmdLineArgsAWS(args);
//    }

}
