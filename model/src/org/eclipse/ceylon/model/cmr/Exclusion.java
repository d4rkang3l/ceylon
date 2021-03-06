package org.eclipse.ceylon.model.cmr;

import java.util.Objects;

public class Exclusion {
    private String artifactId;
    private String groupId;

    public Exclusion(String groupId, String artifactId){
        this.groupId = groupId;
        this.artifactId = artifactId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public String getGroupId() {
        return groupId;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj instanceof Exclusion == false)
            return false;
        Exclusion b=(Exclusion) obj;
        return Objects.equals(groupId, b.groupId)
                && Objects.equals(artifactId, b.artifactId);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Exclusion["+groupId+":"+artifactId+"]";
    }
}
