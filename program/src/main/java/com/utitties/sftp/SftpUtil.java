package com.utitties.sftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    
    private static String readFromFile(String command)
    {
    	String line = null;
        try
        {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER_NAME, HOST_NAME, 22);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            //Read data from remote machine
			InputStream stream = channelSftp.get("/home/karaf/.ssl/rmca-rest/server/keystore.passphase");
			
			 try {
	                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
	                
	                while ((line = br.readLine()) != null) {
	                    System.out.println(line);
	                }

	            } catch (IOException io) {
	                System.out.println("Exception occurred during reading file from SFTP server due to " + io.getMessage());
	                io.getMessage();

	            } catch (Exception e) {
	                System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
	                e.getMessage();

	            }
            channelSftp.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return line;

    }
}
