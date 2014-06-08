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

package org.jclouds.karaf.commands.blobstore;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.felix.gogo.commands.Command;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.domain.Location;

/**
 * List all location names.
 */
@Command(scope = "jclouds", name = "blobstore-location-list", description = "List all location names")
public class LocationListCommand extends BlobStoreCommandWithOptions {

   @Override
   protected Object doExecute() throws Exception {
      BlobStore blobStore = getBlobStore();
      List<String> locationNames = Lists.newArrayList();

      for (Location loc : blobStore.listAssignableLocations()) {
          locationNames.add(loc.getId());
      }

      Collections.sort(locationNames);
      for (String locationName : locationNames) {
         System.out.println(locationName);
      }

      return null;
   }
}
