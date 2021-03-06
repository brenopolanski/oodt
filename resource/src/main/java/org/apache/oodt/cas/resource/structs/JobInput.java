/*
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


package org.apache.oodt.cas.resource.structs;


import java.util.Map;
import java.util.Vector;

import org.apache.oodt.cas.metadata.Metadata;
//OODT imports
import org.apache.oodt.cas.resource.util.Configurable;
import org.apache.oodt.cas.resource.util.XmlRpcWriteable;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Input to a job that should be writeable over the XML-RPC wire.
 * </p>.
 */
public interface JobInput extends XmlRpcWriteable, Configurable {

  /**
   * Gets the ID of this JobInput
   * 
   * @return The string identifier of this JobInput object.
   */
  String getId();

  Map<String, Vector<String>> getMetadata();
  
  
}
