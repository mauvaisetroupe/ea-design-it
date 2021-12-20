package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;

public class SourceTarget {

    public SourceTarget(Application source, Application target) {
        this.source = source;
        this.target = target;
    }

    public Application getSource() {
        return source;
    }

    public void setSource(Application source) {
        this.source = source;
    }

    public Application getTarget() {
        return target;
    }

    public void setTarget(Application target) {
        this.target = target;
    }

    private Application source;
    private Application target;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.getName().hashCode());
        result = prime * result + ((target == null) ? 0 : target.getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SourceTarget other = (SourceTarget) obj;
        if (source == null) {
            if (other.source != null) return false;
        } else if (!source.getName().equals(other.source.getName())) return false;
        if (target == null) {
            if (other.target != null) return false;
        } else if (!target.getName().equals(other.target.getName())) return false;
        return true;
    }
}
