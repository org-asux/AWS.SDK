#!/usr/bin/env node



//decison made: Each subfolder or org.ASUX (like this one org.ASUX.cmdline) will be a standalone project ..
// .. as in: this asux.js is EXPECTING to see cmdline-arguments **as if** it were entered by user on shell-prompt



//--------------------------
var fs = require("fs");     // https://nodejs.org/api/fs.html#fs_fs_accesssync_path_mode 

if ( ! process.env.ORGASUXHOME ) {
	console.error("ERROR: You must define the ENVIRONMENT variable 'ORGASUXHOME' accurately.  A simple way is to run asux.js from the __ROOT__ (org.ASUX) project of the org.ASUX hierarchy-of-projects at GitHub.com." );
	process.exit(99);
}
// file-included - Not a 'require'
eval( fs.readFileSync( process.env.ORGASUXHOME +'/bin/asux-common.js' ) + '' );

//==========================================================
var CMDGRP="aws.sdk"; // this entire file is about this CMDGRP.   !!! This value is needed within processAWSSDKCmd()/processJavaCmd() - that function is defined within ${ORGASUXFLDR}/bin/asux-common.js
var COMMAND = "unknown"; // will be set based on what the user enters on the commandline.

//==========================================================
/* attach options to a command */
/* if a command does NOT define an action (see .action invocation), then the options are NOT validated */
/* For Git-like submodule commands.. ..
 *	When .command() is invoked with a description argument, no .action(callback) should be called to handle sub-commands.
 *	Otherwise there will be an error.
 *	By avoiding .action(), you tell commander that you're going to use separate executables for sub-commands, much like git(1) and other popular tools.
 *	The commander will try to search the executables in the directory of the entry script (if this file is TopCmd.js) for names like:- TopCmd-install.js TopCmd-search.js
 *	Specifying true for opts.noHelp (see noHelp)  will remove the subcommand from the generated help output.
*/

CmdLine
	.version('1.0', '-v, --version')
	.usage('[options] <commands ...>')
	.option('--verbose', 'A value that can be increased by repeating', 0)
	.option('--offline', 'A value that can be increased by repeating', 0)
// .command('sdk ...', 'use the ASUX.org interface to AWS CLI (really no good reason to)', { isDefault: false, noHelp: false } )
// .command('cfn ...', 'create new cloudformation templates', { isDefault: false, noHelp: false } )
	;

//==========================
// Custom HELP output .. must be before .parse() since node's emit() is immediate

CmdLine.on('--help', function(){
	console.log('')
	console.log('Examples:');
	console.log('  $ %s --help', __filename);
	console.log('  $ %s --verbose aws sdk list-regions', __filename);
	console.log('  $ %s --offline aws sdk get-vpc-id ap-northest-1 .. ..', __filename);
});

//==========================
/* execute custom actions by listening to command and option events.
 */

CmdLine.on('option:verbose', function () {
	console.log("Yeah.  Going verbose" + this.verbose);
	process.env.VERBOSE = this.verbose;
});
CmdLine.on('option:offline', function () {
	if (process.env.VERBOSE) console.log("Yeah.  Going _OFFLINE_ " );
	process.env.OFFLINE = true;
});

CmdLine.on('command:get-vpc-id', function () {
	COMMAND="get-vpc-id";
	processAWSSDKCmd( COMMAND );
});
CmdLine.on('command:describe-vpcs', function () {
	COMMAND="describe-vpcs";
	processAWSSDKCmd( COMMAND );
});

CmdLine.on('command:list-regions', function () {
	COMMAND="list-regions";
	processAWSSDKCmd( COMMAND );
});
CmdLine.on('command:list-AZs', function () {
	COMMAND="list-AZs";
	processAWSSDKCmd( COMMAND );
});
CmdLine.on('command:describe-AZs', function () {
	COMMAND="describe-AZs";
	processAWSSDKCmd( COMMAND );
});

CmdLine.on('command:create-key-pair', function () {
	COMMAND="create-key-pair";
	processAWSSDKCmd( COMMAND );
});
CmdLine.on('command:delete-key-pair', function () {
	COMMAND="delete-key-pair";
	processAWSSDKCmd( COMMAND );
});
CmdLine.on('command:describe-key-pairs', function () {
	COMMAND="describe-key-pairs";
	processAWSSDKCmd( COMMAND );
});

// Like the 'default' in a switch statement.. .. After all of the above "on" callbacks **FAIL** to trigger, we'll end up here.
// If we end up here, then .. Show error about unknown command
CmdLine.on('command:*', function () {
	console.error( __filename +':\nInvalid command: %s\nSee --help for a list of available commands.', CmdLine.args.join(' '));
	console.error( 'FULL command-line: ', process.argv.join(' ') );
	process.exit(21);
});

//==========================
CmdLine.parse(process.argv);

//============================================================
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//============================================================

function processAWSSDKCmd( _CMD) {

	if (process.env.VERBOSE) console.log( "Environment variables (As-Is): AWSHOME=" + process.env.AWSHOME +", AWSCFNHOME=" + process.env.AWSCFNHOME +"\n" );

	// Following 15 lines are Common with {ORGASUXFLDR}/AWS/CFN/asux.js
	// whether or not process.env.AWSHOME is already set already.. reset it based on the location of this file (./asux.js)
	  // if ( !  process.env.AWSHOME ) {
		var parentDir = ""+__dirname;
		parentDirArr = parentDir.split(PATH.sep);
		parentDirArr.pop();
		if (process.env.VERBOSE) console.log( "REGULAR variable: parentDirArr='" + parentDirArr.join('/') +"'." );
		const afolder = ""+parentDirArr.join('/'); // for use by all scripts under process.env.ORGASUXHOME/AWS/CFN .. so it know where this asux.js is.
		if ( (afolder != process.env.AWSHOME) && EXECUTESHELLCMD.checkIfExists( process.env.AWSHOME ) ) {
			console.error( __filename +"\nThe parent-folder "+ afolder + " that contains this asux.js script conflicts with the Environment-variable AWSHOME="+ process.env.AWSHOME +".  Please unset the environment variable AWSHOME or remove the folder "+ afolder );
			process.exitCode = 9;
			return;
		}
		process.env.AWSHOME = afolder;
	  // } // if

	// Unlike the above 15 lines, the following is copied from {ORGASUXFLDR}/cmdline/asux.js
    // whether or not process.env.AWSCFNHOME is already set already.. reset it based on the location of this file (./asux.js)
    const afolder2=process.env.AWSHOME +"/CFN";
    if ( EXECUTESHELLCMD.checkIfExists( afolder2 ) ) {
        if ( (afolder2 != process.env.AWSCFNHOME) && EXECUTESHELLCMD.checkIfExists( process.env.AWSCFNHOME ) ) {
			console.error( __filename +"\nThe default folder "+ afolder2 + " that contains this asux.js script conflicts with the Environment-variable AWSCFNHOME="+ process.env.AWSCFNHOME +".  Please unset the environment variable AWSCFNHOME or remove the folder "+ afolder2 );
			process.exitCode = 9;
			return;
        } else {
          // Ok.  Environment variable process.env.AWSCFNHOME is invalid/not-set.  I'm ok either way.
			process.env.AWSCFNHOME = afolder2;
        }
    } else {
        // hmmm... someone is fucking around with the folder-structure that 'install' created.
        // The default folder (represented by 'afolder2') is missing!!!
        // perhaps they know how to (figuratively speaking) know how to pop-up open the hood and customize the car thoroughly!
        if ( checkIfExists( process.env.AWSCFNHOME ) ) {
            // all good;  Proceed further.
            // !!!!!! Attention.  Actually, it turns out.. the rest of the asux.js code will NOW _INITIATE_ a git-pull for this project. LOL!
            // That means the user's attempt to "move" AWSCFNHOME will be completely INEFFECTIVE ;-)  Ha!
        } else {
            console.error( __filename +"\nThe default folder "+ afolder2 + " does Not exist.  EITHER set the environment variable 'AWSCFNHOME' .. or, re-install the entire ASUX.org project from scratch." );
            process.exitCode = 9;
            return;
        }
    }

	if (process.env.VERBOSE) console.log( "Environment variables (final): AWSHOME=" + process.env.AWSHOME +", AWSCFNHOME=" + process.env.AWSCFNHOME +"\n" );

	processJavaCmd( _CMD );

} // end function processCFNCmd

//============================================================
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//============================================================

// The Node.js process will exit on its own if there is no additional work pending in the event loop.
// The process.exitCode property can be set to tell the process which exit code to use when the process exits gracefully.
process.exitCode = 0;

//EoScript