package org.eclipse.ceylon.common.tools.config;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonConfigToolMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonConfigToolMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}