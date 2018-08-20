package es.gobcan.coetl.pentaho.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serverstatus")
public class ServerStatusDTO implements PentahoResponseDTO {

    private static final long serialVersionUID = 1L;

    private String description;

    private Long freeMemory;

    private Long totalMemory;

    private Integer cpuCores;

    private Long cpuProcessTime;

    private Long upTime;

    private Long threadCount;

    private String loadAvg;

    private String osName;

    private String osVersion;

    private String osArchitecture;

    private List<TransStatusDTO> transStatusList = new ArrayList<>();

    private List<JobStatusDTO> jobStatusList = new ArrayList<>();

    @XmlElement(name = "statusdesc")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "memory_free")
    public Long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    @XmlElement(name = "memory_total")
    public Long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
    }

    @XmlElement(name = "cpu_cores")
    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    @XmlElement(name = "cpu_process_time")
    public Long getCpuProcessTime() {
        return cpuProcessTime;
    }

    public void setCpuProcessTime(Long cpuProcessTime) {
        this.cpuProcessTime = cpuProcessTime;
    }

    @XmlElement(name = "uptime")
    public Long getUpTime() {
        return upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    @XmlElement(name = "thread_count")
    public Long getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Long threadCount) {
        this.threadCount = threadCount;
    }

    @XmlElement(name = "load_avg")
    public String getLoadAvg() {
        return loadAvg;
    }

    public void setLoadAvg(String loadAvg) {
        this.loadAvg = loadAvg;
    }

    @XmlElement(name = "os_name")
    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @XmlElement(name = "os_version")
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @XmlElement(name = "os_arch")
    public String getOsArchitecture() {
        return osArchitecture;
    }

    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = osArchitecture;
    }

    @XmlElementWrapper(name = "transstatuslist")
    @XmlElement(name = "transstatus")
    public List<TransStatusDTO> getTransStatusList() {
        return transStatusList;
    }

    public void setTransStatusList(List<TransStatusDTO> transStatusList) {
        this.transStatusList = transStatusList;
    }

    @XmlElementWrapper(name = "jobstatuslist")
    @XmlElement(name = "jobstatus")
    public List<JobStatusDTO> getJobStatusList() {
        return jobStatusList;
    }

    public void setJobStatusList(List<JobStatusDTO> jobStatusList) {
        this.jobStatusList = jobStatusList;
    }

    public List<EtlStatusDTO> getStatusList() {
        return Stream.of(transStatusList, jobStatusList).flatMap(statusList -> statusList.stream()).collect(Collectors.toList());
    }

    public boolean isOnline() {
        return "Online".equals(description);
    }
}
