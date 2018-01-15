#!/bin/sh

HOME_PATH=secretaria-libro
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/secretaria/tomcats/secretaria01/webapps
DEMO_ENV=$HOME_PATH/env

scp -r etc/deploy deploy@secretaria.arte:$TRANSFER_PATH
scp target/secretaria-*.war deploy@secretaria.arte:$TRANSFER_PATH/secretaria-libro.war
ssh deploy@secretaria.arte <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    
    sudo service secretaria01 stop
    
    ###
    # SECRETARIA
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/secretaria-libro
    sudo mv $TRANSFER_PATH/secretaria-libro.war $DEPLOY_TARGET_PATH/secretaria-libro.war
    sudo unzip $DEPLOY_TARGET_PATH/secretaria-libro.war -d $DEPLOY_TARGET_PATH/secretaria-libro
    sudo rm -rf $DEPLOY_TARGET_PATH/secretaria-libro.war

    # Restore Configuration
    sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/secretaria-libro/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/secretaria-libro/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/secretaria-libro/WEB-INF/classes/config/
    
    sudo chown -R secretaria.secretaria /servers/secretaria
    sudo service secretaria01 start
    

    echo "Finished deploy"

EOF