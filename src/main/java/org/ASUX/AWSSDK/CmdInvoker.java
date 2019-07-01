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

import org.ASUX.yaml.JSONTools;
import org.ASUX.YAML.NodeImpl.NodeTools;

import org.ASUX.yaml.YAML_Libraries;
import org.ASUX.yaml.MemoryAndContext;
import org.ASUX.YAML.NodeImpl.NodeTools;
import org.ASUX.yaml.CmdLineArgs;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// https://yaml.org/spec/1.2/spec.html#id2762107
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.error.Mark; // https://bitbucket.org/asomov/snakeyaml/src/default/src/main/java/org/yaml/snakeyaml/error/Mark.java
import org.yaml.snakeyaml.DumperOptions; // https://bitbucket.org/asomov/snakeyaml/src/default/src/main/java/org/yaml/snakeyaml/DumperOptions.java

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p> This org.ASUX.yaml GitHub.com project and the <a href="https://github.com/org-asux/org.ASUX.cmdline">org.ASUX.cmdline</a> GitHub.com projects.</p>
 * <p>This class and entire library is tied to the SnakeYAML implementation (org.ASUX.YAML.NodeImpl).  This is because only this YAML-Implementation library can handle !Ref and !Condition and other CFN tags.</p>
 * <p> This is a class independent from most of  org.ASUX.yaml and org.ASUX.YAML.NodeImpl projects</p>
 * <p> This class is focuesed on providing a SnakeYAML's Node-based interface to specific AWS SDK APIs</p>
 * <p> The 3 API currently supported are: <b>list-regions, list-AZs, describe-AZs</b>. </p>
 * <p> See full details of how to use these commands - in this GitHub project's wiki at:<br>
 *  <a href="https://github.com/org-asux/org-ASUX.github.io/wiki">org.ASUX Wiki </a> on GitHub</p>
 */

public class CmdInvoker extends org.ASUX.yaml.CmdInvoker {

    private static final long serialVersionUID = 602L;

    public static final String CLASSNAME = CmdInvoker.class.getName();

    private CmdLineArgsAWS cmdlineargsaws;
    private DumperOptions dumperopt;

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================
    /**
     *  The constructor exclusively for use by  main() classes anywhere.
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _showStats Whether you want a final summary onto console / System.out
     *  @param _dopt a non-null reference to org.yaml.snakeyaml.DumperOptions instance.  CmdInvoker can provide this reference.
     */
    public CmdInvoker( final boolean _verbose, final boolean _showStats, final DumperOptions _dopt ) {
        super( _verbose, _showStats );
        this.dumperopt = _dopt;
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    // public void setCmdLineArgsAWS( final CmdLineArgsAWS _claa ) {
    //     this.cmdlineargsaws = _claa;
    // }

    public DumperOptions getDumperOptions() {
        return this.dumperopt;
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * It makes NO SENSE to implement this method, but the parent class requires I add some code here.  Need a better design.
     *  @return null
     */
    @Override
    public YAML_Libraries getYamlLibrary() {
        return null;
    }

    /**
     * It makes NO SENSE to implement this method, but the parent class requires I add some code here.  Need a better design.
     * @param _l the YAML-library to use going forward. See {@link YAML_Libraries} for legal values to this parameter
     */
    @Override
    public void setYamlLibrary( final YAML_Libraries _l ) {
        // do nothing
    }

    /**
     * It makes NO SENSE to implement this method, but the parent class requires I add some code here.  Need a better design.
     *  @return null
     */
    @Override
    public Class<?> getLibraryOptionsClass() {
        return null;
    }

    /**
     * It makes NO SENSE to implement this method, but the parent class requires I add some code here.  Need a better design.
     * @return null
     */
    @Override
    public Object getLibraryOptionsObject() {
        return null;
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  This function is meant to be used by Cmd.main() and by BatchProcessor.java.  Read the code *FIRST*, to see if you can use this function too.
     *  @param _cmdLA will be Null for this class, as passed by {@link Cmd} or {@link org.ASUX.yaml.BatchCmdProcessor}.
     *  @param _ignore will be Null for this class, as passed by {@link Cmd} or {@link org.ASUX.yaml.BatchCmdProcessor}. Otherwise, this is the YAML inputData that is the input to pretty much all commands (a org.yaml.snakeyaml.nodes.Node object).
     *  @return either a String or org.yaml.snakeyaml.nodes.Node
     *  @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     *  @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     *  @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public Object processCommand ( org.ASUX.yaml.CmdLineArgsCommon _cmdLA, final Object _ignore )
                throws FileNotFoundException, IOException, Exception
    {
        final String HDR = CLASSNAME + ": processCommand(): ";
        // assertTrue( _inputData instanceof String );

        // aws.sdk ----list-regions
        // aws.sdk ----list-AZs         us-east-2
        // aws.sdk ----describe-AZs     us-east-2
        // in 'cmdLineArgsStrArr' below, we DROP the 'aws.sdk' leaving the rest of the parameters

        assertTrue( _cmdLA instanceof CmdLineArgsAWS );
        this.cmdlineargsaws = (CmdLineArgsAWS) _cmdLA;
        final AWSSDK awssdk = AWSSDK.AWSCmdline( this.verbose );

        //=======================================
        if ( this.dumperopt == null ) { // this won't be null, if this object was created within BatchCmdProcessor.java
            this.dumperopt = org.ASUX.YAML.NodeImpl.GenericYAMLWriter.defaultConfigurationForSnakeYamlWriter();
        }
        switch( this.cmdlineargsaws.quoteType ) {
            case DOUBLE_QUOTED: dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.DOUBLE_QUOTED );  break;
            case SINGLE_QUOTED: dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.SINGLE_QUOTED );  break;
            case LITERAL:       dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.LITERAL );        break;
            case FOLDED:        dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.FOLDED );         break;
            case PLAIN:         dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.PLAIN );          break;
            default:            dumperopt.setDefaultScalarStyle( org.yaml.snakeyaml.DumperOptions.ScalarStyle.FOLDED );         break;
        }

        //=======================================
        final ArrayList<String> cmdLineArgsStrArr = this.cmdlineargsaws.getArgs();
        if (this.verbose) System.out.println( HDR +" cmdLineArgsStrArr has "+ cmdLineArgsStrArr.size() +" containing =["+ cmdLineArgsStrArr +"]" );
        assertTrue( cmdLineArgsStrArr.size() >= 1 );
        // the CmdLineArgsAWS class should take care of validating the # of cmd-line arguments and the type

        // skip the index[0] (in cmdLineArgsStrArr)  which must be 'AWS.SDK' (for this class to be invoked)
        final String awscmdStr = cmdLineArgsStrArr.get(0);
        if (this.verbose) System.out.println( HDR +" awscmdStr =["+ awscmdStr +"]" );

        //-------------------------------------------
        switch( this.cmdlineargsaws.cmdType ) {
        case listRegions:       // ( awscmdStr.equals("--list-regions"))
            final ArrayList<String> regionsList = awssdk.getRegions( );
            final SequenceNode seqNode = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, regionsList, this.getDumperOptions() );
            return seqNode;
        //-------------------------------------------
        case listAZs:           // ( awscmdStr.equals("--list-AZs"))
            if ( cmdLineArgsStrArr.size() < 2 )
                throw new Exception( "AWS.SDK --list-AZs command: INSUFFICIENT # of parameters ["+ cmdLineArgsStrArr +"]" );
            final ArrayList<String> AZList = awssdk.getAZs( cmdLineArgsStrArr.get(1) ); // ATTENTION: Pay attention to index# of cmdLineArgsStrArr
            final SequenceNode seqNode2 = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, AZList, this.getDumperOptions() );
            return seqNode2;
        //-------------------------------------------
        case describeAZs:       // ( awscmdStr.equals("--describe-AZs"))
            if ( cmdLineArgsStrArr.size() < 2 )
                throw new Exception( "AWS.SDK --describe-AZs command: INSUFFICIENT # of parameters ["+ cmdLineArgsStrArr +"]" );
            final ArrayList< LinkedHashMap<String,Object> > AZDetailsList = awssdk.describeAZs( cmdLineArgsStrArr.get(1) ); // ATTENTION: Pay attention to index# of cmdLineArgsStrArr
            final SequenceNode seqNode3 = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, AZDetailsList, this.getDumperOptions() );
            return seqNode3;
        //-------------------------------------------
        case createKeyPair:     // ( awscmdStr.equals("--create-keypair"))
            if ( cmdLineArgsStrArr.size() < 3 ) // aws.sdk --create-keypair AWSRegion MySSHKeyName
                throw new Exception( "AWS.SDK --create-key-pair command: INSUFFICIENT # of parameters ["+ cmdLineArgsStrArr +"]" );
            final String AWSRegion = cmdLineArgsStrArr.get(1);
            final String mySSHKeyName = cmdLineArgsStrArr.get(2);
            final String keyMaterial = awssdk.createKeyPairEC2( AWSRegion, mySSHKeyName ); // ATTENTION: Pay attention to index# of cmdLineArgsStrArr
            //------------------
            final String homedir = System.getProperty("user.home");
            assertTrue( homedir != null );
            final File awsuserhome = new File( homedir +"/.aws" );
            awsuserhome.mkdirs();
            final String mySSHKeyFilePathStr = homedir +"/.aws/"+ mySSHKeyName;
            // final File mySSHKeyFile = new File( mySSHKeyFilePathStr );
            try {
                java.nio.file.Files.write(   java.nio.file.Paths.get( mySSHKeyFilePathStr ),   keyMaterial.getBytes()  );
                System.out.println( "File "+ mySSHKeyFilePathStr +" created." );
            // } catch(IOException ioe) {
            // } catch(IllegalArgumentException ioe) { // thrown by java.nio.file.Paths.get()
            // } catch(FileSystemNotFoundException ioe) { // thrown by java.nio.file.Paths.get()
            } catch(java.nio.file.InvalidPathException ipe) {
                ipe.printStackTrace( System.err );
                System.err.println( "\n\n"+ HDR +"!!SERIOUS INTERNAL ERROR!! Why would the Path '"+ mySSHKeyFilePathStr +"' be invalid?\n\n" );
                throw ipe;
            }
            final ScalarNode scalar = new ScalarNode( Tag.STR, keyMaterial, null, null, this.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
            return scalar;
        //-------------------------------------------
        case deleteKeyPair:     // ( awscmdStr.equals("--delete-keypair"))
            if ( cmdLineArgsStrArr.size() < 3 ) // aws.sdk --create-keypair AWSRegion MySSHKeyName
                throw new Exception( "AWS.SDK --delete-key-pair command: INSUFFICIENT # of parameters ["+ cmdLineArgsStrArr +"]" );
            final String AWSRegion2 = cmdLineArgsStrArr.get(1);
            final String mySSHKeyName2 = cmdLineArgsStrArr.get(2);
            awssdk.deleteKeyPairEC2( AWSRegion2, mySSHKeyName2 ); // ATTENTION: Pay attention to index# of cmdLineArgsStrArr
            final Node n = NodeTools.getEmptyYAML( this.getDumperOptions() );
            return n;
        //-------------------------------------------
        case listKeyPairs:     // ( awscmdStr.equals("--delete-keypair"))
            if ( cmdLineArgsStrArr.size() < 3 ) // aws.sdk --create-keypair AWSRegion MySSHKeyName
                throw new Exception( "AWS.SDK --describe-key-pairs command: INSUFFICIENT # of parameters ["+ cmdLineArgsStrArr +"]" );
            final String AWSRegion3 = cmdLineArgsStrArr.get(1);
            final String mySSHKeyName3 = cmdLineArgsStrArr.get(2);
            final List<com.amazonaws.services.ec2.model.KeyPairInfo> keys = awssdk.listKeyPairEC2( AWSRegion3, mySSHKeyName3 ); // ATTENTION: Pay attention to index# of cmdLineArgsStrArr
            final java.util.LinkedList<Node> seqs = new java.util.LinkedList<Node>();
            final SequenceNode seqNode4 = new SequenceNode( Tag.SEQ, false,     seqs,         null, null, this.getDumperOptions().getDefaultFlowStyle() );
            for ( com.amazonaws.services.ec2.model.KeyPairInfo x: keys ) {
                if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyName=\n"+ x.getKeyName() );
                if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyFingerprint=\n"+ x.getKeyFingerprint() );
                // if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyMaterial=\n"+ x.getKeyMaterial() ); <<-- NOT AVAILABLE, AFTER KEY has been created
                // final ScalarNode k = new ScalarNode( Tag.STR, x.getKeyName(), null, null, this.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
                // final ScalarNode v = new ScalarNode( Tag.STR, x.getKeyFingerprint(), null, null, this.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
                // final java.util.List<NodeTuple> tuples = topNode.getValue();
                // tuples.add( new NodeTuple( k, v ) );
                final MappingNode mapNode = (MappingNode) NodeTools.getNewSingleMap( x.getKeyName(), x.getKeyFingerprint(), this.getDumperOptions() );
                seqs.add ( mapNode );
            }
            return seqNode4;
        //-------------------------------------------
        case Undefined:         // 
        default:
            throw new Exception( "AWS.SDK INTERNAL-ERROR: Unknown command ["+ cmdLineArgsStrArr +"]" );
        }

        //----------------------------------------
        // return null;

    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================
    /**
     * <p>This project (interface with AWS SDK) does Not need this functionality.</p>
     * <p>For other subclasses of {@link org.ASUX.yaml.CmdInvoker}, this method a simpler facade/interface to org.ASUX.yaml.InputsOutputs.getDataFromReference(), for use by org.ASUX.yaml.BatchCmdProcessor.</p>
     * @param _src a javalang.String value - either inline YAML/JSON, or a filename (must be prefixed with '@'), or a reference to a property within a Batch-file execution (must be prefixed with a '!')
     * @return an object (either any of Node, SequenceNode, MapNode, ScalarNode ..)
     * @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     * @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     * @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public Object getDataFromReference( final String _src )
                                throws FileNotFoundException, IOException, Exception
    {   //return InputsOutputs.getDataFromReference( _src, this.memoryAndContext, this.getYamlScanner(), this.getDumperOptions(), this.verbose );
        final String HDR = CLASSNAME + ": getDataFromReference("+ _src +"): ";
        throw new Exception( "UNIMPLEMENTED METHOD: "+ HDR );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * <p>This project (interface with AWS SDK) does Not need this functionality.</p>
     * <p>For other subclasses of {@link org.ASUX.yaml.CmdInvoker}, this method a simpler facade/interface to org.ASUX.yaml.InputsOutputs.getDataFromReference(), for use by org.ASUX.yaml.BatchCmdProcessor</p>
     * @param _dest a javalang.String value - either a filename (must be prefixed with '@'), or a reference to a (new) property-variable within a Batch-file execution (must be prefixed with a '!')
     * @param _input the object to be saved using the reference provided in _dest paramater
     * @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     * @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     * @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public void saveDataIntoReference( final String _dest, final Object _input )
                            throws FileNotFoundException, IOException, Exception
    {
        // InputsOutputs.saveDataIntoReference( _dest, _input, this.memoryAndContext, this.getYamlWriter(), this.getDumperOptions(), this.verbose );
        final String HDR = CLASSNAME + ": saveDataIntoReference("+ _dest +","+ _input.getClass().getName() +"): ";
        throw new Exception( "UNIMPLEMENTED METHOD: "+ HDR );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

}
