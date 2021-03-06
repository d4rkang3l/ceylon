/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.ceylon.cmr.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Helper class.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Helper {
    public static InputStream toInputStream(Node node) throws RepositoryException {
        try {
            return node.getInputStream();
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    public static InputStream toInputStream(File file) throws RepositoryException {
        try {
            return new FileInputStream(file);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }
}
