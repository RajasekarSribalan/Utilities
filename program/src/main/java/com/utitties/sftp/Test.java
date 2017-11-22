package com.utitties.sftp;

import java.io.InputStream;

public class Test {
	public static void main(String[] args) {
		System.out.println("Rajasekar");
		//new Test().scpPutFiles("","");
	}

	public void scpPutFiles(String srcDirName, String destDirName) {

		try{
			destDirName = "/var/tmp/";
			srcDirName = "/wiremock-1.57-standalone.jar";
			System.out.println(srcDirName);
			InputStream KeySchemaFileStream = Test.class
					.getResourceAsStream(srcDirName);
			System.out.println(KeySchemaFileStream);
			JSch jsch = new JSch();

			Session session = jsch.getSession("edmproc", "vmx-edm-094", 22);
			session.setPassword("EvaiKiO1234567890!");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
			channelSftp.cd(destDirName);
			channelSftp.put(KeySchemaFileStream,"wiremock-1.57-standalone.jar");
			channelSftp.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
			
			/*try (FileInputStream srcIs = (FileInputStream) KeySchemaFileStream) {
			//	channelSftp.put(srcIs, srcDir.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
*/
		//	channelSftp.disconnect();

	
	}
}
