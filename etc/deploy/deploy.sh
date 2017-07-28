#!/bin/sh

HOME_PATH=secretaria-libro
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/secretaria/tomcats/secretaria01/webapps
DEMO_ENV=$HOME_PATH/env

scp -r etc/deploy deploy@secretaria.arte:$TRANSFER_PATH
scp target/secretaria-*.war deploy@secretaria.arte:$TRANSFER_PATH/secretaria.war
ssh deploy@secretaria.arte <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh
    
    sudo service secretaria01 stop
    
    ###
    # SECRETARIA
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/ROOT
    sudo mv $TRANSFER_PATH/secretaria.war $DEPLOY_TARGET_PATH/secretaria.war
    sudo unzip $DEPLOY_TARGET_PATH/secretaria.war -d $DEPLOY_TARGET_PATH/ROOT
    sudo rm -rf $DEPLOY_TARGET_PATH/secretaria.war

    # Restore Configuration
    sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/config/
    
    sudo chown -R secretaria.secretaria /servers/secretaria
    sudo service secretaria01 start
    

    echo "Finished deploy"

EOF