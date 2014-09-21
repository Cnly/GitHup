package io.github.Cnly.GitHup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils
{
    
    /**
     * Copies the file <code>source</code> to <code>dest</code>.</br>
     * The destination file WILL be overwritten if it's already existed.
     * @param source source file
     * @param dest destination file
     * @throws IOException 
     */
    public static void copyFile(File source, File dest) throws IOException
    {
        
        if(!dest.exists()) dest.createNewFile();
        
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        
        try(BufferedInputStream bin = new BufferedInputStream(new FileInputStream(source));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));)
        {
            while((bytesRead = bin.read(buffer)) != -1)
            {
                bos.write(buffer, 0, bytesRead);
            }
        }
        
    }
    
}
