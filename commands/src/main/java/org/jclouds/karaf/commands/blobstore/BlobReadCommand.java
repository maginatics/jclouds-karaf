/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.karaf.commands.blobstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.jclouds.blobstore.BlobStore;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * @author: iocanel
 */
@Command(scope = "jclouds", name = "blobstore-read", description = "Reads data from the blobstore")
public class BlobReadCommand extends BlobStoreCommandWithOptions {

   @Argument(index = 0, name = "containerName", description = "The name of the container", required = true, multiValued = false)
   String containerName;

   @Argument(index = 1, name = "blobName", description = "The name of the blob", required = true, multiValued = false)
   String blobName;

   @Argument(index = 2, name = "toFile", description = "The file to store the blob", required = false, multiValued = false)
   String fileName;

   @Option(name = "-d", aliases = "--display", description = "Display the content to the console", required = false, multiValued = false)
   boolean display;

   @Option(name = "-e", aliases = "--exists", description = "Test whether a blob exists", required = false, multiValued = false)
   boolean exists;

   @Override
   protected Object doExecute() throws Exception {
      BlobStore blobStore = null;
      try {
         blobStore = getBlobStore();
      } catch (Throwable t) {
         System.err.println(t.getMessage());
         return null;
      }

      if (exists) {
          if (!blobStore.blobExists(containerName, blobName)) {
              throw new FileNotFoundException("Blob does not exist: " + blobName);
          }
          return null;
      }

      InputSupplier<InputStream> supplier = getBlobInputStream(blobStore, containerName, blobName);

      if (display) {
         CharStreams.copy(CharStreams.newReaderSupplier(supplier, Charsets.UTF_8), System.err);
         System.err.flush();
      } else {
         File file = new File(fileName);
         if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Could not create: " + file);
         }
         Files.copy(supplier, file);
      }

      return null;
   }
}
