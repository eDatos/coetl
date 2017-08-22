#!/bin/bash
# To overwrite cas.properties with the dev ones
if [ -f /cas-overlay/etc/cas/config/cas-dev.properties ] && [ "$DEV" = "true" ] ; then
        cat /cas-overlay/etc/cas/config/cas-dev.properties >> /cas-overlay/etc/cas/config/cas.properties
		cat /cas-overlay/etc/cas/config/cas.properties
        echo "Overwrited cas config with dev config"
fi 