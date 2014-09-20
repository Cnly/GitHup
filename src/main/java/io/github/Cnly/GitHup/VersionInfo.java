package io.github.Cnly.GitHup;

public class VersionInfo
{
    
    private final String version;
    private final String releaseDate;
    private final String description;
    
    public VersionInfo(String version, String releaseDate)
    {
        
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = null;
        
    }
    
    public VersionInfo(String version, String releaseDate, String description)
    {
        
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
        
    }

    @Override
    public String toString()
    {
        return "VersionInfo [version=" + version + ", releaseDate="
                + releaseDate + ", description=" + description + "]";
    }
    
    /**
     * Calculates the hash code.
     * The field <code>description</code> is NOT involved.
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }
    
    /**
     * Check if two <code>VersionInfo</code>s are equal. The field
     * <code>description</code> is NOT compared.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VersionInfo other = (VersionInfo) obj;
        if (version == null)
        {
            if (other.version != null) return false;
        }
        else if (!version.equals(other.version)) return false;
        if (releaseDate == null)
        {
            if (other.releaseDate != null) return false;
        }
        else if (!releaseDate.equals(other.releaseDate)) return false;
        return true;
    }

    public String getVersion()
    {
        return version;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public String getDescription()
    {
        return description;
    }
    
}
