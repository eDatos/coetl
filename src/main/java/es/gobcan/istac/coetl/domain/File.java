package es.gobcan.istac.coetl.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tb_files")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum SupportedFormats {
	        CSV, TXT, XML, JSON, XLSX, XLS
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", initialValue = 10)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private Blob content;

    @NotNull
    @Column(name = "format", nullable = false)
    private String format;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;
    
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String formato) {
        this.format = formato;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String filename) {
        this.name = filename;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

}
