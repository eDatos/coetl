package es.gobcan.coetl.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

public class FileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String dataContentType;

    private String name;

    private Long length;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
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
