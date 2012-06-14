package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Method
public final class className {
    
    private className() {}
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language.Object")
    final java.lang.Object object) {
    	//TODO: type args?
        java.lang.String name = object.getClass().getName();
        int i = name.indexOf("$");
        if (i>0) {
            name = name.substring(0, i) + 
                    name.substring(i+1);
        }
        return name;
    }
}
