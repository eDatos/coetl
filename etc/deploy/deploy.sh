#!/bin/sh

# FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

HOME_PATH=arte-application-template
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/arte-application-template/tomcats/arte-application-template01/webapps
DEMO_ENV=$HOME_PATH/env

scp -r etc/deploy deploy@arte-application-template.arte:$TRANSFER_PATH
scp target/arte-*.war deploy@arte-application-template.arte:$TRANSFER_PATH/arte-application-template.war
ssh deploy@arte-application-template.arte <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    
    sudo service arte-application-template01 stop
    
    ###
    # ARTE-APPLICATION-TEMPLATE
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/arte-application-template
    sudo mv $TRANSFER_PATH/arte-application-template.war $DEPLOY_TARGET_PATH/arte-application-template.war
    sudo unzip $DEPLOY_TARGET_PATH/arte-application-template.war -d $DEPLOY_TARGET_PATH/arte-application-template
    sudo rm -rf $DEPLOY_TARGET_PATH/arte-application-template.war

    # Restore Configuration
    sudo cp $DEMO_ENV/logback.xml $DEPLOY_TARGET_PATH/arte-application-template/WEB-INF/classes/
    sudo rm -f $DEPLOY_TARGET_PATH/arte-application-template/WEB-INF/classes/config/application-env.yml
    sudo cp $DEMO_ENV/data-location.properties $DEPLOY_TARGET_PATH/arte-application-template/WEB-INF/classes/config/
    
    sudo chown -R arte-application-template.arte-application-template /servers/arte-application-template
    sudo service arte-application-template01 start
    

    echo "Finished deploy"

EOF