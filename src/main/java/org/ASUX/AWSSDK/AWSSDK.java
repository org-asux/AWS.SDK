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
import org.ASUX.common.Tuple;
import org.ASUX.common.Inet;

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

import java.util.regex.*;

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

// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/AmazonServiceException.html
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/AmazonClientException.html
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/SdkClientException.html
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;

// https://github.com/eugenp/tutorials/tree/master/aws/src/main/java/com/baeldung
// https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_Region.html
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/package-summary.html
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2.html
import com.amazonaws.services.ec2.AmazonEC2; // <<------- !!!!!!!!  This is an _INTERFACE_;  A Concrete implementation-class is 'AmazonEC2Client'
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2Client.html
import com.amazonaws.services.ec2.AmazonEC2Client; // <<---- !!!!!!!!! This implements the _INTERFACE_ called 'AmazonEC2'

import com.amazonaws.services.ec2.model.*;
// !!!!!!! ATTENTION !!!!!!!! 'Tag' clashes with SnakeYaml's 'Tag';  So, cannot import this.
// import com.amazonaws.services.ec2.model.Tag;         // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Tag.html
// import com.amazonaws.services.ec2.model.Region;
// import com.amazonaws.services.ec2.model.AvailabilityZone;
    // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/AvailabilityZone.html
// import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
// import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/model/AssociateVPCWithHostedZoneRequest.html
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneRequest;
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneResult;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/model/VPC.html
import com.amazonaws.services.route53.model.VPC;    // !!!!!!!!!!!!!!!!! ATTENTION !!!!!!!!!!!!!!!!!! This is a different VPC class !!!!!!!!!!!!!!!!!!

import com.amazonaws.services.ec2.model.Vpc;        // !!!!!!!!!!!!!!!!! ATTENTION !!!!!!!!!!!!!!!!!! This is a different VPC class !!!!!!!!!!!!!!!!!!
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.DeleteKeyPairRequest;
import com.amazonaws.services.ec2.model.DeleteKeyPairResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.KeyPairInfo;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2.html#describeSubnets--
import com.amazonaws.services.ec2.model.Subnet;
// import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2.html#describeSecurityGroups--
import com.amazonaws.services.ec2.model.SecurityGroup;
// import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/IpPermission.html
import com.amazonaws.services.ec2.model.IpPermission; // represents the IP port associated with a SecurityGroup (see above)
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

// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/InternetGateway.html
import com.amazonaws.services.ec2.model.InternetGateway;
import com.amazonaws.services.ec2.model.DescribeInternetGatewaysRequest;
import com.amazonaws.services.ec2.model.DescribeInternetGatewaysResult;
// aws ec2 describe-internet-gateways --query 'InternetGateways[*].InternetGatewayId'   <-- will return ALL known IGW IDs (in that region).  Note! No VPC mentioned!

// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3-objects.html
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html#listResourceRecordSets-com.amazonaws.services.route53.model.ListResourceRecordSetsRequest-
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder;
import com.amazonaws.services.route53.model.ListHostedZonesByNameRequest;
import com.amazonaws.services.route53.model.ListHostedZonesByNameResult;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.HostedZoneConfig;
// import com.amazonaws.services.route53.model.ListResourceRecordSetsResult;
// import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/identitymanagement/AmazonIdentityManagement.html
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.GetUserResult; // for getUser()  // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/identitymanagement/model/GetUserResult.html
import com.amazonaws.services.identitymanagement.model.User; // user.getArn() and user.getCreateDate()/java.util.Data and user.getTags() and user.getUserId() and user.getUserName()/Friendly-username
// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/identitymanagement/model/User.html

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
    protected enum ProgressBarMileStones { STARTING, INPROGRESS, COMPLETED, UNKNOWN };

    protected void showProgressbar( final boolean _justReadOnly, final ProgressBarMileStones _stepStatus, final String _context ) {
        // if ( this.verbose )
        //     System.out.println( ?? Why ?? we've tons of verbose output everywhere !!! )
        if ( _justReadOnly ) {
            switch ( _stepStatus ) {
            case STARTING:      System.out.print(".");  break;
            case INPROGRESS:    System.out.print(".");  break;
            case COMPLETED:     System.out.print(". "); break;
            case UNKNOWN:
            default:            assertTrue( false );
            } // end switch
        } else {
            switch ( _stepStatus ) {
            case STARTING:      System.out.print( " !"+ _context ); break;
            case INPROGRESS:    System.out.print(".");  break;
            case COMPLETED:     System.out.print(". "); break;
            case UNKNOWN:
            default:            assertTrue( false );    break;
            } // end switch
        } // if-else
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
        showProgressbar( false, ProgressBarMileStones.STARTING, "Logging in" );
        // Authenticate into AWS
        // https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html
        this.aws_credentials = new BasicAWSCredentials( _AWSAccessKeyId, _AWSSecretAccessKey );
        showProgressbar( false, ProgressBarMileStones.INPROGRESS, null );

        this.AWSAuthenticationHndl = new AWSStaticCredentialsProvider( this.aws_credentials );
        showProgressbar( false, ProgressBarMileStones.COMPLETED, null );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    private AmazonEC2 getAWSEC2Hndl( final String _regionStr ) {
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-ec2-regions-zones.html
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        showProgressbar( false, ProgressBarMileStones.STARTING, "AWS.SDK.EC2 init" );
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        showProgressbar( false, ProgressBarMileStones.COMPLETED, null );
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().build();
        // To use the default credential/region provider chain 
        // Ec2Client ec2 = Ec2Client.create(); // AWS_REGION is checked .. ~/.aws/config default profile .. aws.profile system property
        return ec2;
    }

    private AmazonRoute53 getAWSRoute53Hndl( final String _regionStr ) {
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html 
        // final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.defaultClient();
        showProgressbar( false, ProgressBarMileStones.STARTING, "AWS.SDK.Rt53 init" );
        final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        showProgressbar( false, ProgressBarMileStones.COMPLETED, null );
        // final AmazonRoute53 Rt53 = AmazonRoute53ClientBuilder.standard().build();
        // To use the default credential/region provider chain 
        // AmazonRoute53Client Rt53 = AmazonRoute53Client.create(); // AWS_REGION is checked .. ~/.aws/config default profile .. aws.profile system property
        return Rt53;
    }

    private AmazonIdentityManagement getIAMHndl( final String _regionStr ) {
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/identitymanagement/AmazonIdentityManagement.html
        // final AmazonIdentityManagement IAM = AmazonIdentityManagement.defaultClient();
        showProgressbar( false, ProgressBarMileStones.STARTING, "AWS.SDK.IAM init" );
        final AmazonIdentityManagement IAM = AmazonIdentityManagementClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        showProgressbar( false, ProgressBarMileStones.COMPLETED, null );
        // final AmazonIdentityManagement IAM = AmazonIdentityManagementClientBuilder.standard().build();
        // To use the default credential/region provider chain 
        // AmazonIdentityManagement IAM = AmazonIdentityManagement.create(); // AWS_REGION is checked .. ~/.aws/config default profile .. aws.profile system property
        return IAM;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * Given a AWS-Region, this method looks up _ALL_ IGWs, and then .. checks to see if there are ANY associated with the VPC (passed as 2nd argument).
     * @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     * @param _myVPC NotNull the VPC ID.  Note: If you'd like to get _ALL_ IGWs, to attach to your _NEW_ VPC, use 
     * @return either Null (if No IGWs are __ASSOCIATED__ with the _myVPC argument), or the ID of the 1st IGW associated with your _myVPC
     */
    public String getIGWForVPC( final String _regionStr, final String _myVPC ) {
        final String HDR = CLASSNAME +": getIGWs("+ _regionStr +","+ _myVPC +"): ";
        assertTrue( _regionStr != null);
        assertTrue( _myVPC != null );

        if ( this.offline ) return "running--offline-No-IGW";

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );

        // final DescribeInternetGatewaysResult igwGWResult = new DescribeInternetGatewaysRequest().withInternetGatewayIds( _getExistingIGW );

        showProgressbar( true, ProgressBarMileStones.STARTING, null );
        final DescribeInternetGatewaysResult igwGWResult = ec2.describeInternetGateways( new DescribeInternetGatewaysRequest() );
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        for( InternetGateway igw : igwGWResult.getInternetGateways() ) {
            if ( this.verbose )  System.out.println( HDR +"IGW ID#"+ igw.getInternetGatewayId() +".. .. Checking, whether it belongs to my VPC.." );
            for ( InternetGatewayAttachment attachment : igw.getAttachments() ) {
                if ( this.verbose ) System.out.println( HDR +"FYI: IGW ID#"+ igw.getInternetGatewayId() +" is currently attached to VPC ID# "+ attachment.getVpcId() );
                if ( attachment.getVpcId().equals( _myVPC ) ) {
                    return igw.getInternetGatewayId();
                } // if
            } // INNER For-loop
        } // OUTER For-loop

        return null;
    }

    /**
     *  Given a AWS-Region, this method looks up _ALL_ IGWs (in your account for _THAT_ region)
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _bUnassociated true if you need a IGW that is NOT associated with ANY VPC; False otherwise.
     *  @return either __EMPTY_ list (if No IGWs are __ASSOCIATED__ with the region)..  or, one ore more KV-PAIRS(IGWID,VPCID).  Guaranteed to be NotNull
     */
    public ArrayList< Tuple<String,String> >  getIGWs( final String _regionStr, final boolean _bUnassociated ) {
        final String HDR = CLASSNAME +": getIGWs("+ _regionStr +"): ";
        assertTrue( _regionStr != null);
        if ( this.offline ) {
            final ArrayList< Tuple<String,String> >   r = new ArrayList<>();
            r.add( new Tuple<>( "running--offline-No-list-of-IGWs","?VPCID?") );
            return r;
        }

        final ArrayList< Tuple<String,String> > arr = new ArrayList< Tuple<String,String> >();

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );

        showProgressbar( true, ProgressBarMileStones.STARTING, null );
        final DescribeInternetGatewaysResult igwGWResult = ec2.describeInternetGateways( new DescribeInternetGatewaysRequest() );
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        for( InternetGateway igw : igwGWResult.getInternetGateways() ) {
            if ( this.verbose ) System.out.println( HDR +"IGW ID#"+ igw.getInternetGatewayId() +".. .. It is attached to "+ igw.getAttachments().size() +" VPCs." );
            for ( InternetGatewayAttachment attachment : igw.getAttachments() ) { // This loop is just for debugging purposes only!
                if ( this.verbose ) System.out.println( HDR +"IGW ID#"+ igw.getInternetGatewayId() +" is attached to VPC ID# "+ attachment.getVpcId() );
            }
            if ( _bUnassociated ) {
                if ( igw.getAttachments().size() <= 0 ) {
                    arr.add( new Tuple<String,String>( igw.getInternetGatewayId(), UNATTACHED_IGW ) );
                } else {
                    if ( this.verbose ) System.out.println( HDR +"Skipping VPC ID# "+ igw.getInternetGatewayId() +" .. because it is ATTACHED to another VPC already" );
                    continue; // This is _NOT_ an un-attached IGW.
                }
            } else {
                for ( InternetGatewayAttachment attachment : igw.getAttachments() ) {
                    arr.add( new Tuple<String,String>( igw.getInternetGatewayId(), attachment.getVpcId() ) );
                } // For-loop
            } // if-else
        } // OUTER For-loop

        return arr;
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

    /** = System.getProperty("ORGASUXHOME"); */
    public static final String ORGASUXHOME      = System.getProperty("ORGASUXHOME");

    /** = System.getProperty("AWSHOME"); */
    public static final String AWSHOME          = System.getProperty("AWSHOME");
    // public static final String AWS C FN HOME       = System.getProperty("AWS C FN HOME");  // if you need this, we will end up with circular dependencies between org.ASUX.AWS-SDK and org.ASUX.AWS.CFN projects,

    //--------------------
    private static boolean  bNeverRan_getAWSHOME = true;

    /**
     *  If the folder represented by the returned value does Not exist, you'll get a __RUNTIME__ exception (org.junit.Assert.AssertionError)
     *  @return a NotNull path like ~/github.com/org.ASUX/AWS (assuming your installed org.ASUX project at ~/github.com/org.ASUX);  Note: this will never be Null.
     */
    public static final String getAWSHOME() {
        final String awshome = ( AWSHOME == null ) ?  ORGASUXHOME +"/AWS" : AWSHOME;
        if ( AWSSDK.bNeverRan_getAWSHOME ) {
            final File fObj = new File( awshome );
            assertTrue( fObj.exists() && fObj.canRead() );
            AWSSDK.bNeverRan_getAWSHOME = false;
        }
        return awshome;
    }

    private static boolean  bNeverRan_getAWSSDKHOME = true;

    /**
     *  If the folder represented by the returned value does Not exist, you'll get a __RUNTIME__ exception (org.junit.Assert.AssertionError)
     *  @return a NotNull path like ~/github.com/org.ASUX/AWS/AWS-SDK (assuming your installed org.ASUX project at ~/github.com/org.ASUX);  Note: this will never be Null.
     */
    public static final String getAWSSDKHOME() {
        final String awssdkhome = getAWSHOME() +"/AWS-SDK";
        if ( AWSSDK.bNeverRan_getAWSSDKHOME ) {
            final File fObj = new File( awssdkhome );
            assertTrue( fObj.exists() && fObj.canRead() );
            AWSSDK.bNeverRan_getAWSSDKHOME = false;
        }
        return awssdkhome;
    }

    private static boolean  bNeverRan_getOfflineFolderPath = true;
    /**
     *  If the folder represeted by the returned value does Not exist, you'll get a __RUNTIME__ exception (org.junit.Assert.AssertionError)
     *  @return a NotNull path like <code>~/org.ASUX/AWS/etc/offline-downloads</code> (assuming you installed org.ASUX project at <code>~/org.ASUX</code>);  Note: this will never be Null.
     */
    public static final String getOfflineFolderPath() {
        final String offlinefldr = getAWSHOME() + "/AWS-SDK/etc/offline-downloads";
        if ( AWSSDK.bNeverRan_getOfflineFolderPath ) {
            final File fObj = new File( offlinefldr );
            assertTrue( fObj.exists() && fObj.canRead() );
            AWSSDK.bNeverRan_getOfflineFolderPath = false;
        }
        return offlinefldr;
    }

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
            final org.ASUX.common.Output.Object<?> ooo = NodeTools.Node2Map( this.verbose, subN );
            assertTrue( ooo.getType() == org.ASUX.common.Output.OutputType.Type_LinkedHashMap );
            if ( this.verbose ) System.out.println( ooo.getMap() );
            retarr.add( ooo.getMap() );
        }
        return retarr;
    }

    // The following method is _NOT_ going to work - EVER.
    // tag is of Generic-Type t, and compiler will never know what the heck is 'tag.getKey()' and 'tag.getValue()' !!
    // Worse, Amazon does _NOT_ have a 'base-class' for all their Tags classes (example: amazonaws.services.identitymanagement.model.Tag & com.amazonaws.services.ec2.model.Tag)
    // private static <T> LinkedHashMap<String,Object> convertAWSTagsToSimpleList( List<T> _tags ) throws Exception {
    //     // final List<com.amazonaws.services.ec2.model.Tag>	tags = vpc.getTags();
    //     final LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
    //     for ( T tag: _tags ) {
    //         map.put( tag.getKey(), tag.getValue() );
    //     }
    //     return map;
    // }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    public static final String REGION2LOCATIONMAPPING = getAWSSDKHOME()  +"/config/AWSRegionsLocations.properties"; // + org.ASUX.AWS.CFN.EnvironmentParameters.AWSREGIONSLOCATIONS );
    public static final String LOCATION2REGIONMAPPING = getAWSSDKHOME()  +"/config/AWSLocationsRegions.properties"; // + org.ASUX.AWS.CFN.EnvironmentParameters.AWSLOCATIONSREGIONS );

    private boolean bNeverRan_RegionLookup = true;
    private boolean bNeverRan_LocationLookup = true;

    private Properties region2LocationLookupProps = new Properties();
    private Properties location2RegionLookupProps = new Properties();

    //------------------------
    /**
     *  Given a NotNull region-name, will convert it to human-friendly name.  Example: for 'ap-northeast-1' as argument, the return value is 'Tokyo' (initial-capital always)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return If _regionStr is _NOT_ actual AWS region, you'll get null.  Otherwise, a NotNull string
     *  @throws Exception any errors trying to load and parse the file at {@link #REGION2LOCATIONMAPPING}
     */
    private String getLocation( final String _regionStr ) throws Exception {
        if ( bNeverRan_LocationLookup ) {
            final Properties AWSRegions2Locations = org.ASUX.common.Utils.parseProperties( "@"+ REGION2LOCATIONMAPPING ); 
            region2LocationLookupProps.putAll( AWSRegions2Locations );
            bNeverRan_LocationLookup = false;
        }
        // Example: US-EAST-1 --> us-east-1 .. .. 'EU-West-1' -> 'eu-west-1'
        return region2LocationLookupProps.getProperty( "AWS-"+ _regionStr.toLowerCase() );
    }

    //------------------------
    /**
     *  Given a NotNull region-name, will convert it to human-friendly name.  Example: for 'ap-northeast-1' as argument, the return value is 'Tokyo' (initial-capital always)
     *  @param _locationStr pass in valid AWS-Locations like 'virginia', 'Ohio', 'Tokyo', 'Seoul', 'SaoPaulo' .. (case-insensitive is OK)
     *  @return If _locationStr is _NOT_ actual AWS region, you'll get null.  Otherwise, a NotNull string
     *  @throws Exception any errors trying to load and parse the file at {@link #REGION2LOCATIONMAPPING}
     */
    private String getRegion( String _locationStr ) throws Exception {
        if ( bNeverRan_RegionLookup ) {
            final Properties AWSLocations2Regions = org.ASUX.common.Utils.parseProperties( "@"+ LOCATION2REGIONMAPPING ); 
            location2RegionLookupProps.putAll( AWSLocations2Regions );
            bNeverRan_RegionLookup = false;
        }
        // Example: toKYo --> Tokyo .. .. 'SYDNEY' -> 'Sydney'
        _locationStr = _locationStr.toLowerCase();
        _locationStr = Character.toUpperCase( _locationStr.charAt(0) ) + _locationStr.substring( 1 );
        return location2RegionLookupProps.getProperty( "AWS-"+ _locationStr );
    }

    //------------------------
    /**
     *  <p>This utility method is to understand what the user _SLOPPILY_ entered - no matter either 'us-east-1' or 'virGiNia' (in any char-case).</p>
     *  @param _userInput any Nullable String, case-insensitive is OK.
     *  @return Nullable in 2 scenarios: if input-argument is null - or - if input-argument is neither an AWS RegionString(like 'us-east-1') nor is it an AWS Location(like 'Virginia')
     *  @throws Exception anything thrown by {@link #getRegion(String)} and {@link #getLocation(String)} - which are invoked by this method.
     */
    public Tuple<String,String> getRegionAndLocation( final String _userInput ) throws Exception {
        if ( _userInput == null )
            return null;
        final String s = _userInput.toLowerCase();
        if ( this.matchesAWSRegionPattern( s ) ) {
            final String AWSLocation = this.getLocation( s );
            return new Tuple<String,String>( s, AWSLocation );
        }
        // Ok. so _userInput is __NOT__ of the type 'eu-west-1'
        // Perhaps, it is so,mething like 'Tokyo' or 'virGiNia'
        final String AWSRegion = this.getRegion( _userInput );
        if ( AWSRegion != null ) {
            // Example: toKYo --> Tokyo .. .. 'SAOPAULO' -> 'Saopaulo'
            String l = _userInput.toLowerCase();
            l = Character.toUpperCase( l.charAt(0) ) + l.substring( 1 );
            return new Tuple<String,String>( AWSRegion, l );
        }
        return null; // neither an AWS RegionString (like 'us-east-1') nor is it an AWS LocationString (like 'Virginia')
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  An offline implementation (substituting for {@link #getRegions()}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in getOfflineFolderPath() folder.
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList<String> getRegions_Offline() throws Exception {
        final String YAMLFile = getOfflineFolderPath() +"/AWSRegions.yaml";
        return this.convNode2ArrayList( this.readYamlFile( YAMLFile ) );
    }

    /**
     *  <p>An offline implementation (substituting for {@link #getVPCID(String, boolean)}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in getOfflineFolderPath() folder.</p>
     *  <p>Get the single Default-VPC  - or - the 1st Non-Default VPC (based on the argument passed to this method)</p>
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _onlyNonDefaultVPC true if you want the default VPC, else false will return the 1st NON-DEFAULT VPC
     *  @return If no VPCs (imagine that! Not even a Default), you'll get null;  Otherwise. if argument is 'false' and No Non-default-VPCs, again a Null is returned. Otherwise, a NotNull string
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public String getVPCID_Offline( final String _regionStr, final boolean _onlyNonDefaultVPC ) throws Exception {
        final ArrayList< LinkedHashMap<String,Object> > ret =  getVPCs_Offline( _regionStr, _onlyNonDefaultVPC );
        if ( ret.size() <= 0 )
            return null;
        else
            return (String) ret.get( 0 ).get( "ID" ); // could be null - by definition of get().
    }

    /**
     *  <p>An offline implementation (substituting for {@link #getVPCs(String, boolean)} that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in getOfflineFolderPath() folder.</p>
     *  Get the list of VPC-ID for _ALL_ the VPCs (incl. default)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _onlyNonDefaultVPC true if you want the default VPC, else false will return the 1st NON-DEFAULT VPC
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-vpcs --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files 
     */
    public ArrayList< LinkedHashMap<String,Object> > getVPCs_Offline( final String _regionStr, final boolean _onlyNonDefaultVPC ) throws Exception {
        final String YAMLFile = getOfflineFolderPath() +"/VPCdetails"+ (_onlyNonDefaultVPC ? "-NotDefaults" : "-all" ) +"-"+ this.getLocation( _regionStr ) +".yaml";
        return this.convNode2ArrayOfMaps( this.readYamlFile( YAMLFile ) );
    }

    /**
     *  <p>An offline implementation (substituting for {@link #getSubnets(String, String, String)} does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in getOfflineFolderPath() folder.</p>
     *  Get the list of Subnet-ID for _ALL_ the subnets in _ALL_ VPCs (incl. default)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _VPCID An ID in AWS (whether VPC, subnet, SG, EC2...) === prefix('vpc-', 'subnet-', ..) + a hexadecimal suffix {@link org.ASUX.AWSSDK.AWSSDK#REGEXP_AWSID_SUFFIX}.  This method checks against that rule.
     *  @param _PublicOrPrivate whether a public or private subnet EC2 instance (String value is case-sensitive.  Exact allowed values are: 'Public' 'Private')
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-vpcs --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files 
     */
    public ArrayList< LinkedHashMap<String,Object> > getSubnets_Offline( final String _regionStr, final String _VPCID, final String _PublicOrPrivate ) throws Exception {
        final String YAMLFile = getOfflineFolderPath() +"/Subnetdetails"+ _PublicOrPrivate +"-"+ this.getLocation( _regionStr ) +".yaml";
        return this.convNode2ArrayOfMaps( this.readYamlFile( YAMLFile ) );
    }

    /**
     *  <p>An offline implementation (substituting for {@link #getSGs(String, String, String)} that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in getOfflineFolderPath() folder.</p>
     *  Get the list of SG-ID for _ALL_ the Security-Groups in _ALL_ VPCs (incl. default)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _VPCID An ID in AWS (whether VPC, subnet, SG, EC2...) === prefix('vpc-', 'subnet-', ..) + a hexadecimal suffix {@link org.ASUX.AWSSDK.AWSSDK#REGEXP_AWSID_SUFFIX}.  This method checks against that rule.
     *  @param _portOfInterest whether "ssh", "rdp", .. (String value is case-sensitive)
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-vpcs --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files 
     */
    public ArrayList< LinkedHashMap<String,Object> > getSGs_Offline( final String _regionStr, final String _VPCID, final String _portOfInterest ) throws Exception {
        final String YAMLFile = getOfflineFolderPath() +"/SGdetails"+ _portOfInterest +"-"+ this.getLocation( _regionStr ) +".yaml";
        return this.convNode2ArrayOfMaps( this.readYamlFile( YAMLFile ) );
    }

    //==============================================================================
    /**
     *  An offline implementation (substituting for {@link #getAZs(String)}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in {getOfflineFolderPath() folder.
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList<String> getAZs_Offline( final String _regionStr ) throws Exception {
        final String YAMLFile = getOfflineFolderPath() +"/AWS.AZlist-"+ _regionStr +".yaml";
        return this.convNode2ArrayList( this.readYamlFile( YAMLFile ) );
    }

    /**
     *  An offline implementation (substituting for {@link #describeAZs(String)}), that does _NOT_ make api API calls to AWS's SDK.  Instead it looks up cached-files in {getOfflineFolderPath() +"/etc/offline-downloads/"} folder.
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return a NotNull instance
     *  @throws Exception thrown if any issues reading the cached YAML files.
     */
    public ArrayList< LinkedHashMap<String,Object> > describeAZs_Offline( final String _regionStr ) throws Exception  {
        final String YAMLFile = getOfflineFolderPath() +"/AWS.AZlist-"+ _regionStr +".yaml";
        return convNode2ArrayOfMaps( this.readYamlFile( YAMLFile ) );
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
        showProgressbar( true, ProgressBarMileStones.STARTING, null );
        final DescribeRegionsResult regions_response = ec2.describeRegions();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        final ArrayList<String> retarr = new ArrayList<String>();
        for(Region region : regions_response.getRegions()) {
            // System.out.printf( "Found region %s with endpoint %s\n", region.getRegionName(), region.getEndpoint());
            retarr.add( region.getRegionName() );
        }
        return retarr;
    }

    //==============================================================================

    /**
     * @return Based on the Access-Keys used to interact with AWS SDK/APIs, determine the IAM-user that the access-keys belong to
     * @throws Exception if any issues with invoking AWS APIs/SDKs.  There should be No exceptions from this ASUX.org library
     */
    public String getUserARN() throws Exception {
        if ( this.offline ) return "running--offline";
        final AmazonIdentityManagement IAM = this.getIAMHndl( null );
        final com.amazonaws.services.identitymanagement.model.User user = IAM.getUser().getUser();
        return user.getArn();
    }

    //==============================================================================

    /**
     * @return Based on the Access-Keys used to interact with AWS SDK/APIs, determine the IAM-user that the access-keys belong to
     * @throws Exception if any issues with invoking AWS APIs/SDKs.  There should be No exceptions from this ASUX.org library
     */
    public String getUserName() throws Exception {
        if ( this.offline ) return "running--offline";
        final AmazonIdentityManagement IAM = this.getIAMHndl( null );
        // showProgressbar( true, ProgressBarMileStones.???, null );
        final com.amazonaws.services.identitymanagement.model.User user = IAM.getUser().getUser();
        return user.getUserName();
    }

    //==============================================================================

    /**
     * @return Based on the Access-Keys used to interact with AWS SDK/APIs, determine the IAM-user that the access-keys belong to.. and then get the Tags associated with that user.
     * @throws Exception if any issues with invoking AWS APIs/SDKs.  There should be No exceptions from this ASUX.org library
     */
    public LinkedHashMap<String,Object> getUserTags() throws Exception {
        final LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        if ( this.offline ) {
            map.put( "running--offline", "true" );
            return map;
        }
        final AmazonIdentityManagement IAM = this.getIAMHndl( null );
        showProgressbar( true, ProgressBarMileStones.STARTING, null );
        final com.amazonaws.services.identitymanagement.model.User user = IAM.getUser().getUser();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );
        for ( com.amazonaws.services.identitymanagement.model.Tag tag: user.getTags() ) {
            map.put( tag.getKey(), tag.getValue() );
        }
        return map;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Get the single Default-VPC  - or - the 1st Non-Default VPC (based on the argument passed to this method)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _onlyNonDefaultVPC true if you want the default VPC, else false will return the 1st NON-DEFAULT VPC
     *  @return If no VPCs (imagine that! Not even a Default), you'll get null;  Otherwise. if argument is 'false' and No Non-default-VPCs, again a Null is returned. Otherwise, a NotNull string
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public String getVPCID( final String _regionStr, final boolean _onlyNonDefaultVPC ) throws Exception {
        if ( this.offline ) return getVPCID_Offline( _regionStr, _onlyNonDefaultVPC );

        showProgressbar( false, ProgressBarMileStones.STARTING, "getVPCs" );
        for(LinkedHashMap<String,Object> vpc : this.getVPCs( _regionStr, _onlyNonDefaultVPC ) ) {
            showProgressbar( true, ProgressBarMileStones.COMPLETED, null );
            return (String) vpc.get( "ID" ); // return the 1st element found.
        }
        return null;
    }

    //==============================================================================
    public static final String VPC_ISDEFAULT = "isDefault";
    public static final String VPC_ID = "VPC-ID";
    public static final String SUBNET_ID = "SUBNET-ID";
    public static final String SG_ID = "SG-ID";
    public static final String CIDRBLOCK = "CIDR-Block";
    public static final String UNATTACHED_IGW = "Un-attached-IGW";
    //==============================================================================

    /**
     *  Get the list of VPC-ID for _ALL_ the VPCs (incl. default)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _onlyNonDefaultVPC true if you want the default VPC, else false will return the 1st NON-DEFAULT VPC
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-vpcs --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public ArrayList< LinkedHashMap<String,Object> > getVPCs( final String _regionStr, final boolean _onlyNonDefaultVPC ) throws Exception
    {   final String HDR = CLASSNAME +"getVPCs("+ _regionStr +","+ _onlyNonDefaultVPC +"): ";
        if ( this.offline ) return getVPCs_Offline( _regionStr, _onlyNonDefaultVPC );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "describeVPCs" );
        final DescribeVpcsResult vpclist_response = ec2.describeVpcs();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        final ArrayList< LinkedHashMap<String,Object> > retarr = new ArrayList<>();
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Vpc.html
        // warning!  There are 2 VPCs in AWS Data Model !!!
        // com.amazonaws.services.route53.model.VPC
        // com.amazonaws.services.ec2.model.Vpc
        showProgressbar( false, ProgressBarMileStones.STARTING, "getVPCs" );
        for( com.amazonaws.services.ec2.model.Vpc vpc : vpclist_response.getVpcs()) {
            showProgressbar( false, ProgressBarMileStones.INPROGRESS, "getVPCs" );
            if ( this.verbose) System.out.printf( "%s looking at VPC ID=%s with CIDRBlock=%s\n", HDR, vpc.getVpcId(), vpc.getCidrBlock() );
            if ( vpc.isDefault() ) {
                if ( this.verbose) System.out.println( " - is DEFAULT VPC" ); 
                if ( _onlyNonDefaultVPC )
                    continue; // skip the default VPCs if argument passed is true.
            } else {
                if ( this.verbose) System.out.println();
            }
            if ( this.verbose) System.out.println( vpc.toString() );
            final LinkedHashMap<String,Object> ix = new LinkedHashMap<String,Object>();
            // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Vpc.html
            ix.put ( VPC_ISDEFAULT, vpc.isDefault() );
            ix.put ( VPC_ID,        vpc.getVpcId() );
            ix.put ( CIDRBLOCK,     vpc.getCidrBlock() );    // The primary IPv4 CIDR block for the VPC.
            // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Tag.html
            showProgressbar( true, ProgressBarMileStones.STARTING, null );
            final List<com.amazonaws.services.ec2.model.Tag>	tags = vpc.getTags();
            showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

            for ( com.amazonaws.services.ec2.model.Tag tag: tags ) {
                ix.put( tag.getKey(), tag.getValue() );
            }
            retarr.add( ix );
            // List<VpcCidrBlockAssociation>	getCidrBlockAssociationSet() // Information about the IPv4 CIDR-blocks (__MULTIPLE__) associated with the VPC.
        }
        showProgressbar( false, ProgressBarMileStones.COMPLETED, "getVPCs" );
        return retarr;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Get the list of Subnets (and all their details) for a given VPC
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _VPCID An ID in AWS (whether VPC, subnet, SG, EC2...) === prefix('vpc-', 'subnet-', ..) + a hexadecimal suffix {@link org.ASUX.AWSSDK.AWSSDK#REGEXP_AWSID_SUFFIX}.  This method checks against that rule.
     *  @param _PublicOrPrivate whether a public or private subnet EC2 instance (String value is case-sensitive.  Exact allowed values are: 'Public' 'Private')
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-subnets --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public ArrayList< LinkedHashMap<String,Object> > getSubnets( final String _regionStr, final String _VPCID, final String _PublicOrPrivate ) throws Exception
    {   final String HDR = CLASSNAME +"getSubnets("+ _regionStr +","+ _VPCID +","+ _PublicOrPrivate +"): ";
        if ( this.offline ) return getSubnets_Offline( _regionStr, _VPCID, _PublicOrPrivate );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "describeSubnets" );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2.html#describeSubnets--
        final DescribeSubnetsResult subnetlist_response = ec2.describeSubnets();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        final ArrayList< LinkedHashMap<String,Object> > retarr = new ArrayList<>();

        showProgressbar( false, ProgressBarMileStones.STARTING, "getSubnets" );
        for( com.amazonaws.services.ec2.model.Subnet subnet : subnetlist_response.getSubnets() ) {
            showProgressbar( false, ProgressBarMileStones.INPROGRESS, "getSubnets" );
            if ( this.verbose) System.out.printf( "%s looking at Subnet ID=%s in VPC-ID=%s with CIDRBlock=%s\n", HDR, subnet.getVpcId(), subnet.getSubnetId(), subnet.getCidrBlock() );
            if ( this.verbose) System.out.println( subnet.toString() );
            if (   !   subnet.getVpcId().equals( _VPCID ) )
                continue;

            final LinkedHashMap<String,Object> ix = new LinkedHashMap<String,Object>();
            // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Subnet.html
            ix.put ( SUBNET_ID, subnet.getSubnetId() );
            ix.put ( VPC_ID,    subnet.getVpcId() );
            ix.put ( CIDRBLOCK, subnet.getCidrBlock() );    // The  IPv4 CIDR block for the SUBNET.  NOT vpc!
            ix.put ( "AZ",      subnet.getAvailabilityZoneId() );
            ix.put ( "DefaultForAZ", ""+subnet.isDefaultForAz() ); // whether this is the default subnet for the Availability Zone.
            // subnet.getAvailableIpAddressCount() // The number of unused private IPv4 addresses in the subnet.
            // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Tag.html
            showProgressbar( true, ProgressBarMileStones.STARTING, null );
            final List<com.amazonaws.services.ec2.model.Tag>	tags = subnet.getTags();
            showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

            for ( com.amazonaws.services.ec2.model.Tag tag: tags ) {
                ix.put( tag.getKey(), tag.getValue() );
            }
            retarr.add( ix );
        }
        showProgressbar( false, ProgressBarMileStones.COMPLETED, "getSubnets" );
        return retarr;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Get the list of SG-ID for _ALL_ the Security-Groups in _ALL_ VPCs (incl. default)
     *  @param _regionStr NotNull.  Pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @param _VPCID NotNull, An ID in AWS (whether VPC, subnet, SG, EC2...) === prefix('vpc-', 'subnet-', ..) + a hexadecimal suffix {@link org.ASUX.AWSSDK.AWSSDK#REGEXP_AWSID_SUFFIX}.  This method checks against that rule.
     *  @param _portOfInterest NotNull whether "ssh", "rdp", .. (String value is case-sensitive)
     *  @return An NotNull array of KV-pairs.  Its exactly === cmdline output of: aws ec2 describe-vpcs --region ap-northeast-1 --profile ______
     *  @throws Exception thrown if any issues reading the cached YAML files 
     */
    public ArrayList< LinkedHashMap<String,Object> > getSGs( final String _regionStr, final String _VPCID, final String _portOfInterest ) throws Exception
    {   final String HDR = CLASSNAME +"getSGs("+ _regionStr +","+ _VPCID +","+ _portOfInterest +"): ";
        if ( this.offline ) return getSubnets_Offline( _regionStr, _VPCID, _portOfInterest );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "describeSGs" );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/AmazonEC2.html#describeSecurityGroups--
        final DescribeSecurityGroupsResult sglist_response = ec2.describeSecurityGroups();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        final ArrayList< LinkedHashMap<String,Object> > retarr = new ArrayList<>();
        final Inet inet = new Inet( this.verbose );

        showProgressbar( false, ProgressBarMileStones.STARTING, "getSGs" );
        for( com.amazonaws.services.ec2.model.SecurityGroup sg : sglist_response.getSecurityGroups() ) {
            showProgressbar( false, ProgressBarMileStones.INPROGRESS, "getSGs" );
            if ( this.verbose) System.out.printf( "%s looking at SG ID=%s in VPC-ID=%s with Name=%s\n", HDR, sg.getVpcId(), sg.getGroupId(), sg.	getGroupName() );
            if ( this.verbose) System.out.println( sg.toString() );
            if (   !   sg.getVpcId().equals( _VPCID ) )
                continue;

            // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/IpPermission.html
            for ( com.amazonaws.services.ec2.model.IpPermission ipperm: sg.getIpPermissions() ) {
                if ( this.verbose) System.out.print( HDR +"FromPort="+ ipperm.getFromPort() +", Toport#="+ ipperm.getToPort() +"\t" );
                if ( ipperm.getFromPort() == null || ipperm.getToPort() == null )
                    continue;
                final String fromPortStr = inet.getNameForPortNumber( ipperm.getFromPort()  );
                final String toPortStr   = inet.getNameForPortNumber( ipperm.getToPort()    );
                if ( this.verbose) System.out.print( HDR +"FromPort="+ fromPortStr +", Toport#="+ toPortStr +"\t" );

                if ( fromPortStr != null && toPortStr != null && fromPortStr.equals( _portOfInterest ) && toPortStr.equals( _portOfInterest ) ) {
                    final LinkedHashMap<String,Object> ix = new LinkedHashMap<String,Object>();
                    // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Subnet.html
                    ix.put ( SG_ID,         sg.getGroupId() );
                    ix.put ( VPC_ID,        sg.getVpcId() );
                    ix.put ( "FromIPPort",  fromPortStr );
                    ix.put ( "ToIPPort",    toPortStr );
                    // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/Tag.html
                    showProgressbar( true, ProgressBarMileStones.STARTING, null );
                    final List<com.amazonaws.services.ec2.model.Tag>	tags = sg.getTags();
                    showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

                    for ( com.amazonaws.services.ec2.model.Tag tag: tags ) {
                        ix.put( tag.getKey(), tag.getValue() );
                    }
                    retarr.add( ix );
                } // if port matches
            } // innermost for-loop
        } // outermost for-loop

        showProgressbar( false, ProgressBarMileStones.COMPLETED, "getSGs" );
        if ( this.verbose) System.out.println(); // to finish the above print().
        return retarr;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Pass in a region-name and get back ONLY THE AZ-NAMES in the output of the cmdline as JSON (cmdline being:- aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json)
     *  @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     *  @return An array of Strings.
     *  @throws Exception thrown if any issues reading the cached YAML files (if this library is in offline-mode {@link #offline}).
     */
    public ArrayList<String>  getAZs( final String _regionStr ) throws Exception {
        if ( this.offline ) return getAZs_Offline( _regionStr );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "AZs" );
        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        final ArrayList<String> retarr = new ArrayList<String>();
        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
            // System.out.printf( "Found availability zone %s with status %s in region %s\n", zone.getZoneName(), zone.getState(), zone.getRegionName());
            if ( zone.getState().equals("available") ) retarr.add( zone.getZoneName() );
        }
        return retarr;
    }

    //==============================================================================
    public static final String KV_ZONENAME  = "ZoneName";
    public static final String KV_ZONEID    = "ZoneId";
    public static final String KV_STATE     = "State";
    public static final String KV_ZONEREGIONNAME= "RegionName";
    public static final String KV_ZONEMESSAGES  = "Messages";
    public static final String KV_PUBLICorPRIVATE  = "PublicOrPrivate";
    //==============================================================================

    /**
     * Pass in a region-name and get back the output of the cmdline as JSON (cmdline being:- aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json)
     * @param _regionStr pass in valid AWS region names like 'us-east-2', 'us-west-1', 'ap-northeast-1' ..
     * @return An array of YAML-Maps.  Its exactly === cmdline output of: aws ec2 describe-availability-zones --region us-east-2 --profile ______ --output json
     * @throws Exception any runtime Exception
     */
    public ArrayList< LinkedHashMap<String,Object> >  describeAZs( final String _regionStr ) throws Exception {
        if ( this.offline ) return describeAZs_Offline( _regionStr );

        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "AZs" );
        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

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
            oneZone.put( KV_ZONENAME,       zone.getZoneName()  );
            oneZone.put( KV_ZONEID,         zone.getZoneId()    );
            oneZone.put( KV_STATE,          zone.getState()     );
            oneZone.put( KV_ZONEREGIONNAME, zone.getRegionName() );
            final ArrayList<String> sm = new ArrayList<>();
            for( AvailabilityZoneMessage azm: zone.getMessages() ) {
                sm.add( azm.getMessage() );
            }
            oneZone.put( KV_ZONEMESSAGES, sm );
            if ( this.verbose ) System.out.println( CLASSNAME +": describeAZs(): aws ec2 describe-az command output in JSON-compatible form is ["+ oneZone.toString() +"]" );

            retarr.add( oneZone );

        }
        return retarr;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    // *  @throws Exception if any errors with the bucket-name, SDK-invocation or AWS-API invocation failures

    /**
     *  <p>Simpler version of {@link #doesBucketExist(String, String)}. Whether anyone on this planet has created a bucket with this name.</p>
     *  @param _s3bucketname NotNull String that must be _COMPLIANT with AWS-naming conventions for S3-buckets.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @return true if the Bucket exists in some region, whether or NOT you have permissions to it
     *  @see #isValidS3Bucket(String,String)
     */
    public boolean doesBucketExist( final String _s3bucketname ) {
        return this.doesBucketExist( null, _s3bucketname );
    }

    //==============================================================================

    // *  @throws Exception if any errors with the bucket-name, SDK-invocation or AWS-API invocation failures

    /**
     *  <p>Whether anyone on this planet has created a bucket with this name.</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _s3bucketname NotNull String that must be _COMPLIANT with AWS-naming conventions for S3-buckets.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @return true if the Bucket exists in some region, whether or NOT you have permissions to it
     *  @see #isValidS3Bucket(String, String)
     */
    public boolean doesBucketExist( final String _regionStr, final String _s3bucketname ) {
        final String HDR = CLASSNAME +"doesBucketExist("+ _s3bucketname +"): ";
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        try {
            return s3.doesBucketExistV2( _s3bucketname ); // <<---- Note the 'V2' suffix to the method.
            // return s3.doesBucketExist( _s3bucketname ); // AWS Documentation says, this relies on headBucket() and is therefore NOT reliable.
        } catch (AmazonServiceException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        } catch (AmazonClientException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        }
        if ( this.verbose ) System.err.println( HDR + "Failed to upload file into new object." );
        // throw new Exception( "Failed to access the S3-bucket with name ''"+ _s3bucketname +"'" );
        return false;
    }

    //==============================================================================

    /**
     *  <p>Whether __you__ have permissions to this bucket, if it exists in the 1st place.</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _s3bucketname NotNull String that must be _COMPLIANT with AWS-naming conventions for S3-buckets.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @return true if the Bucket exists in some region + whether you have any permissions allowed on it
     *  @see #doesBucketExist(String, String)
     */
    public boolean isValidS3Bucket( final String _regionStr, final String _s3bucketname ) {
        final String HDR = CLASSNAME +"isValidS3Bucket("+ _s3bucketname +"): ";
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        try {
            boolean bFoundRWAcess = false;
            final com.amazonaws.services.s3.model.AccessControlList acl = s3.getBucketAcl( _s3bucketname );
            final List<com.amazonaws.services.s3.model.Grant> grants = acl.getGrantsAsList();
            if ( this.verbose ) System.out.println( HDR + "grants has '"+ grants.size() + " entries" );
            for ( com.amazonaws.services.s3.model.Grant grant: grants ) {
                // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/Grantee.html   <-- this is an Interface
                // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/CanonicalGrantee.html  <-- implementation of Grantee interface
                final com.amazonaws.services.s3.model.Grantee grantee = grant.getGrantee();
                final String granteeId = grantee.getIdentifier();
                final String typeid = grantee.getTypeIdentifier();
                // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/Permission.html
                // This is an ENUM with valid values: FullControl, Read, Write, ReadAcp, WriteAcp
                final com.amazonaws.services.s3.model.Permission perm = grant.getPermission();
                bFoundRWAcess = bFoundRWAcess || ( perm == com.amazonaws.services.s3.model.Permission.Write ) || ( perm == com.amazonaws.services.s3.model.Permission.FullControl );
                if ( this.verbose ) System.out.println( HDR + "granteeId='"+ granteeId + " typeid='"+ typeid + " perm='"+ perm );
// S3Bucket(org-asux-aws-cfn): grants has '6 entries
// S3Bucket(org-asux-aws-cfn): granteeId='c7c930ed985964095a51c6993c895da4294700f3cc8cc8347cef99d42ffdbdeb  typeid='id perm='FULL_CONTROL
// S3Bucket(org-asux-aws-cfn): granteeId='c7c930ed985964095a51c6993c895da4294700f3cc8cc8347cef99d42ffdbdeb  typeid='id perm='FULL_CONTROL
// S3Bucket(org-asux-aws-cfn): granteeId='http://acs.amazonaws.com/groups/global/AllUsers                   typeid='uri perm='READ
// S3Bucket(org-asux-aws-cfn): granteeId='c7c930ed985964095a51c6993c895da4294700f3cc8cc8347cef99d42ffdbdeb  typeid='id perm='FULL_CONTROL
// S3Bucket(org-asux-aws-cfn): granteeId='http://acs.amazonaws.com/groups/s3/LogDelivery                    typeid='uri perm='READ
// S3Bucket(org-asux-aws-cfn): granteeId='c7c930ed985964095a51c6993c895da4294700f3cc8cc8347cef99d42ffdbdeb  typeid='id perm='FULL_CONTROL
            }
            return s3.doesBucketExistV2( _s3bucketname ); // <<---- Note the 'V2' suffix to the method.
        } catch (AmazonServiceException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        } catch (AmazonClientException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        }
        if ( this.verbose ) System.err.println( HDR + "Failed to access ACL for bucket!!!!!!!!!" );
        // throw new Exception( "Failed to access the S3-bucket with name ''"+ _s3bucketname +"'" );
        return false;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#getBucketLocation-java.lang.String-
    /**
     *  Gets the geographical region where Amazon S3 stores the specified bucket.
     *  To view the location constraint of a bucket, the user must be the bucket owner.
     *  Use Region.fromValue(String) to get the Region enumeration value, but be prepared to handle an IllegalArgumentException if the value passed is not a known Region value.
     *  Note that Region enumeration values are not returned directly from this method.
     *  @param _NOT_IMPLEMENTED_ The method is NOT YET implemented!!
     *  @return a string value that is _NOT_ like "us-east-1".  See above description for details.
     */
    public String getBucketLocation( String _NOT_IMPLEMENTED_ ) {
        return null;
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  <p>See also {@link #S3put(String, String, String, String)} whose 3rd argument is a java.lang.String (for file-path).  This method does NOT validate the file-path, unlike the polyorphic variant.</p>
     *  <p>Use this method to upload a file into the S3 path <code>s3://_S3Bucketname/_S3ObjectName</code></p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _S3Bucketname NotNull String that must be _COMPLIANT with AWS-naming conventions for S3-buckets.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @param _S3ObjectName can be Null. If Null, then will substitute with <code>_filepath.getFileName()</code>
     *  @param _filepath NotNull
     *  @return NotNull String === S3 URL to the uploaded Object.  If any errors, Exception will be thrown instead.
     *  @throws Exception if any errors with the bucket-name, object-name or access-rights
     */
    public String S3put( final String _regionStr, final String _S3Bucketname, final String _S3ObjectName, final Path _filepath ) throws Exception
    {   final String HDR = CLASSNAME +" S3put("+ _S3Bucketname +","+ _filepath.toString() +"): ";
        final String S3ObjNm = ( _S3ObjectName != null ? _S3ObjectName : _filepath.getFileName().toString() );
        final String S3URL = "s3://"+ _S3Bucketname +"/"+ S3ObjNm;
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            // throw new Exception( "AWS.SDK failed to find the HostedDomain under the name ''"+ _DNSHostedZoneName + "'" );
            return "--offline MODE of AWS.SDK PROJECT:- S3put() for "+ S3URL;
        }

        // System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials( this.AWSAuthenticationHndl ).withRegion( _regionStr==null?"us-east-2":_regionStr ).build();
        try {
            showProgressbar( false, ProgressBarMileStones.STARTING, "s3.putObject" );
            s3.putObject( _S3Bucketname, S3ObjNm, new File( _filepath.toString() ) );
            showProgressbar( false, ProgressBarMileStones.COMPLETED, "s3.putObject" );
            return S3URL;
        } catch (AmazonServiceException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        } catch (AmazonClientException e) {
            if ( this.verbose ) e.printStackTrace( System.err );
        }
        if ( this.verbose ) System.err.println( HDR + "Failed to upload file into new object." );
        throw new Exception( "Failed to upload "+ _filepath.toString() +" into S3-URL "+ S3URL );
    }

    //==============================================================================

    /**
     *  <p>The simplest method to upload a file into the S3 path <code>s3://_S3Bucketname/_S3ObjectName</code></p>
     *  <p>This method will attempt to 'validate' the file-path, as well as "correct" the _regionStr if the _S3BucketName is of the form "bucketname@eu-west-1".</p>
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _S3Bucketname NotNull String.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @param _S3ObjectName can be Null. If Null, then will use <code>_filepath.getFileName()</code>
     *  @param _filepathString NotNull
     *  @return NotNull String === S3 URL to the uploaded Object.  If any errors, Exception will be thrown instead.
     *  @throws Exception if any errors with the bucket-name, object-name or access-rights
     */
    public String S3put( final String _regionStr, final String _S3Bucketname, final String _S3ObjectName, final String _filepathString ) throws Exception
    {   final String HDR = CLASSNAME +" S3put("+ _S3Bucketname +","+ _filepathString.toString() +"): ";
        final Path path = FileSystems.getDefault().getPath( _filepathString );
        final Tuple<String,String> tuple = this.parseS3Bucketname( _S3Bucketname ); // splits "bucketname@eu-west-1" into 'bucketname' & 'eu-west-1'
        final String properBucketName = tuple.key;
        final String correctRegionID = "".equals(tuple.val) ? _regionStr : tuple.val;
        if ( path.toFile().exists() )
            return S3put( correctRegionID, properBucketName, _S3ObjectName, path );
        else
            throw new Exception( "File "+ _filepathString +" does Not exist!   FYI: Java's current-working-directory="+ System.getProperty("user.dir") );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

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

        showProgressbar( false, ProgressBarMileStones.STARTING, "DeleteEC2KeyPair" );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        final DeleteKeyPairRequest request = new DeleteKeyPairRequest().withKeyName( _MySSHKeyName );
        final DeleteKeyPairResult response = ec2.deleteKeyPair(request);
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

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
            return "--offline MODE of AWS.SDK PROJECT:-- createKeyPairEC2()";
        }

        // https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/java/example_code/ec2/src/main/java/aws/example/ec2/CreateKeyPair.java
        // http://docs.amazonaws.cn/en_us/sdk-for-java/v1/developer-guide/examples-ec2-key-pairs.html
        final AmazonEC2 ec2 = this.getAWSEC2Hndl( _regionStr );

        showProgressbar( false, ProgressBarMileStones.STARTING, "CreateEC2KeyPair" );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        final CreateKeyPairRequest request = new CreateKeyPairRequest().withKeyName( _MySSHKeyName );
        final CreateKeyPairResult response = ec2.createKeyPair(request);
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

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
        final DescribeKeyPairsRequest request = (_MySSHKeyName == null || "".equals( _MySSHKeyName.trim())  || "null".equals( _MySSHKeyName.trim()) )
                                ? new DescribeKeyPairsRequest()
                                : new DescribeKeyPairsRequest().withKeyNames( _MySSHKeyName );

        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/ec2/AmazonEC2Client.html
        try {
            showProgressbar( false, ProgressBarMileStones.STARTING, "DescribeEC2KeyPairs" );
            final DescribeKeyPairsResult response = ec2.describeKeyPairs( request );
            final List<KeyPairInfo> keys = response.getKeyPairs();
            showProgressbar( true, ProgressBarMileStones.COMPLETED, null );
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

        if ( _offline ) {
            final AWSSDK awssdk = new AWSSDK(_verbose);
            return awssdk;
        }

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

    /**
     *  Given a FQDN like 'example.com' (if that is hosted by Route53), you'll get the ZoneID (like 'Z2NF71MJ75KYXK') back (if search request is valid)
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _DNSHostedZoneName a NotNull string like 'subdomain.example.com'
     *  @param _needPublicHostedZone true === public hosted-zone, false === private hosted-zone
     *  @return a NotNull string like 'Z2NF71MJ75KYXK' (representing the HostedZoneID as you can see within Route53 domain)
     *  @throws Exception throws InvalidInputException, if input is not valid /or/ throws InvalidDomainNameException, if specified domain name is not valid.
     */
    public final String getRt53HostedZoneId( final String _regionStr, final String _DNSHostedZoneName, final boolean _needPublicHostedZone ) throws Exception {
        final String HDR = CLASSNAME +"getHostedZoneId("+ _regionStr +","+ _DNSHostedZoneName +","+ _needPublicHostedZone +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            // throw new Exception( "AWS.SDK failed to find the HostedDomain under the name ''"+ _DNSHostedZoneName + "'" );
            return "--offline MODE of AWS.SDK PROJECT:-- getHostedZoneId()";
        }

        final AmazonRoute53 Rt53 = this.getAWSRoute53Hndl( _regionStr );
        showProgressbar( false, ProgressBarMileStones.STARTING, "Rt53ListHostedZones" );
        final ListHostedZonesByNameRequest req = new ListHostedZonesByNameRequest().withDNSName( _DNSHostedZoneName +"." );
        showProgressbar( false, ProgressBarMileStones.INPROGRESS, null );
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/AmazonRoute53.html#listHostedZonesByName-com.amazonaws.services.route53.model.ListHostedZonesByNameRequest-
        final ListHostedZonesByNameResult response = Rt53.listHostedZonesByName( req ); // Retrieves a list of the public + private hosted zones
        // Attention: 'ListHostedZonesByNameResult' (ListHostedZonesByName()) sorts hosted-zones by name with the labels reversed. For example:    com.example.www.
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );

        String zoneid = response.getHostedZoneId(); // !!!!!!!!!!!!!!!!!! ATTENTION !!!!!!!!!!!!!!!!!!!!! Apparently, this always returns 'null'.   So, we need if-else below.
        if ( zoneid != null ) {
            return zoneid;
        } else {
            final List<HostedZone> zones = response.getHostedZones();
            // if ( zones.size() > 0 )
            for( HostedZone zone: zones ) {
                final HostedZoneConfig zconf = zone.getConfig();
                final boolean isAPrivateZone = zconf.getPrivateZone();  // see https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/model/HostedZoneConfig.html#getPrivateZone--
                if (this.verbose) System.out.println( HDR + "_DNSHostedZoneName " + _DNSHostedZoneName + " zone= " + zone  + " zone1.getId()= " + zone.getId() +" zone.getConfig()="+ zconf  );
                if ( _needPublicHostedZone )
                    if ( isAPrivateZone )
                        continue;
                    else
                        return zone.getId().replaceFirst( "/hostedzone/", "" );
                else // need a PRIVATE HostedZone
                    if ( isAPrivateZone )
                        return zone.getId().replaceFirst( "/hostedzone/", "" );
                    else
                        continue;
            } // for-loop
            // We should Not be here.  After all we assume that .. if you need an ID to a Zone, you are providing a EXISTING/KNOWN Hosted-Zone name.
            throw new Exception( "AWS.SDK failed to find the HostedDomain under the name ''"+ _DNSHostedZoneName + "'" );
        }
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     *  Given a region and a HostedZone in it, this will associate the Rt53 HostedZone with VPCID (for now, you'll see a dump on STDERR, as to whether it succeeded-or-NOT)
     *  @param _regionStr NotNull string for the AWSRegion (Not the AWSLocation)
     *  @param _DNSHostedZoneID a NotNull ID.  __NOT__ the DomainName (Note.. invoke this.getHostedZoneId( 'subdomain.example.com' ) to derive this argument)
     *  @param _myVPC NotNull the VPC ID.  Note: If you'd like to get _ALL_ IGWs, to attach to your _NEW_ VPC, use 
     */
    public void associateRt53HostedZoneWithVPC( final String _regionStr, final String _DNSHostedZoneID, final String _myVPC )
    {   final String HDR = CLASSNAME +"associateRt53HostedZoneWithVPC("+ _DNSHostedZoneID +","+ _myVPC +"): ";
        if ( this.offline ) {
            System.err.println( HDR +"AWS.SDK library is running in __OFFLINE__ mode.  So this method is a 'NOOP'!!!!!!!!");
            return;
        }

        final AmazonRoute53 Rt53 = this.getAWSRoute53Hndl( _regionStr );
        // warning!  There are 2 VPCs in AWS Data Model !!!
        // com.amazonaws.services.route53.model.VPC
        // com.amazonaws.services.ec2.model.Vpc
        showProgressbar( false, ProgressBarMileStones.STARTING, "Rt53AssociateWithVPC" );
        final com.amazonaws.services.route53.model.VPC vpc = new com.amazonaws.services.route53.model.VPC().withVPCId( _myVPC );
        showProgressbar( false, ProgressBarMileStones.INPROGRESS, null );
        final AssociateVPCWithHostedZoneRequest assocRequest = new AssociateVPCWithHostedZoneRequest().withHostedZoneId( _DNSHostedZoneID );
        showProgressbar( false, ProgressBarMileStones.INPROGRESS, null );
        assocRequest.setVPC( vpc );
        final AssociateVPCWithHostedZoneResult assocRes = Rt53.associateVPCWithHostedZone( assocRequest );
        showProgressbar( true, ProgressBarMileStones.COMPLETED, null );
        if ( this.verbose ) System.out.println( HDR + assocRes );
        // final ChangeInfo ci = assocRes.getChangeInfo(); // <-- A complex type that describes the changes made to your hosted zone.
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/route53/model/ChangeInfo.html
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    public static final String REGEXP_AWSID_SUFFIX = "([0-9][a-f][A-F])+";
    public static final String REGEXP_AWSREGIONNAME = "[a-zA-Z]+-[a-zA-Z]+-[0-9]";
    public static final String REGEXP_AWSREGIONNAME_WSPACES = "^\\s*("+REGEXP_AWSREGIONNAME+")\\s*$";
    public static final String REGEXP_S3BUCKETNAME_CHARS = "[0-9a-z][-0-9a-z\\.]";   // Lower-case ONLY.  start with a lowercase letter or number.
    public static final String REGEXP_S3BUCKETNAME = "^\\s*("+REGEXP_S3BUCKETNAME_CHARS+"+)\\s*$";   // Lower-case ONLY.  start with a lowercase letter or number.
    public static final String REGEXP_S3BUCKETNAME_HASREGIONID = "^\\s*("+REGEXP_S3BUCKETNAME_CHARS+"+)@("+REGEXP_AWSREGIONNAME+")\\s*$"; // Per AWS Spec, Bucket-ID can only contain Alphanumerics, Dash, Period (and other rules)

    //==============================================================================

    /**
     *  <p>ALL IDs in AWS (whether VPC, subnet, SG, EC2...) have a prefix + a hexadecimal suffix {@link #REGEXP_AWSID_SUFFIX}.  This method checks against that rule.</p>
     *  <p>This is a very useful method, to use to pre-check user-input, before invoking AWS SDK API calls</p>
     *  @param _IDStr a NotNull string representing am IOD in AWS (whether VPC, subnet, SG, EC2...)
     *  @param _musthavePrefix (for VPCs, pass in "vpc") (for Subnets, pass in "subnet") (for SGs, pass in "sg") (for EC2 instances, pass in "i") etc..
     *  @return true if it string matches the REGEXP pattern rule.
     */
    public boolean matchesAWSIDPattern( final String _IDStr, final String _musthavePrefix )
    {   final String HDR = CLASSNAME +" matchesAWSIDPattern("+ _IDStr +","+ _musthavePrefix +"): ";

        final String pattStr = "^"+ _musthavePrefix + REGEXP_AWSID_SUFFIX + "$";
        try {
            final Pattern pattern_AWSID = Pattern.compile( pattStr );
            final Matcher matcher = pattern_AWSID.matcher( _IDStr );
            if ( matcher.find() ) {
                if ( this.verbose ) System.out.println( HDR +"I found the text "+ matcher.group() +" starting at index "+  matcher.start() +" and ending at index "+ matcher.end() );
                final String ID = matcher.group(1);
                assertTrue( ID != null && ID.matches(_IDStr) );
                if ( this.verbose ) System.out.println( HDR +"Confirmed: "+ _IDStr +" is a valid AWS-ID ("+ _musthavePrefix +")" );
                return true;
            } else 
                return false;
        }catch(PatternSyntaxException e){
            if ( this.verbose ) e.printStackTrace( System.err );
            System.err.println( HDR +" Invalid pattern '"+ pattStr +"' provided, that incorporates '_musthavePrefix' " );
            System.exit(441);
            throw new RuntimeException( e.getMessage() );
            // return false;
        }
    }

    //==============================================================================

    /**
     * @param _regionStr a Nullable String
     * @return true if you know why :-)
     */

    public boolean matchesAWSRegionPattern( final String _regionStr )
    {   final String HDR = CLASSNAME +" matchesAWSRegionPattern("+ _regionStr +"): ";
        if ( _regionStr == null ) return false;

        try {
            final Pattern pattern_AWSID = Pattern.compile( REGEXP_AWSREGIONNAME_WSPACES );
            final Matcher matcher = pattern_AWSID.matcher( _regionStr );
            if ( matcher.find() ) {
                if ( this.verbose ) System.out.println( HDR +"I found the text "+ matcher.group() +" starting at index "+  matcher.start() +" and ending at index "+ matcher.end() );
                try {
                    if ( this.getLocation( _regionStr ) == null ) return false; // EXAMPLE: 'us-east-1' comes back as 'Virginia'.  Invalid _regionStr will come back as NULL.
                } catch( Exception e ) {
                    return false; // If we are here, we have SERIOUS internal failure, while reading the file: config/AWSRegionsLocations.properties
                }
                if ( this.verbose ) System.out.println( HDR +"Confirmed: "+ _regionStr +" is a valid AWS REGION" );
                return true;
            } else 
                return false;
        }catch(PatternSyntaxException e){
            if ( this.verbose ) e.printStackTrace( System.err );
            System.err.println( HDR +" Invalid pattern '"+ REGEXP_AWSREGIONNAME_WSPACES +"' in code!" );
            System.exit(441);
            throw new RuntimeException( e.getMessage() );
            // return false;
        }
    }

    //==============================================================================

    /**
     * <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html">See AWS Rules on bucket names</a>
     * @param _s3bucketname a Nullable String
     * @return true if AWS will accept the bucketname.
     */
    public boolean matchesS3BucketNamePattern( final String _s3bucketname )
    {   final String HDR = CLASSNAME +" matchesS3BucketNamePattern("+ _s3bucketname +"): ";
        if ( _s3bucketname == null ) return false;

        try {
            final Pattern pattern_AWSID = Pattern.compile( REGEXP_S3BUCKETNAME );
            final Matcher matcher = pattern_AWSID.matcher( _s3bucketname );
            if ( matcher.find() ) {
                if ( this.verbose ) System.out.println( HDR +"I found the text "+ matcher.group() +" starting at index "+  matcher.start() +" and ending at index "+ matcher.end() );
                if ( _s3bucketname.contains("--") ) return false;
                if ( this.verbose ) System.out.println( HDR +"Confirmed: "+ _s3bucketname +" is a valid AWS S3 bucketname" );
                return true;
            } else 
                return false;
        }catch(PatternSyntaxException e){
            if ( this.verbose ) e.printStackTrace( System.err );
            System.err.println( HDR +" Invalid pattern '"+ REGEXP_S3BUCKETNAME +"' in code " );
            System.exit(441);
            throw new RuntimeException( e.getMessage() );
            // return false;
        }
    }

    //==============================================================================

    /**
     *  <p>__IF__ the _S3BucketName is of the form "bucketname@eu-west-1" (Not AWS-Compliant), this method returns the pair "bucketname" + "eu-west-1".</p>
     *  <p>__IF__ the _S3BucketName is a fully-compliant AWS-bucketname, this method returns the pair "bucketname" and ""(empty-String).</p>
     *  @param _S3Bucketname NotNull String.  Will not verify whether this is a valid bucketname.  So, AWS APIs will throw Exception if invalid.
     *  @return NotNull pair-of-Strings, consisting of a NotNull-BucketName + a NotNull-RegionName (empty-string is possible, for the 2nd one)
     */
    public Tuple<String,String> parseS3Bucketname( final String _S3Bucketname )
    {   final String HDR = CLASSNAME +" parseS3Bucketname("+ _S3Bucketname +"): ";
        if ( _S3Bucketname == null )
            return new Tuple<String,String>( null, null );
        String correctRegionID = "";
        String properBucketName = _S3Bucketname;
        try {
            final Pattern pattern = Pattern.compile( REGEXP_S3BUCKETNAME_HASREGIONID );
            final Matcher matcher = pattern.matcher( _S3Bucketname );
            if ( matcher.find() ) {
                if ( this.verbose ) System.out.println( HDR +"I found the text "+ matcher.group() +" starting at index "+  matcher.start() +" and ending at index "+ matcher.end() );
                properBucketName = matcher.group(1);
                correctRegionID = matcher.group(2);
                if ( this.verbose ) System.out.println( HDR +"Confirmed: RegionID was specified as "+ correctRegionID +" for the S3-bucket ("+ properBucketName +")" );
                return new Tuple<String,String>( properBucketName, correctRegionID );
            } // inner IF
            return new Tuple<String,String>( _S3Bucketname, "" );

        } catch (PatternSyntaxException e) {
			e.printStackTrace(System.err); // too serious an internal-error.  Immediate bug-fix required.  The application/Program will exit .. in 2nd line below.
			System.err.println( HDR + ": Unexpected Internal ERROR, while checking for pattern ("+ REGEXP_S3BUCKETNAME_HASREGIONID +")." );
            System.exit(491); // This is a serious failure. Shouldn't be happening.
            throw e;
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
