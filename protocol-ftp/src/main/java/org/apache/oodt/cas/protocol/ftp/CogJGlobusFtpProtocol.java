/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.oodt.cas.protocol.ftp;

//JDK imports
import java.io.File;
import java.util.List;
import java.util.Vector;

//Globus imports
import org.apache.oodt.cas.protocol.Protocol;
import org.apache.oodt.cas.protocol.ProtocolFile;
import org.apache.oodt.cas.protocol.auth.Authentication;
import org.apache.oodt.cas.protocol.exceptions.ProtocolException;
import org.globus.ftp.FTPClient;
import org.globus.ftp.FileInfo;

/**
 * FTP implementation of a {@link Protocol}
 * 
 * @author bfoster
 */
public class CogJGlobusFtpProtocol implements Protocol {

  private FTPClient ftp;

  private boolean isConnected;
  
  public void cd(ProtocolFile file) throws ProtocolException {
      try {
          ftp.changeDir(file.getPath());
      } catch (Exception e) {
          throw new ProtocolException("Failed to cd to " + file + " : "
                  + e.getMessage());
      }
  }

  public void connect(String host, Authentication auth) throws ProtocolException {
      try {
          ftp = new FTPClient(host, 21);
      } catch (Exception e) {
          throw new ProtocolException("Failed to connect to: " + host + " : "
                  + e.getMessage());
      }
      isConnected = true;

      try {
          ftp.authorize(auth.getUser(), auth.getPass());
          ftp.setActive(ftp.setLocalPassive());
      } catch (Exception e) {
          throw new ProtocolException("Failed to login to: " + host + " : "
                  + e.getMessage());
      }
  }

  public void close() throws ProtocolException {
      try {
          ftp.close();
          isConnected = false;
      } catch (Exception e) {
          throw new ProtocolException("Error disconnecting from "
                  + ftp.getHost() + " : " + e.getMessage());
      }
  }

  public void get(ProtocolFile fromFile, File toFile)
          throws ProtocolException {
      try {
          ftp.setActive(ftp.setLocalPassive());
          ftp.get(fromFile.getPath(), toFile);
      } catch (Exception e) {
          throw new ProtocolException("Failed to download: " + fromFile.getName()
                  + " : " + e.getMessage());
      }
  }
  
  public void put(File fromFile, ProtocolFile toFile) throws ProtocolException {
	  try {
		  ftp.put(fromFile, toFile.getPath(), false);
	  }catch (Exception e) {
		  throw new ProtocolException("Failed to put file '" + fromFile + "' : " + e.getMessage(), e);
	  }
  }
  
  public List<ProtocolFile> ls() throws ProtocolException {
      try {
          ftp.setActive(ftp.setLocalPassive());
          Vector<FileInfo> fileList = (Vector<FileInfo>) ftp.list("*", null);
          Vector<ProtocolFile> returnList = new Vector<ProtocolFile>();
          String path = this.pwd().getPath();
          for (FileInfo file : fileList) {
              returnList.add(new ProtocolFile(path + File.separator 
            		  + file.getName(), file.isDirectory()));
          }
          return returnList;
      } catch (Exception e) {
          throw new ProtocolException("Failed to get list of files : "
                  + e.getMessage());
      }
  }

  public ProtocolFile pwd() throws ProtocolException {
      try {
          return new ProtocolFile(ftp.getCurrentDir(), true);
      } catch (Exception e) {
          throw new ProtocolException("Failed to pwd : " + e.getMessage());
      }
  }

  public boolean connected() {
      return isConnected;
  }

  public void delete(ProtocolFile file) throws ProtocolException {
	  try {
		ftp.deleteFile(file.getPath());
	} catch (Exception e) {
		throw new ProtocolException("Failed to download file '" 
				+ file.getPath() + "' : " + e.getMessage(), e);
	}
  }

}