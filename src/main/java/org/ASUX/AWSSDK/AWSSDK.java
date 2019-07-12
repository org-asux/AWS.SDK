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

import org.ASUX.common.Macros;

import org.ASUX.yaml.JSONTools;
import org.ASUX.yaml.YAML_Libraries;

import org.ASUX.YAML.NodeImpl.ReadYamlEntry;
import org.ASUX.YAML.NodeImpl.BatchCmdProcessor;
import org.ASUX.YAML.NodeImpl.NodeTools;
import org.ASUX.YAML.NodeImpl.GenericYAMLScanner;
import org.ASUX.YAML.NodeImpl.GenericYAMLWriter;
import org.ASUX.YAML.NodeImpl.InputsOutputs;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Properties;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;


// https://yaml.org/spec/1.2/spec.html#id2762107
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.Mark; // https://bitbucket.org/asomov/snakeyaml/src/default/src/main/java/org/yaml/snakeyaml/error/Mark.java
import org.yaml.snakeyaml.DumperOptions; // https://bitbucket.org/asomov/snakeyaml/src/default/src/main/java/org/yaml/snakeyaml/DumperOptions.java


// https://github.com/eugenp/tutorials/tree/master/aws/src/main/java/com/baeldung
// https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_Region.html
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/package-summary.html
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.AmazonEC2Client;
// import com.amazonaws.services.ec2.model.Region;
// import com.amazonaws.services.ec2.model.AvailabilityZone;
    // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/AvailabilityZone.html
// import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
// import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.DeleteKeyPairRequest;
import com.amazonaws.services.ec2.model.DeleteKeyPairResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.KeyPairInfo;
// import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
// import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
// import com.amazonaws.services.ec2.model.DescribeInstancesResult;
// import com.amazonaws.services.ec2.model.IpPermission;
// import com.amazonaws.services.ec2.model.IpRange;
// import com.amazonaws.services.ec2.model.MonitorInstancesRequest;
// import com.amazonaws.services.ec2.model.RebootInstancesRequest;
// import com.amazonaws.services.ec2.model.RunInstancesRequest;
// import com.amazonaws.services.ec2.model.StartInstancesRequest;
// import com.amazonaws.services.ec2.model.StopInstancesRequest;
// import com.amazonaws.services.ec2.model.UnmonitorInstancesRequest;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html#listResourceRecordSets-com.amazonaws.services.route53.model.ListResourceRecordSetsRequest-
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder;
import com.amazonaws.services.route53.model.ListHostedZonesByNameRequest;
import com.amazonaws.services.route53.model.ListHostedZonesByNameResult;
import com.amazonaws.services.route53.model.HostedZone;
// import com.amazonaws.services.route53.model.ListResourceRecordSetsResult;
// import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;

import static org.junit.Assert.*;

/**
 *  
 */
public class AWSSDK {

    public static final String CLASSNAME = "org.ASUX.yaml.AWSSDK";

    /** <p>Whether you want deluge of debug-output onto System.out.</p><p>Set this via the constructor.</p>
     *  <p>It's read-only (final data-attribute).</p>
     */
    public final boolean verbose;

    /**
     *  <p>Whether this entire class and all it's methods will use "cached" output (a.k.a. files under {ASUXCFNHOME}/configu/inputs folder), instead of invoking AWS SDK calls.</p>
     *  <p>In addition to ensuring this class works for those without an AWS account, it is quite helpful from a speed perspective!</p>
     */
    public final boolean offline;

    /**
     *  This constructor allows us to centralize the authentication.  But then again.. what if Classses have to pass this object around?  For that, use the Static Factory function connect()
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _AWSAccessKeyId your AWS credential with API-level access as appropriate
     *  @param _AWSSecretAccessKey your AWS credential with API-level access as appropriate
     */
    public AWSSDK( final boolean _verbose, final String _AWSAccessKeyId, final String _AWSSecretAccessKey) {
        this.verbose = _verbose;
        this.offline = false;
        this.AWSAuthenticate( _AWSAccessKeyId, _AWSSecretAccessKey );
    }

    /**
     *  <p>If you are <b>SURE</b> that you'd like to use an object of this class and all it's methods in an <b>OFFLINE</b> (a.k.a. no internet available), or .. to use ONLY "cached" output (a.k.a. files under {ASUXCFNHOME}/configu/inputs folder) for faster responses, or .. to _ENSURE_ No AWS SDK calls are invoked (because you do not have/do want to use an AWS API Key).</p>
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     */
    public AWSSDK( final boolean _verbose ) {
        this.verbose = _verbose;
        this.offline = true;
    }

    //------------------------------------------------------------------------------
    private static class MyAWSException extends Exception {
        private static final long serialVersionUID = 99L;
        public MyAWSException(String _s) { super(_s); }
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    private boolean bTried2Authenticate = false;
    private AWSCredentials aws_credentials = null;
    private AWSStaticCredentialsProvider AWSAuthenticationHndl = null;

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private static AWSSDK singleton = null;

    /**
     *  This constructor allows us to centralize the authentication.  But then again.. what if Classses have to pass this object around?  For that, use the Static Factory function getConnection()
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _AWSAccessKeyId your AWS credential with API-level access as appropriate
     *  @param _AWSSecretAccessKey your AWS credential with API-level access as appropriate
     *  @return the singleton instance of this AWSSDK class
     *  @throws MyAWSException if code has Not yet logged in with AWS credentials
     */
    public static AWSSDK getConnection(boolean _verbose, final String _AWSAccessKeyId, final String _AWSSecretAccessKey) throws MyAWSException
    {
        if ( singleton != null )
            throw new MyAWSException( CLASSNAME +": static factory getConnection(): Someone already invoked this function, with 3 potentially-DIFFERENT parameters already!" );

        singleton = new AWSSDK(_verbose, _AWSAccessKeyId, _AWSSecretAccessKey);
        return singleton;
    }

    /**
     *  This constructor allows us to centralize the authentication.  But then again.. what if Classses have to pass this object around?  For that, use the Static Factory function getConnection()
     *  @return (could be null, if this class not properly invoked) the singleton instance of this AWSSDK class
     */
    public static AWSSDK getConnectionNoThrow() 
    {
        return singleton;
    }

    /**
     *  This constructor allows us to centralize the authentication.  But then again.. what if Classses have to pass this object around?  For that, use the Static Factory function getConnection()
     *  @return the singleton instance of this AWSSDK class
     *  @throws MyAWSException if code has Not yet logged in with AWS credentials
     */
    public static AWSSDK getConnection() throws MyAWSException
    {
        if ( singleton == null )
            throw new MyAWSException( CLASSNAME +": static factory getConnection(No-ARG): FORGOT to call the 3-arg POLYMORPHIC variant of getConnection()????" );

        return singleton;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * If already logged in, get me a handle - to pass to my own AWS SDK invocations.
     * If not logged in already, an Exception will be thrown
     * @return the handle to a previously successful AWS login-connection
     * @throws MyAWSException if code has Not yet logged in with AWS credentials
     */
    private AWSStaticCredentialsProvider getAWSAuthenticationHndl() throws MyAWSException {
        if ( this.offline )
            throw new MyAWSException( CLASSNAME +": getAWSAuthenticationHndl(no-arg): INTERNAL SERIOUS ERROR: !!!! Logic-mistake in code !!!! UN-expectedly invoked the AWSAuthenticate(AccessKey,SecretKey) function." );
        else if ( this.AWSAuthenticationHndl == null )
            throw new MyAWSException( CLASSNAME +": getAWSAuthenticationHndl(no-arg): code hasn't !!!!SUCCESSFULLY!!!! invoked the AWSAuthenticate(AccessKey,SecretKey) function." );
        else 
            return this.AWSAuthenticationHndl;
    }

    /**
     *  Login into AWS using the credentials provided.  This invocation is a pre-requisite before invoking any other non-static method in this class.
     *  @param _AWSAccessKeyId your AWS credential with API-level access as appropriate
     *  @param _AWSSecretAccessKey your AWS credential with API-level access as appropriate
     */
    private void AWSAuthenticate( final String _AWSAccessKeyId, final String _AWSSecretAccessKey ) {
        assertTrue ( this.offline == false );
            // throw new MyAWSException( CLASSNAME +": AWSAuthenticate(..,..): INTERNAL SERIOUS ERROR: !!!! Logic-mistake in code !!!! UN-expectedly invoked the this method." );
        this.bTried2Authenticate = true;
        // Authenticate into AWS
        // https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html
        this.aws_credentials = new BasicAWSCredentials( _AWSAccessKeyId, _AWSSecretAccessKey );
        this.AWSAuthenticationHndl = new AWSStaticCredentialsProvider( this.aws_credentials );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    private AmazonEC2 getAWSEC2Hndl( final String _regionStr ) {
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-ec2-regions-zones.html
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        // To use the default credential/region provider chain 
        // Ec2Client ec2 = Ec2Client.create(); // AWS_REGION is checked .. ~/.aws/config default profile .. aws.profile system property
        return ec2;
    }

    private AmazonRoute53 getAWSRoute53Hndl( final String _regionStr ) {
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html 
        // final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.defaultClient();
        final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        // final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.standard().build();
        // To use the default credential/region provider chain 
        // AmazonRoute53Client Rt53 = AmazonRoute53Client.create(); // AWS_REGION is checked .. ~/.aws/config default profile .. aws.profile system property
        return Rt53;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    public static final String ORGASUXHOME      = System.getProperty("ORGASUXHOME");
    public static final String AWSHOME          = System.getProperty("AWSHOME");
    public static final String AWSCFNHOME       = System.getProperty("AWSCFNHOME");

    //==============================================================================
    /**
     *  Given a file-path to a YAML file, load the contents into a SnakeYaml implementation Node.class
     *  @param _absoluteFilePath a NotNull file-path that is === new File(...).getAbsolutePath() (Note: __NO__ guarantees if it is a RELATIVE-path)
     *  @return a NotNull node
     *  @throws Exception thrown by the org.ASUX.YAML.NodeImpl.GenericYAMLScanner class that is used to implement this method.
     */
    public Node readYamlFile( final String _absoluteFilePath ) throws Exception
    {   final String HDR = CLASSNAME + ": readYamlFile(): ";
        
        final InputStream is1 = new FileInputStream( _absoluteFilePath );
        final InputStreamReader filereader = new InputStreamReader(is1);
        final GenericYAMLScanner yamlscanner = new GenericYAMLScanner( this.verbose );
        yamlscanner.setYamlLibrary( YAML_Libraries.NodeImpl_Library );
        final Node node = yamlscanner.load( filereader );
        if ( this.verbose ) System.out.println( HDR +" file contents= '" + NodeTools.Node2YAMLString( node ) + "");
        return node;
    }

    //==============================================================================
    /**
     *  This is a specialization of org.ASUX.YAML.NodeImpl.NodeTools.Node2Map(), in that the output is ArrayList&lt; String &gt;, which is needed by {@link #getAZs_Offline(String)} and {@link #getRegions()}
     *  @param node an NotNull instance of SnakeYaml implementation Node
     *  @return a NotNull instance
     */
    public ArrayList<String> convNode2ArrayList( final Node node ) {
        assertTrue( node instanceof SequenceNode );
        final SequenceNode seqN = (SequenceNode) node;
        final java.util.List<Node> seqs = seqN.getValue();
        final ArrayList<String> retarr = new ArrayList<String>();
        for( Node subN : seqs ) {
            assertTrue( subN instanceof ScalarNode );
            // @SuppressWarnings("unchecked")
            final ScalarNode scalarKey = (ScalarNode) subN;
            final String regionName = scalarKey.getValue();
            if ( this.verbose ) System.out.print( "\t" + regionName ); // No EOLN !
            retarr.add( regionName );
        }
        if ( this.verbose ) System.out.println();
        return retarr;
    }

    /**
     *  This is a specialization of org.ASUX.YAML.NodeImpl.NodeTools.Node2Map(), in that the output is ArrayList&lt; LinkedHashMap &lt;String,Object&gt; &gt;, which is needed by {@link #describeAZs_Offline(String)}
     *  @param node an NotNull instance of SnakeYaml implementation Node
     *  @return a NotNull instance
     *  @throws Exception thrown by NodeTools.Node2Map() in case of unimplemented support for exotic YAML-content
     */
    public ArrayList< LinkedHashMap<String,Object> > convNode2ArrayOfMaps( final Node node ) throws Exception {
        assertTrue( node instanceof SequenceNode );
        final SequenceNode seqN = (SequenceNode) node;
        final java.util.List<Node> seqs = seqN.getValue();
        final ArrayList< LinkedHashMap<String,Object> > retarr = new ArrayList< LinkedHashMap<String,Object> >();
        for( Node subN : seqs ) {
            assertTrue( subN instanceof MappingNode );
            final org.ASUX.common.Output.Object<?> ooo = NodeTools.Node2Map( this.verbose, node );
            assertTrue( ooo.getType() != org.ASUX.common.Output.OutputType.Type_LinkedHashMap );
            if ( this.verbose ) System.out.println( ooo.getMap() );
            retarr.add( ooo.getMap() );
        }
        return retarr;
    }

    //==============================================================================
    /**
     *  An offline implementation (substituting for {@link #getRegions()}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in {AWSCFNHOME +"/config/inputs/"} folder.
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList<String> getRegions_Offline() throws Exception {
        final String YAMLFile = AWSCFNHOME +"/config/inputs/AWSRegions.yaml";
        return convNode2ArrayList( readYamlFile( YAMLFile ) );
    }

    //==============================================================================
    /**
     *  An offline implementation (substituting for {@link #getAZs(String)}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in {AWSCFNHOME +"/config/inputs/"} folder.
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList<String> getAZs_Offline( final String _regionStr ) throws Exception {
        final String YAMLFile = AWSCFNHOME +"/config/inputs/AWS.AZlist-"+ _regionStr +".yaml";
        return convNode2ArrayList( readYamlFile( YAMLFile ) );
    }

    /**
     *  An offline implementation (substituting for {@link #describeAZs(String)}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in {AWSCFNHOME +"/config/inputs/"} folder.
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList< LinkedHashMap<String,Object> > describeAZs_Offline( final String _regionStr ) throws Exception  {
        final String YAMLFile = AWSCFNHOME +"/config/inputs/AWS.AZlist-"+ _regionStr +".yaml";
        return convNode2ArrayOfMaps( readYamlFile( YAMLFile ) );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Pass in a region-name and get back the output of the cmdline as JSON (cmdline being:- aws ec2 describe-regions --profile ______ --output json)
     *  @return An array of YAML-Maps.  Its exactly === cmdline output of: aws ec2 describe-regions --profile ______ --output json
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public ArrayList<String> getRegions() throws Exception {
        if ( this.offline ) return getRegions_Offline();

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( null );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        final DescribeRegionsResult regions_response = ec2.describeRegions();
        final ArrayList<String> retarr = new ArrayList<String>();
        for(Region region : regions_response.getRegions()) {
            // System.out.printf( "Found region %s with endpoint %s\n", region.getRegionName(), region.getEndpoint());
            retarr.add( region.getRegionName() );
        }
        return retarr;
    }

    /**
     *  Pass in a region-name and get back ONLY THE AZ-NAMES in the output of the cmdline as JSON (cmdline being:- aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return An array of Strings.
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public ArrayList<String>  getAZs( final String _regionStr ) throws Exception {
        if ( this.offline ) return getAZs_Offline( _regionStr );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
        final ArrayList<String> retarr = new ArrayList<String>();
        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
            // System.out.printf( "Found availability zone %s with status %s in region %s\n", zone.getZoneName(), zone.getState(), zone.getRegionName());
            if ( zone.getState().equals("available") ) retarr.add( zone.getZoneName() );
        }
        return retarr;
    }

    /**
     * Pass in a region-name and get back the output of the cmdline as JSON (cmdline being:- aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json)
     * @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     * @return An array of YAML-Maps.  Its exactly === cmdline output of: aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json
     * @throws Exception any runtime Exception
     */
    public ArrayList< LinkedHashMap<String,Object> >  describeAZs( final String _regionStr ) throws Exception {
        if ( this.offline ) return describeAZs_Offline( _regionStr );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
        final ArrayList< LinkedHashMap<String,Object> > retarr = new ArrayList<>();

        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/AvailabilityZone.html
        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
            // System.out.printf( "Found availability zone %s with status %s in region %s\n", zone.getZoneName(), zone.getState(), zone.getRegionName());
            // ASSUMPTION: I printed zone.toString() to STDOUT, and noted it's proper JSON.
            // String s = zone.toString().replaceAll("=",":");
            // above zone.toString returns INVALID JSON.. that causes problems elsewhere.. when loading these strings.
            // So, manually have to create JSON here.
            // String s = "{ ";
            // s+= "ZoneName: '"+ zone.getZoneName()   +"',";
            // s+= "ZoneId: '"  + zone.getZoneId()     +"',";
            // s+= "State: '"   + zone.getState()      +"',";
            // s+= "RegionName: '"+ zone.getRegionName() +"',";
            // String sm = "";
            // for( AvailabilityZoneMessage azm: zone.getMessages() ) {
            //     if (   !   sm.equals("") ) sm += ",";
            //     s += "'"+ azm.getMessage() + "'";
            // }
            // s+= "Messages: [" + sm + "] }";
            // if ( this.verbose ) System.out.println( CLASSNAME +": describeAZs(): aws ec2 describe-az command output corrected to be JSON-compatible as ["+ s +"]" );
            // final LinkedHashMap<String,Object> map = new Tools(this.verbose).JSONString2YAML( s );
            // if ( this.verbose ) System.out.println( map.toString() );
            // retarr.add( map );

            final LinkedHashMap<String,Object> oneZone = new LinkedHashMap<>();
            oneZone.put( "ZoneName",    zone.getZoneName() );
            oneZone.put( "ZoneId",      zone.getZoneId() );
            oneZone.put( "State",       zone.getState() );
            oneZone.put( "RegionName",  zone.getRegionName() );
            final ArrayList<String> sm = new ArrayList<>();
            for( AvailabilityZoneMessage azm: zone.getMessages() ) {
                sm.add( azm.getMessage() );
            }
            oneZone.put( "Messages", sm );
            if ( this.verbose ) System.out.println( CLASSNAME +": describeAZs(): aws ec2 describe-az command output in JSON-compatible form is ["+ oneZone.toString() +"]" );

            retarr.add( oneZone );

        }
        return retarr;
    }


    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  <p>Given the region, delete the key-pair, with the name provided as the 2nd argument.</p>
     *  <p>You should ideally first call {@link #listKeyPairEC2(String, String)}, to ensure a key-pair with that name does Not already exist.</p>
     *  <p>ATTENTION! If the keypair does Not exist, AWS APIs return quietly  So, the only way to know something actually got deleted? .. is to _COMPARE_ the output of {@link #listKeyPairEC2(String, String)} before and after that call to this method .</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _MySSHKeyName optional (in fact, you can pass in 'null' or an empty-string as a string-value and it will be treated as java's null)
     *  @throws Exception com.amazonaws.services.ec2.model.AmazonEC2Exception gets thrown if any errors with AWS APIs.
     */
    public void deleteKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        final String HDR = CLASSNAME +"deleteKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            return;
        }

        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/DeleteKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        final DeleteKeyPairRequest request = new DeleteKeyPairRequest().withKeyName( _MySSHKeyName );
        final DeleteKeyPairResult response = ec2.deleteKeyPair(request);
        if ( this.verbose) System.out.println( HDR +"DeleteKeyPairResult =\n"+ response.toString() );
        if ( response == null ||    !  "{}".equals( response.toString().trim() ) )
            throw new Exception( "FAILURE invoking AWS-SDK re: "+ HDR +"\nResponse="+response );
    }

    /**
     *  <p>Given the region, you'll get the private-key 'material' for a new key, with the name provided as the 2nd argument.</p>
     *  <p>You should first call {@link #listKeyPairEC2(String, String)}, to ensure a key-pair with that name does Not already exist.</p>
     *  <p>Currently, no attempt is made here to verify the private-key 'material', with the key's fingerprint.</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _MySSHKeyName optional (in fact, you can pass in 'null' or an empty-string as a string-value and it will be treated as java's null)
     *  @return a list of at least 1 item (see above note as to why you'll never see a list of zero-size.)
     *  @throws Exception com.amazonaws.services.ec2.model.AmazonEC2Exception gets thrown if any errors with AWS APIs.
     */
    public String createKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        final String HDR = CLASSNAME +"createKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            return "__OFFLINE_MODE - AWS.SDK PROJECT - createKeyPairEC2()";
        }

        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/CreateKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        final CreateKeyPairRequest request = new CreateKeyPairRequest().withKeyName( _MySSHKeyName );
        final CreateKeyPairResult response = ec2.createKeyPair(request);
        if (this.verbose) System.out.println( HDR +"CreateKeyPairResult =\n"+ response.getKeyPair().toString() );
        // response.getKeyPair().toString().. .. is JSON containing 3 elements: {KeyFingerprint: 29:cf:7b:f2:19:30:59:4f:96:1a:cc:3c:4d:de:2e:ce:d4:68:b0:79, KeyMaterial: -----BEGIN RSA PRIVATE KEY----- .. .. .. -----END RSA PRIVATE KEY-----,KeyName: <_MySSHKeyName>}
        if ( this.verbose) System.out.println( HDR +"CreateKeyPairResult KeyFingerprint=\n"+ response.getKeyPair().getKeyFingerprint() );
        if ( this.verbose) System.out.println( HDR +"CreateKeyPairResult KeyMaterial=\n"+ response.getKeyPair().getKeyMaterial() );
        if ( this.verbose) System.out.println( HDR +"CreateKeyPairResult KeyName=\n"+ response.getKeyPair().getKeyName() );
        // This above println gives EXACTLY IDENTICAL to _MySSHKeyName
        // !!!!!!!!!!!!!!!!!!!!!! ATTENTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // The following 3 lines .. to _PARSE_ the JSON __WILL__ __FAIL__, as the value of 1st sub-element contains ':' (colons)
        // final LinkedHashMap<String, Object> jsonmap = JSONTools.JSONString2Map( this.verbose, response.getKeyPair().toString() ); <<--- !!!!!!!!!!!!!!!! NEVER WORKS !!!!!!!!!!!!!!!!!!!!
        // if ( jsonmap.get("KeyFingerprint") == null || jsonmap.get("KeyMaterial") == null ||   !   _MySSHKeyName.equals( jsonmap.get("KeyName") ) )
        //     throw new Exception( "FAILURE invoking AWS-SDK re: "+ HDR +"\nResponse="+response );
        return response.getKeyPair().getKeyMaterial();
    }

    /**
     *  <p>Given the region, you'll get back the list of all KeyPairs associated with EC2 instances in that region.  If you provide the OPTIONAL KeyPairname, then only KeyPairs matching it will be returned.</p>
     *  <p>unfortunately, if the optional KeyPair-name is invalid, you'll get a lot of warnings and error messages on STDERR, that comes from  com.amazonaws.util library.  I can't prevent it.</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _MySSHKeyName optional (in fact, you can pass in 'null' or an empty-string as a string-value and it will be treated as java's null)
     *  @return a list of at least 1 item (see above note as to why you'll never see a list of zero-size.)
     *  @throws Exception com.amazonaws.services.ec2.model.AmazonEC2Exception gets thrown if any errors with AWS APIs.
     */
    public List<KeyPairInfo>  listKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        final String HDR = CLASSNAME +"listKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            return new ArrayList<KeyPairInfo>();
        }

        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/CreateKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        final DescribeKeyPairsRequest request = (_MySSHKeyName == null || "".equals( _MySSHKeyName.trim())  || "null".equals( _MySSHKeyName.trim()) )
                                ? new DescribeKeyPairsRequest()
                                : new DescribeKeyPairsRequest().withKeyNames( _MySSHKeyName );

        
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        try {
            final DescribeKeyPairsResult response = ec2.describeKeyPairs( request );
            final List<KeyPairInfo> keys = response.getKeyPairs();
            if (this.verbose) System.out.println( HDR +"DescribeKeyPairsResult has "+ keys.size() +"keys =\n"+ keys );
            return keys;
        } catch( com.amazonaws.services.ec2.model.AmazonEC2Exception ae ) {
            if ( ae.getMessage().contains("does not exist (Service: AmazonEC2; Status Code: 400; Error Code: InvalidKeyPair.NotFound; ") ) {
                return new LinkedList<KeyPairInfo>();
            } else
                throw ae;
        }
        // for ( KeyPairInfo x: keys ) {
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyName=\n"+ x.getKeyName() );
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyFingerprint=\n"+ x.getKeyFingerprint() );
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyMaterial=\n"+ x.getKeyMaterial() );
        // }
        // This above println gives EXACTLY IDENTICAL to _MySSHKeyName
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * Initialize &amp; Connect into AWS, by leveraging the AWS-credentials stored in a file called 'profile' in the current working directory from which this code is being run
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _offline 'true' === this entire class and all it's methods will use "cached" output (a.k.a. files under {ASUXCFNHOME}/configu/inputs folder), instead of invoking AWS SDK calls.
     *  @return return a handle to the SDK - for further calls to methods within this class
     *  @throws FileNotFoundException if the file named 'profile' does NOT exist in current-folder (it should contain the aws.accessKeyId and aws.secretAccessKey)
     *  @throws Exception if any AWS SDK timesout or other errors/exceptions from AWS SDK
     */
    public static AWSSDK AWSCmdline( final boolean _verbose, final boolean _offline )  throws FileNotFoundException, Exception
    {
        final String HDR = CLASSNAME + ": AWSCmdline(): ";
        final String homedir = System.getProperty("user.home");
        assertTrue( homedir != null );
        final File awsuserhome = new File( homedir +"/.aws" );
        awsuserhome.mkdirs();
        // final String AWSProfileFileNameStr = homedir +"/.aws/profile";
        final Path AWSProfileFilePath = FileSystems.getDefault().getPath( homedir, "/.aws/profile" );
        final String AWSProfileFileNameStr = AWSProfileFilePath.toString();

        try {
            final Set<PosixFilePermission> perms = Files.getPosixFilePermissions( AWSProfileFilePath );
            if ( perms.contains(PosixFilePermission.GROUP_READ) || perms.contains(PosixFilePermission.OTHERS_READ) )
                throw new Exception( "SECURITY-RISK! filepath '"+ AWSProfileFileNameStr +"' is ACCESSIBLE to GROUP and OTHER user-groups."  );
            //-----------------
            // final File file = new File ( AWSProfileFileNameStr );
            // file.setReadable( true, true ); // even if user forgets, make sure GROUP and OTHERS do NOT have permissions to this file.
            // file.setWritable( true, true ); // even if user forgets, make sure GROUP and OTHERS do NOT have permissions to this file.
            // file.setExecutable( true, true ); // even if user forgets, make sure GROUP and OTHERS do NOT have permissions to this file.
            //-----------------
            // final Set<PosixFilePermission> posixperms = new HashSet<PosixFilePermission>();
            // posixperms.add( PosixFilePermission.OWNER_READ );
            // posixperms.add( PosixFilePermission.OWNER_WRITE );
            // posixperms.add( PosixFilePermission.OWNER_EXECUTE );
            // // PosixFilePermission.GROUP_READ, GROUP_WRITE, GROUP_EXECUTE);
            // // PosixFilePermission.OTHERS_READ, OTHERS_WRITE, OTHERS_EXECUTE);            
            // Files.setPosixFilePermissions( AWSProfileFilePath, posixperms);
        } catch(SecurityException se) {
            if ( _verbose ) se.printStackTrace(System.err);
            System.err.println( "\n"+ se +"\n\nUnable to ensure filepath ["+ AWSProfileFileNameStr +"] is NOT ACCESSIBLE to GROUP and OTHER user-groups.\n\n" );
            // throw fe;
        } catch(java.io.IOException fe) {
            if ( _verbose ) fe.printStackTrace(System.err);
            System.err.println( "\n"+ fe +"\n\nUnable to ensure filepath ["+ AWSProfileFileNameStr +"] is NOT ACCESSIBLE to GROUP and OTHER user-groups.\n\n" );
            // throw fe;
        }

        try{
            final Properties p = new Properties();
            p.load( new FileInputStream( AWSProfileFileNameStr ) );
            // Not 100% understand why it is ___VERY IMPORTANT___ to load the 'Profile' file INTO the System.properties .. , otherwise.. I get NULLPointerException from within Amazon's AWS SDK-java-library.
            System.getProperties().load( new FileInputStream( AWSProfileFileNameStr ) );
            final String AWSAccessKeyId = System.getProperty( "aws.accessKeyId");
            final String AWSSecretAccessKey = System.getProperty( "aws.secretAccessKey");
            // System.out.println( "AWSAccessKeyId=["+ AWSAccessKeyId +" AWSSecretAccessKey=["+ AWSSecretAccessKey +"]" );

            if ( _offline ) {
                final AWSSDK awssdk = new AWSSDK(_verbose);
                return awssdk;
            } else {
                final AWSSDK awssdk = ( AWSSDK.getConnectionNoThrow() != null )
                                        ? AWSSDK.getConnectionNoThrow()
                                        : AWSSDK.getConnection( _verbose, AWSAccessKeyId, AWSSecretAccessKey );
                return awssdk;
            }

        } catch(FileNotFoundException fe) {
            if ( _verbose ) fe.printStackTrace(System.err);
            System.err.println( "\n"+ fe +"\n\nUnable to load the AWS Profile file (containing AWS Key) at the path ["+ AWSProfileFileNameStr +"].\n" );
            throw fe;
        } catch (Exception e) {
            if ( _verbose ) e.printStackTrace(System.err);
            System.err.println( "\n"+ e +"\n\nSee details in the lines above.\n" );
            throw e;
        }
        // return null;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Given a FQDN like 'example.com' (if that is hosted by Route53), you'll get the ZoneID (like 'Z2NF71MJ75KYXK') back (if search request is valid)
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _DNSHostedZoneName a NotNull string like 'server.subdomain.example.com'
     *  @return a NotNull string like 'Z2NF71MJ75KYXK' (representing the HostedZoneID as you can see within Route53 domain)
     *  @throws Exception throws InvalidInputException, if input is not valid /or/ throws InvalidDomainNameException, if specified domain name is not valid.
     */
    public final String getHostedZoneId( final String _regionStr, final String _DNSHostedZoneName ) throws Exception {
        final String HDR = CLASSNAME +"getHostedZoneId("+ _regionStr +","+ _DNSHostedZoneName +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            // throw new Exception( "AWS.SDK failed to find the HostedDomain under the name ''"+ _DNSHostedZoneName + "'" );
            return "__OFFLINE_MODE - AWS.SDK PROJECT - getHostedZoneId()";
        }

        final AmazonRoute53 Rt53 = this.getAWSRoute53Hndl( _regionStr );
        final ListHostedZonesByNameRequest req = new ListHostedZonesByNameRequest().withDNSName( _DNSHostedZoneName +"." );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html#listHostedZonesByName-com.amazonaws.services.route53.model.ListHostedZonesByNameRequest-
        final ListHostedZonesByNameResult response = Rt53.listHostedZonesByName( req ); // Retrieves a list of the public + private hosted zones
        // Attention: 'ListHostedZonesByNameResult' (ListHostedZonesByName()) sorts hosted-zones by name with the labels reversed. For example:    com.example.www.
        String zoneid = response.getHostedZoneId(); // !!!!!!!!!!!!!!!!!! ATTENTION !!!!!!!!!!!!!!!!!!!!! Apparently, this always returns 'null'.   So, we need if-else below.
        if ( zoneid != null ) {
            return zoneid;
        } else {
            final List<HostedZone> zones = response.getHostedZones();
            if ( zones.size() > 0 ) {
                final HostedZone zone1 = zones.get(0);
                if (this.verbose) System.out.println( HDR + "_DNSHostedZoneName " + _DNSHostedZoneName + " zone1= " + zone1  + " zone1.getId()= " + zone1.getId()  );
                return zone1.getId().replaceFirst( "/hostedzone/", "" );
            } else {
                throw new Exception( "AWS.SDK failed to find the HostedDomain under the name ''"+ _DNSHostedZoneName + "'" );
            }
        }
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================


    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================


    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================


    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    // private void  CLIPBOARD( final String _regionStr ) {
    //     // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_Region.html
    //     final Region myRegion = new Region().withRegionName( _regionStr );
    //     // AmazonEC2Client.serviceMetadata().regions().forEach(System.out::println);
    // }

    public static void main(String[] args) {
        try {
            final AWSSDK awssdk = AWSCmdline( true, true );
            awssdk.getRegions( ).forEach( s -> System.out.println(s) );
            System.out.println("\n\n");
            awssdk.getAZs( args[0] ).forEach( s -> System.out.println(s) );

        } catch (Exception e) {
            e.printStackTrace(System.err); // main() for unit testing
            System.err.println( CLASSNAME + ": main():  \n\nSee details in the lines above.");
            // System.exit(103);
        }

    }


}
