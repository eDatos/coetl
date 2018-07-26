#!/bin/sh

HOME_PATH=coetl
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/coetl/tomcats/coetl01/webapps
DEMO_ENV=$HOME_PATH/env

scp -r etc/deploy deploy@coetl:$TRANSFER_PATH
scp target/coetl-*.war deploy@coetl.arte:$TRANSFER_PATH/coetl.war
ssh deploy@coetl.arte <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    
    sudo service coetl01 stop
    
    ###
    # COETL
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/ROOT
    sudo mv $TRANSFER_PATH/coetl.war $DEPLOY_TARGET_PATH/coetl.war
    sudo unzip $DEPLOY_TARGET_PATH/coetl.war -d $DEPLOY_TARGET_PATH/ROOT
    sudo rm -rf $DEPLOY_TARGET_PATH/coetl.war

    # Restore Configuration
    sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/ROOT/WEB-INF/classes/config/
    
    sudo chown -R coetl.coetl /servers/coetl
    sudo service coetl01 start
    

    echo "Finished deploy"

EOF