#!/bin/bash
info() {
    echo "[ INFO  ] `date +'%Y/%m/%d %H:%M:%S'` -> $*"
}
error() {
    echo "[ ERROR ] `date +'%Y/%m/%d %H:%M:%S'` -> $*"
}

MAXR=100
SLEEP=3

checkURL() {
    URL=$1
    NAME=$2
    RESPONSE=`curl -sL -w "%{http_code}" "$URL" -o /dev/null`
    I=0
    while [ $RESPONSE -ne 200 ]; do
        info "Waiting response from $NAME ($RESPONSE) $I/$MAXR ..."
        sleep $SLEEP
        I=$(( I + 1 ))
        if [  $I -gt $MAXR ]; then
            error "Timeout reached, $NAME not started properly ..."
            break;
        fi
        RESPONSE=`curl -sL -w "%{http_code}" "$URL" -o /dev/null`
    done
}

checkPROC() {
     USER_SERVICE=$1

     PID=`ps -fu $USER_SERVICE | tail -n +2 | awk '{printf $2" "}'`
     I=0
     while [ -n "$PID" ]; do
         info "Stopping PID ($PID) $I/$MAXR..."
         sleep $SLEEP
         I=$(( I + 1 ))
         if [  $I -gt $MAXR ]; then
             error "Timeout reached, killing pid $PID  ..."
             error "kill -9 $PID"
             break;
         fi
         PID=`ps -fu $USER_SERVICE | tail -n +2 | awk '{printf $2" "}'`
     done
}
