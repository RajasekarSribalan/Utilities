package com.utitties.sftp;

import java.io.InputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpUtil
{
    private static final String HOST_NAME = "vmx-raj-090";
    private static final String USER_NAME = "rajasekar";
    private static final String PASSWORD = "rajasekar";

    public static void main(String[] args)
    {

        String destDirName = "/var/tmp/"; // This is directory of the destination where file will be placed
        String srcDirName = "/wiremock-1.57-standalone.jar"; // This is the source file to be transfered,which is available in resource folder
        scpPutFiles(srcDirName, destDirName);
    }

    private static void scpPutFiles(String srcDirName, String destDirName)
    {

        try
        {
            InputStream KeySchemaFileStream = SftpUtil.class
                    .getResourceAsStream(srcDirName);
            JSch jsch = new JSch();

            Session session = jsch.getSession(USER_NAME, HOST_NAME, 22);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(destDirName);
            channelSftp.put(KeySchemaFileStream, "filename");// Sending the file to remote directory
            channelSftp.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
