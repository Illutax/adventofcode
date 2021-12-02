#!/bin/bash
YEAR=2021

# prefix $D with 0
if [[ $1 -lt 10 ]]; then D=0$1; else D=$1; fi

FILEPATH=day$D.txt

# check file
if [[ -f $FILEPATH ]]; then
    echo "$FILEPATH exists."
    exit 1
fi

echo "getting input for day $1..."
curl -b $(cat .session) https://adventofcode.com/$YEAR/day/$1/input --silent > $FILEPATH

L=$(cat $FILEPATH | wc -l)
if [[ $L -eq 1 ]]; then
    echo "ERROR:"
    echo $(cat $FILEPATH)
    rm $FILEPATH
    exit 1
fi

echo "wrote input to $FILEPATH with $L lines"