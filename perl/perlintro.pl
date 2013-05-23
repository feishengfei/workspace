#!/usr/bin/env perl

print "hello world!\r\n";
print 'hello world!\r\n';

print "hello 
world!\r\n";

print 42;
print("helloworld\r\n");


my $animal = "camel";
my $answer = 42;

print $animal, "\r\n";
print "The animal is $animal\r\n";
print "The square of $answer is ", $answer * $answer, "\n";

print;          # prints contents of $_ by default

my @animals = ("camel", "llama", "owl");
my @numbers = (23, 42, 69);
my @mixed   = ("camel", 42, 1.23);


print $animals[0], "\r\n";
print $animals[1], "\r\n";

print $mixed[$#mixed], "\r\n";       # last element, prints 1.23
print @mixed, "\r\n";

my @ENV_KEY = keys(%ENV);
my @ENV_VALUES = values(%ENV);

foreach (@ENV_KEY) {
	print "KEY   = ",$_, "\r\n";
	print "VALUE = ", $ENV{$_}, "\r\n\r\n";
}

print "PATH=", $ENV{"PATH"}, "\r\n";

my $variables = {
	scalar  =>  {
		description => "single item",
		sigil => '$',
	},
	array   =>  {
		description => "ordered list of items",
		sigil => '@',
	},
	hash    =>  {
		description => "key/value pairs",
		sigil => '%',
	},
};

print "scalar :  $variables->{'scalar'}->{''}\n";
print "scalar begin with a $variables->{'scalar'}->{'sigil'}\n";
print "array :  $variables->{'array'}->{''}\n";
print "array begin with a $variables->{'array'}->{'sigil'}\n";
print "hash :  $variables->{'hash'}->{''}\n";
print "hash begin with a $variables->{'hash'}->{'sigil'}\n";

my $x = "foo";
my $some_condition = 1;
if ($some_condition) {
	my $y = "bar";
	print $x, "\r\n";           # prints "foo"
	print $y, "\r\n";           # prints "bar"
}
print $x, "\r\n";               # prints "foo"

#print "LALALA\r\n" while 1;


open(my $in,  "<",  "input.txt")  or die "Can't open input.txt: $!";
open(my $out, ">",  "output.txt") or die "Can't open output.txt: $!";
open(my $log, ">>", "my.log")     or die "Can't open my.log: $!";

while (<$in>) {     # assigns each line in turn to $_
	print "Just read in this line: $_";
}

print STDERR "This is your final warning.\n";

foreach (@ENV_KEY) {
	print $out "KEY   = ",$_, "\r\n";
	print $out "VALUE = ", $ENV{$_}, "\r\n\r\n";
	print $log $_, "=", $ENV{$_}, "\r\n\r\n";
}
