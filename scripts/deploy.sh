#!/bin/bash

REPOSITORY=/home/ubuntu/test
PROJECT_NAME=WhereUP

echo "> Build 파일"

cp $REPOSITORY/deploy/*.jar $REPOSITORY/

CURRENT_PID=$(pgrep -fl WhereUP | grep java | awk '{print $1}')

echo "현재 구동 중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    sudo kill -15 $CURRENT_PID
    sleep 10

    CURRENT_PID=$(pgrep -fl WhereUP | grep java | awk '{print $1}')
    echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

    if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
    else
        echo "> kill -15 $CURRENT_PID"
        sudo kill -15 $CURRENT_PID
        sleep 10
    fi
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

sudo nohup java -jar \
    -Dspring.config.location=classpath:/application.yml \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

aws s3 cp "$JAR_NAME" "s3://whereupp-s3/backup/$(date "+%Y%m%d_%H%M%S")_$(basename "$JAR_NAME")"