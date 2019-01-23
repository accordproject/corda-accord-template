package net.corda.accord;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AccordUtils {

    public static InputStream getCompressed(InputStream is )
            throws IOException
    {
        byte data[] = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream( bos );
        BufferedInputStream entryStream = new BufferedInputStream( is, 2048);
        ZipEntry entry = new ZipEntry( "" );
        zos.putNextEntry( entry );
        int count;
        while ( ( count = entryStream.read( data, 0, 2048) ) != -1 )
        {
            zos.write( data, 0, count );
        }
        entryStream.close();
        zos.closeEntry();
        zos.close();

        return new ByteArrayInputStream( bos.toByteArray() );
    }

    public static InputStream getStateFromContract() throws IOException {
        String[] command = {"./resources/cicero-parse.sh", "java/AccordProject/cicero-template-library/src/promissory-note"};
        ProcessBuilder ciceroParse = new ProcessBuilder(command);
        ciceroParse.directory(new File("./src/main"));
        return ciceroParse.start().getInputStream();
    }

}
