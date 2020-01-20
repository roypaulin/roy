all: c

check:	a b
	 multiply check a b
c:	c-1-1
	fuse c 1 1 c-1-1

#
c-1-1:	p-1-1-1-1
	sum c-1-1 p-1-1-1-1

#
p-1-1-1-1:	a-1-1 b-1-1
	multiply p-1-1-1-1 a-1-1 b-1-1

#
a-1-1:	a
	split a-1-1 a 1 1 1 1

#
b-1-1:	b
	split b-1-1 b 1 1 1 1
clean:
	rm -f a-1-1 b-1-1 p-1-1-1-1 c-1-1 c check
