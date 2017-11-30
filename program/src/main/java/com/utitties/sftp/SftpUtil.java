package com.utitties.sftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpUtil
{
    private static final String HOST_NAME = "vmx-raj-090";
    private static final String USER_NAME = "rajasekar";
    private static final String PASSWORD = "rajasekar";

    public static void main(String[] args) throws JSchException, IOException
    {

        String destDirName = "/var/tmp/"; // This is directory of the destination where file will be placed
        String srcDirName = "/wiremock-1.57-standalone.jar"; // This is the source file to be transfered,which is available in resource folder
        scpPutFiles(srcDirName, destDirName);
        readFromFile("/home/karaf/.ssl/rmca-rest/server/keystore.passphase");
        executeCommands("sudo su sample.sample");
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
			InputStream stream = channelSftp.get(command); //Read data from remote machine

			
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
    
	private static void executeCommands(String command) throws JSchException,
			IOException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(USER_NAME, HOST_NAME, 22);
		session.setPassword(PASSWORD);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		List<String> result = new ArrayList<String>();
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			result.add(line);
		}
		System.out.println(Arrays.toString(result.toArray()));
		int exitStatus = channelExec.getExitStatus();
		channelExec.disconnect();
		if (exitStatus < 0) {
			System.out.println("Done, but exit status not set!");
		} else if (exitStatus > 0) {
			System.out.println("Done, but with error!");
		} else {
			System.out.println("Done!");
		}

	}
}
