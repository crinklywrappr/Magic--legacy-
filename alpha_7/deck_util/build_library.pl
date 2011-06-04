#!/usr/bin/perl -w

use Cwd;

my $dir = cwd();
my $line = "";
my $in_file = $ARGV[0] || die "build_library.pl <infile> <outfile> $!";
my $out_file = $ARGV[1] || die "build_library.pl <infile> <outfile> $!";
my $out = "";
my @components;

while(defined($line = <>)) {
	@components = split("\t", $line);
	chomp($components[1]);
	$out = $out.$components[0]."\t".$components[1]."\t".$dir."\/".$components[1]."\.jpg\n";
	print $components[1]."\n";
	system "gatherer_search.pl", $components[1];
	sleep(3);
}

open(DECK, ">".$out_file);
print DECK $out;
close DECK;
