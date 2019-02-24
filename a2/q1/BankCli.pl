# Created by:	Colin Dyson
# Student #:	7683407
use IO::Handle;
use strict;
use warnings;
use Socket;

my $sock;
my $host = "cormorant.cs.umanitoba.ca";
my $port = 13059;

my $iaddr = inet_aton($host) || die "no host: $host";
my $paddr = sockaddr_in($port, $iaddr);
my $proto = getprotobyname("tcp");

my $exit = 0;
my $in;
my $response;

print "Client Starting\n";

while ($exit != 1) {
	print ">";
	$in = <STDIN>;
	chop $in;

	if ($in ne "E") {
		socket($sock, PF_INET, SOCK_STREAM, $proto) || die  "Failed to create socket";
		$sock->autoflush;
		connect($sock, $paddr) || die "Failed to connect to server";
		print $sock "$in\n" || die "Communication with the server has failed.";
		$response = <$sock>;
		print "[Server]: $response\n";
		close $sock || die "Failed to close socket";
	} else {
		$exit = 1;
	}
}

print "Client Finished.\n";
