package io.github.Cnly.GitHup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GitHup
{
    
    private final long checkInterval;
    private final VersionInfo originalVersion;
    private final GitHupListener listener;
    
    private final URL baseUrl;
    private final URL infoUrl;
    
    private boolean started = false;
    private Timer timer = null;
    
    /**
     * Create an instance of GitHup.
     * Once the GitHup instance is started, it will check the info.txt
     * and parse it into a {@link VersionInfo}. Then, GitHup will compare
     * it with the <code>originalVersion</code>. If they are different in
     * <code>version</code> and <code>releaseDate</code>, GitHup will
     * call <code>listener.onUpdateAvailable(versionInfo)</code>.
     * 
     * @param username
     *            GitHub username
     * @param repoName
     *            GitHub repo name
     * @param projectName
     *            project name in the repo
     * @param checkInterval
     *            check interval in milliseconds
     * @param originalVersion
     *            original version information
     * @param listener
     *            The GitHup Listener for calling back when an update is
     *            available
     * @throws MalformedURLException
     */
    public GitHup(String username, String repoName, String projectName,
            long checkInterval, VersionInfo originalVersion,
            GitHupListener listener)
    {
        
        if (listener == null)
            throw new NullPointerException("listener cannot be null!");
        if (checkInterval <= 0L)
            throw new IllegalArgumentException(
                    "checkInterval must be positive!");
        
        this.checkInterval = checkInterval;
        this.originalVersion = originalVersion;
        this.listener = listener;
        
        try
        {
            this.baseUrl = new URL(new StringBuilder(
                    "https://raw.githubusercontent.com/").append(username)
                    .append('/').append(repoName).append("/githup-updates/")
                    .append(projectName).append("/").toString());
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("Cannot create URL properly!", e);
        }
        try
        {
            this.infoUrl = new URL(baseUrl, "info.txt");
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("Cannot create URL properly!", e);
        }
        
    }
    
    /**
     * Start checking for updates.
     * 
     * @return false if it's already started. Otherwise true.
     */
    public boolean start()
    {
        
        if (started) return false;
        
        TimerTask timerTask = new TimerTask()
        {
            
            @Override
            public void run()
            {
                
               checkForUpdates(); 
                
            }
            
        };
        
        this.timer = new Timer();
        timer.schedule(timerTask, 0L, this.checkInterval);
        
        started = true;
        
        return true;
    }
    
    /**
     * Stop checking for updates. Once a GitHup is stopped, it cannot be started
     * again.
     * 
     * @return false if it's not started or it's been stopped once before or true if it's
     * stopped successfully.
     */
    public boolean stop()
    {
        
        if (!started) return false;
        
        timer.cancel();
        
        return true;
    }
    
    /**
     * Checks for updates manually
     */
    public void checkForUpdates()
    {
        
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(
                    infoUrl.openStream(), "utf-8"));
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        
        char[] buffer = new char[1024];
        StringBuilder sb = new StringBuilder();
        int charsRead = 0;
        
        try
        {
            while ((charsRead = reader.read(buffer)) != -1)
            {
                
                sb.append(buffer, 0, charsRead);
                
            }
        }
        catch (IOException e)
        {
            new IOException(
                    "An error occured while checking for update!", e)
                    .printStackTrace();
        }
        
        VersionInfo info = VersionInfo.fromInfoString(sb.toString());
        
        if (!this.originalVersion.equals(info))
        {// An update is available!
        
            this.listener.onUpdateAvailable(this, info);
            
        }
        
    }
    
    public boolean isStarted()
    {
        return started;
    }
    
}
