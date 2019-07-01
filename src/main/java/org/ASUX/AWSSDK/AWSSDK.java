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

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;
//import java.util.regex.*;
import java.util.Set;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;

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
     *  This constructor allows us to centralize the authentication.  But then again.. what if Classses have to pass this object around?  For that, use the Static Factory function connect()
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _AWSAccessKeyId your AWS credential with API-level access as appropriate
     *  @param _AWSSecretAccessKey your AWS credential with API-level access as appropriate
     */
    public AWSSDK(boolean _verbose, final String _AWSAccessKeyId, final String _AWSSecretAccessKey) {
        this.verbose = _verbose;
        this.AWSAuthenticate( _AWSAccessKeyId, _AWSSecretAccessKey );
    }

    private AWSSDK() {
        this.verbose = false;
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
        if ( this.AWSAuthenticationHndl == null )
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

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * Pass in a region-name and get back the output of the cmdline as JSON (cmdline being:- aws ec2 describe-regions --profile ______ --output json)
     * @return An array of YAML-Maps.  Its exactly === cmdline output of: aws ec2 describe-regions --profile ______ --output json
     */
    public ArrayList<String> getRegions( ) {
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
     * Pass in a region-name and get back ONLY THE AZ-NAMES in the output of the cmdline as JSON (cmdline being:- aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json)
     * @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     * @return An array of Strings.
     */
    public ArrayList<String>  getAZs( final String _regionStr ) {
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

    public void deleteKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/DeleteKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final String HDR = CLASSNAME +"deleteKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        final DeleteKeyPairRequest request = new DeleteKeyPairRequest().withKeyName( _MySSHKeyName );
        final DeleteKeyPairResult response = ec2.deleteKeyPair(request);
        if ( this.verbose) System.out.println( HDR +"DeleteKeyPairResult =\n"+ response.toString() );
        if ( response == null ||    !  "{}".equals( response.toString().trim() ) )
            throw new Exception( "FAILURE invoking AWS-SDK re: "+ HDR +"\nResponse="+response );
    }

    public String createKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/CreateKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final String HDR = CLASSNAME +"createKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
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

    public List<KeyPairInfo>  listKeyPairEC2( final String _regionStr, final String _MySSHKeyName ) throws Exception {
        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/CreateKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final String HDR = CLASSNAME +"createKeyPairEC2("+ _regionStr +","+ _MySSHKeyName +"): ";
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        final DescribeKeyPairsRequest request = (_MySSHKeyName != null || "".equals( _MySSHKeyName.trim()) )
                                ? new DescribeKeyPairsRequest().withKeyNames( _MySSHKeyName )
                                : new DescribeKeyPairsRequest();
        final DescribeKeyPairsResult response = ec2.describeKeyPairs( request );
        final List<KeyPairInfo> keys = response.getKeyPairs();
        if (this.verbose) System.out.println( HDR +"DescribeKeyPairsResult has "+ keys.size() +"keys =\n"+ keys );
        // for ( KeyPairInfo x: keys ) {
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyName=\n"+ x.getKeyName() );
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyFingerprint=\n"+ x.getKeyFingerprint() );
        //     if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyMaterial=\n"+ x.getKeyMaterial() );
        // }
        // This above println gives EXACTLY IDENTICAL to _MySSHKeyName
        return keys;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * Initialize &amp; Connect into AWS, by leveraging the AWS-credentials stored in a file called 'profile' in the current working directory from which this code is being run
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @return return a handle to the SDK - for further calls to methods within this class
     *  @throws FileNotFoundException if the file named 'profile' does NOT exist in current-folder (it should contain the aws.accessKeyId and aws.secretAccessKey)
     *  @throws Exception if any AWS SDK timesout or other errors/exceptions from AWS SDK
     */
    public static AWSSDK AWSCmdline( final boolean _verbose )  throws FileNotFoundException, Exception
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

            final AWSSDK awssdk = ( AWSSDK.getConnectionNoThrow() != null )
                                    ? AWSSDK.getConnectionNoThrow()
                                    : AWSSDK.getConnection( _verbose, AWSAccessKeyId, AWSSecretAccessKey );
            return awssdk;

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

    // private void  CLIPBOARD( final String _regionStr ) {
    //     // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_Region.html
    //     final Region myRegion = new Region().withRegionName( _regionStr );
    //     // AmazonEC2Client.serviceMetadata().regions().forEach(System.out::println);
    // }

    public static void main(String[] args) {
        try {
            final AWSSDK awssdk = AWSCmdline( true );
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
