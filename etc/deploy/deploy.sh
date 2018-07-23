#!/bin/sh

# FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, plantilla, etc...)

HOME_PATH=plantilla
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/plantilla/tomcats/plantilla01/webapps
DEMO_ENV=$HOME_PATH/env

scp -r etc/deploy deploy@tacoronte.arte:$TRANSFER_PATH
scp target/coetl-*.war deploy@tacoronte.arte:$TRANSFER_PATH/plantilla.war
ssh deploy@tacoronte.arte <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    
    sudo service plantilla01 stop
    
    ###
    # ARTE-APPLICATION-TEMPLATE
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/plantilla
    sudo mv $TRANSFER_PATH/plantilla.war $DEPLOY_TARGET_PATH/plantilla.war
    sudo unzip $DEPLOY_TARGET_PATH/plantilla.war -d $DEPLOY_TARGET_PATH/plantilla
    sudo rm -rf $DEPLOY_TARGET_PATH/plantilla.war

    # Restore Configuration
    sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/plantilla/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/plantilla/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/plantilla/WEB-INF/classes/config/
    
    sudo chown -R plantilla.plantilla /servers/plantilla
    sudo service plantilla01 start
    

    echo "Finished deploy"

EOF