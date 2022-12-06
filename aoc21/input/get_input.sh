#!/bin/bash
YEAR=2021

function help {
    echo "How to use: './get_input 3' or './get_input 1..25 2016' default year is $YEAR"
}

if [[ $# -eq 0 ]]; then
    echo "Error: you need to pass at least an argument for the day"
    help
    exit 1
elif [[ $# -eq 2 ]]; then
    YEAR=$2
elif [[ $# -gt 2 ]]; then
    echo "Error: $# is too many arguments"
    help
    exit 1
# Check for existing
elif [[ ! -f ".session" ]]; then
    echo "No session file found. Add your session token into a file named 'session' and place it next to this script"
    exit 1
# Check session token format against RegEx
elif [[ ! $(cat .session) =~ ^"session="[0-9a-f]+$ ]]; then
    echo "Your session token seems wrong. It has to start with 'session='"
    exit 1
fi

INPUT=$1

function download {
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

    CONTENT=$(cat $FILEPATH)
    L=$(cat $FILEPATH | wc -l)

    if [[ $CONTENT == *"Please log in"* ]]; then
        echo "ERROR: Your session cookie is invalid."
        echo "Make sure
    * you have a valid cookie in the format 'session=[0-9a-f]{128}' (without apostrophes)
    * in a file named '.session'
    * next to this script"
        echo "(and don't forget to adust your .gitignore to ignore the cookie)"
        rm $FILEPATH
        exit 1
    elif [[ $CONTENT == "Please don't repeatedly request this endpoint"* || $CONTENT == "404 Not Found" ]]; then
        echo "ERROR: $(cat $FILEPATH)"
        rm $FILEPATH
        exit 1
    elif [[ $L -eq 1 ]]; then
        echo "Strange, just one line of input. Is that correct?"
        echo $CONTENT
    fi

    echo "wrote input to $FILEPATH with $L lines"
    cat $FILEPATH
}

# download a range of inputs
if [[ $INPUT == *".."* ]]; then
    RANGE=(${INPUT//../ })
    START=${RANGE[0]}
    END=${RANGE[1]}
    if [[ $START -gt $END ]]; then
        echo "Invalid range $START to $END"
        exit 1
    else
    echo "NICE! downlaoding for $START to $END"
        i=$START
        j=$END
        while [ $i -le $j ] 
        do
            download $i
            i=$((i + 1));
        done
        exit 0
    fi
fi

download $INPUT