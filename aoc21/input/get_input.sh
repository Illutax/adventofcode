#!/bin/bash
YEAR=2021

# prefix $D with 0
if [[ $1 -lt 10 ]]; then D=0$1; else D=$1; fi

FILEPATH=day$D.txt


echo getting input for day $1...
#curl -b $(cat .session) https://adventofcode.com/$YEAR/day/$1/input --silent > $FILEPATH
echo "wrote input to $FILEPATH with $(cat $FILEPATH | wc -l) lines"