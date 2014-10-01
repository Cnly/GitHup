package io.github.Cnly.GitHup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionInfo
{
    
    private static final Pattern VERSION_PATTERN = Pattern
            .compile("version: (.*)");
    private static final Pattern RELEASE_DATE_PATTERN = Pattern
            .compile("releaseDate: (.*)");
    private static final Pattern DOWNLOAD_LINK_PATTERN = Pattern
            .compile("downloadLink: (.*)");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile(
            "description: (.*)", Pattern.DOTALL);
    
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
    
    public VersionInfo(String version, String releaseDate, String description,
            String downloadLink)
    {
        
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
        this.downloadLink = downloadLink;
        
    }
    
    /**
     * Creates an instance of VersionInfo from <code>infoString</code>
     * @param infoString content of info.txt
     * @return an instance of VersionInfo parsed from <code>infoString</code>
     */
    public static VersionInfo fromInfoString(String infoString)
    {
        
        String version = null;
        String releaseDate = null;
        String downloadLink = null;
        String description = null;
        
        Matcher versionMatcher = VERSION_PATTERN.matcher(infoString);
        if (!versionMatcher.find())
            throw new AssertionError(
                    "Cannot find the version field in the info.txt!");
        version = versionMatcher.group(1);
        
        Matcher releaseDateMatcher = RELEASE_DATE_PATTERN.matcher(infoString);
        if (!releaseDateMatcher.find())
            throw new AssertionError(
                    "Cannot find the releaseDate field in the info.txt!");
        releaseDate = releaseDateMatcher.group(1);
        
        Matcher downloadLinkMatcher = DOWNLOAD_LINK_PATTERN.matcher(infoString);
        if (downloadLinkMatcher.find())
        {
            
            downloadLink = downloadLinkMatcher.group(1);
            
            if (downloadLink.startsWith("gh:"))
            {// The update file is on GitHub!
            
                String[] downloadLinkSplit = downloadLink.split(":");
                
                if (downloadLinkSplit.length != 5)
                    throw new IllegalStateException("Malformed downloadLink!");
                
                String username = downloadLinkSplit[1];
                String repoName = downloadLinkSplit[2];
                String projectName = downloadLinkSplit[3];
                String filename = downloadLinkSplit[4];
                
                downloadLink = new StringBuilder(
                        "https://raw.githubusercontent.com/").append(username)
                        .append('/').append(repoName)
                        .append("/githup-updates/").append(projectName)
                        .append("/files/").append(filename).toString();
                
            }
            
        }
        
        Matcher descriptionMatcher = DESCRIPTION_PATTERN.matcher(infoString);
        if (!descriptionMatcher.find())
            throw new AssertionError(
                    "Cannot find the description field in the info.txt!");
        description = descriptionMatcher.group(1);
        
        return new VersionInfo(version, releaseDate, description, downloadLink);
    }
    
    @Override
    public String toString()
    {
        return "VersionInfo [version=" + version + ", releaseDate="
                + releaseDate + ", description=" + description
                + ", downloadLink=" + downloadLink + "]";
    }
    
    /**
     * Calculates the hash code. The field <code>description</code> is NOT
     * involved.
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
     * Checks if the <code>downloadLink</code> of this <code>VersionInfo</code>
     * is set.
     * 
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
     * 
     * @return the <code>downloadLink</code>
     */
    public String getDownloadLink()
    {
        return downloadLink;
    }
    
    /**
     * Downloads the update file to <code>path</code> without changing the
     * filename.
     * 
     * @param path
     *            where to put the file
     * @throws IOException
     */
    public void download(File path) throws IOException
    {
        download(path, null);
    }
    
    /**
     * Downloads the update file to <code>path</code> and change the filename.
     * 
     * @param path
     *            where to put the file
     * @param fileName
     *            the new filename
     * @throws IOException
     */
    public void download(File path, String fileName) throws IOException
    {
        
        if (fileName == null)
        {// download the update file into it directly without changing the
         // file's name
        
            if (!path.exists()) path.mkdirs();
            
            File file = new File(path, getDownloadFileName());
            
            downloadToFile(file);
            
        }
        else
        {// write to the file with name fileName
        
            downloadToFile(new File(path, fileName));
            
        }
        
    }
    
    private void downloadToFile(File file) throws IOException
    {
        
        assert downloadLink != null;
        
        if (!file.exists()) file.createNewFile();
        
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file));
                BufferedInputStream bis = new BufferedInputStream(new URL(
                        downloadLink).openStream());)
        {
            
            byte[] buffer = new byte[2048];
            int bytesRead = 0;
            
            while ((bytesRead = bis.read(buffer)) != -1)
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
