#!/bin/sh

HOME_PATH=coetl
TRANSFER_PATH=$HOME_PATH/tmp
DEMO_ENV=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/metamac/tomcats/metamac01/webapps
RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -r etc/deploy deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH
scp coetl/target/coetl-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/coetl.war
ssh deploy@estadisticas.arte-consultores.com <<EOF

    # chmod a+x $TRANSFER_PATH/deploy/*.sh;
    
    if [ $RESTART -eq 1 ]; then
        sudo service metamac01 stop
    fi
    
    ###
    # COETL
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/coetl
    sudo mv $TRANSFER_PATH/coetl.war $DEPLOY_TARGET_PATH/coetl.war
    sudo unzip $DEPLOY_TARGET_PATH/coetl.war -d $DEPLOY_TARGET_PATH/coetl
    sudo rm -rf $DEPLOY_TARGET_PATH/coetl.war

    # Restore Configuration
    #sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/coetl/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/coetl/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/coetl/WEB-INF/classes/config/
    
    if [ $RESTART -eq 1 ]; then
        sudo chown -R metamac.metamac /servers/metamac
        sudo service metamac01 start
    fi

    echo "Finished deploy"

EOF