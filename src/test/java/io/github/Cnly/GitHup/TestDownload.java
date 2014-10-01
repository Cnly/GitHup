package io.github.Cnly.GitHup;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestDownload
{
    
    @Test
    public void test()
    {
        
        new GitHup("Cnly", "GitHupTest", "test-nolink", 600000L, new VersionInfo("thatVersion", "yesterday"), new GitHupListener()
        {
            
            @Override
            public void onUpdateAvailable(GitHup githup, VersionInfo info)
            {
                
                Assert.assertTrue(githup.stop());
                
                System.out.println("version without link:\n" + info);
                
                Assert.assertFalse(info.hasDownloadLink());
                
                System.out.println("version without link test ended!");
                
            }
            
        }).start();
        
        new GitHup("Cnly", "GitHupTest", "test", 600000L, new VersionInfo("thatVersion", "yesterday"), new GitHupListener()
        {
            
            @Override
            public void onUpdateAvailable(GitHup githup, VersionInfo info)
            {
                
                Assert.assertTrue(githup.stop());
                
                System.out.println("version with link:\n" + info);
                
                Assert.assertTrue(info.hasDownloadLink());
                
                try
                {
                    
                    System.out.println("downloading to download/");
                    info.download(new File("download/"));
                    Assert.assertTrue(new File("download/ProjectThornz-dependencies-1.0.0.jar").exists());
                    
                    System.out.println("downloading to download/jar.jar");
                    info.download(new File("download"), "jar.jar");
                    Assert.assertTrue(new File("download/jar.jar").exists());
                    
                    System.out.println("version with link test ended!");
                    
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
            }
            
        }).start();
        
        new GitHup("Cnly", "GitHupTest", "test-ghlink", 600000L, new VersionInfo("thatVersion", "yesterday"), new GitHupListener()
        {
            
            @Override
            public void onUpdateAvailable(GitHup githup, VersionInfo info)
            {
                
                Assert.assertTrue(githup.stop());
                
                System.out.println("version with GHlink:\n" + info);
                
                Assert.assertTrue(info.hasDownloadLink());
                
                try
                {
                    
                    System.out.println("downloading to download/");
                    info.download(new File("download/"));
                    Assert.assertTrue(new File("download/me.txt").exists());
                    
                    System.out.println("downloading to download/myself.txt");
                    info.download(new File("download"), "myself.txt");
                    Assert.assertTrue(new File("download/myself.txt").exists());
                    
                    System.out.println("version with GHlink test ended!");
                    
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
            }
            
        }).start();
        
        System.out.println("TESTS HAVE STARTED. THE MAIN THREAD WILL WAIT ABOUT 20 SEC FOR THE TESTS TO FINISH.");
        
        try
        {
            Thread.sleep(20000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
    }
    
}
