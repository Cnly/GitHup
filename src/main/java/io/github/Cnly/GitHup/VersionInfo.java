package io.github.Cnly.GitHup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class VersionInfo
{
    
    private final String version;
    private final String releaseDate;
    private final String description;
    private final String downloadLink;
    
    public VersionInfo(String version, String releaseDate)
    {
        this(version, releaseDate, null);
    }
    
    public VersionInfo(String version, String releaseDate, String description)
    {
        this(version, releaseDate, description, null);
    }
    
    public VersionInfo(String version, String releaseDate, String description, String downloadLink)
    {
        
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
        this.downloadLink = downloadLink;
        
    }

    @Override
    public String toString()
    {
        return "VersionInfo [version=" + version + ", releaseDate="
                + releaseDate + ", description=" + description
                + ", downloadLink=" + downloadLink + "]";
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
    
    /**
     * Checks if the <code>downloadLink</code> of this <code>VersionInfo</code> is set.
     * @return <code>true</code> if <code>downloadLink != null</code>
     */
    public boolean hasDownloadLink()
    {
        return downloadLink != null;
    }
    
    /**
     * Gets the <code>downloadLink</code> of this <code>VersionInfo</code>.<br/>
     * The method {@link #hasDownloadLink()} should be called first to ensure
     * the <code>downloadLink</code> is set.
     * @return the <code>downloadLink</code>
     */
    public String getDownloadLink()
    {
        return downloadLink;
    }
    
    public void download(File path) throws IOException
    {
        download(path, null);
    }
    
    public void download(File path, String fileName) throws IOException
    {
        
        if(fileName == null)
        {//download the update file into it directly without changing the file's name 
            
            if(!path.exists()) path.mkdirs();
            
            File file = new File(path, getDownloadFileName());
            
            downloadToFile(file);
            
        }
        else
        {//write to the file with name fileName
            
            downloadToFile(new File(path, fileName));
            
        }
        
    }
    
    private void downloadToFile(File file) throws IOException
    {
        
        assert downloadLink != null;
        
        if(!file.exists()) file.createNewFile();
        
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                BufferedInputStream bis = new BufferedInputStream(new URL(downloadLink).openStream());)
        {
            
            byte[] buffer = new byte[2048];
            int bytesRead = 0;
            
            while((bytesRead = bis.read(buffer)) != -1)
            {
                
                bos.write(buffer, 0, bytesRead);
                
            }
            
        }
        
    }
    
    private String getDownloadFileName()
    {
        
        assert downloadLink != null;
        
        String[] split = getDownloadLink().split("/");
        
        return split[split.length - 1];
    }
    
}
