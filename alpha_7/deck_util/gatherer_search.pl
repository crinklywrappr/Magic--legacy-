#!/usr/bin/perl -w

use strict;
use warnings;
use LWP::Simple;

my $term = $ARGV[0];
$term =~ s/ /%20/g; #place %20 where space occurs
$term =~ s/(.*)/\L$1/g; #change all to lowercase
chomp($term); #get rid of newline
my $url = 'http://ww2.wizards.com/gatherer/index.aspx?term='.$term.'&Field_Name=on&setfilter=All%20sets';
my $response = get($url);

$term =~ s/%20/ /g;

my $num_cards = "";
my $detail_codes = "";
my $names = "";

while( $response =~ /([0-9])+ Card[s]* Found/gi ) {
	$num_cards = $1."\n";
}

if($num_cards eq "") {
	print "not found\n";
	die $!;
}

my $curr = "";
my $last = "";

while($response =~ /onclick="javascript:openDetailsWindow\(([0-9]+)\)\;"/gi) {
	$last = $curr;
	$curr = $1;
	if( $curr ne $last ) {
		$detail_codes = $detail_codes.$1."\n";
	}
}

while($response =~ /<b>(.*$term.*)<\/b>/gi) {
	$names = $names.$1."\n";
}

my @detail_codes_array = split("\n", $detail_codes);
my @names_array = split("\n", $names);

my $correct_index = 0;

if($num_cards>1) {
	my $appropriate = 0;
	while($appropriate==0) {
		print "We found multiple results for ".$term." - select which you want.\n";
		my $i = 0;
		for($i=0;$i<$num_cards;$i++) {
			print $i.".  ".$names_array[$i]."\n";
		}
		$correct_index = <STDIN>;
		chomp($correct_index);
		if($correct_index =~ /[0-9]+/ && $correct_index<$num_cards) {
			$appropriate = 1;
		}
	}
}

$url = 'http://ww2.wizards.com/gatherer/CardDetails.aspx?&id='.$detail_codes_array[$correct_index];
$response = get($url);

my $card_loc = "";

while($response =~ /src="(http:\/\/.*\/Card[0-9]+\.jpg)"/gi) {
	$card_loc = $1."\n";
}

my $pic = "";
$pic = get($card_loc);

open(IMAGE, ">".$names_array[$correct_index].".jpg") || die $names_array[$correct_index].".jpg: $!";
print IMAGE $pic;
close IMAGE;
