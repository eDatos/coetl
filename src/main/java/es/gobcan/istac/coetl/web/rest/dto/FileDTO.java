package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class FileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String format;

    private String name;

    private Timestamp creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileDTO localFileDTO = (FileDTO) o;

        return Objects.equals(id, localFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileDTO (id = " + getId() + ", name = " + getName() + ")";
    }
}
