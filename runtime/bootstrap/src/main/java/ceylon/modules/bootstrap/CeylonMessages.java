package ceylon.modules.bootstrap;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("ceylon.modules.bootstrap.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
}
