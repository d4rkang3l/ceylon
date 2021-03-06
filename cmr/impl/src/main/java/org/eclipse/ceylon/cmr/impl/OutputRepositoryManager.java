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

package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.cmr.api.AbstractRepositoryManager;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleSearchResult;
import org.eclipse.ceylon.cmr.api.ModuleVersionQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Output manager.
 * Treat compiler output differently from runtime repos.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OutputRepositoryManager extends AbstractRepositoryManager {

    private final RepositoryManager output;
    private final RepositoryManager manager; // default root manager

    private static File getOutputDir() {
        return new File("output"); // TODO
    }

    public OutputRepositoryManager(RepositoryManager manager, Logger log) {
        this(getOutputDir(), manager, log);
    }

    public OutputRepositoryManager(File outputDir, RepositoryManager manager, Logger log) {
        this(new RootRepositoryManager(outputDir, log, null), manager, log);
    }

    public OutputRepositoryManager(RepositoryManager output, RepositoryManager manager, Logger log) {
        super(log, null);
        if (output == null)
            throw new IllegalArgumentException("Output is null!");
        if (manager == null)
            throw new IllegalArgumentException("Manager is null!");

        this.output = output;
        this.manager = manager;
    }

    @Override
    public List<CmrRepository> getRepositories() {
        List<CmrRepository> repos = new ArrayList<>();
        repos.addAll(output.getRepositories());
        repos.addAll(manager.getRepositories());
        return repos;
    }

    public List<String> getRepositoriesDisplayString() {
        List<String> displayStrings = new ArrayList<String>();
        displayStrings.addAll(output.getRepositoriesDisplayString());
        displayStrings.addAll(manager.getRepositoriesDisplayString());
        return displayStrings;
    }

    public ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException {
        final ArtifactResult result = output.getArtifactResult(context);
        if (result != null) {
            return result;
        } else {
            return manager.getArtifactResult(context);
        }
    }

    public void putArtifact(ArtifactContext context, InputStream content) throws RepositoryException {
        output.putArtifact(context, content);
    }

    public void putArtifact(ArtifactContext context, File content) throws RepositoryException {
        output.putArtifact(context, content);
    }

    public void removeArtifact(ArtifactContext context) throws RepositoryException {
        output.removeArtifact(context);
    }

    @Override
    public boolean isSameFile(ArtifactContext context, File srcFile) throws RepositoryException {
        if (output instanceof AbstractRepositoryManager) {
            return ((AbstractRepositoryManager)output).isSameFile(context, srcFile);
        }
        return false;
    }

    @Override
    public String toString() {
        return "OutputRepositoryManager: " + output;
    }
    
    @Override
    public ModuleSearchResult completeModules(ModuleQuery query) {
        return new ModuleSearchResult();
    }
    
    @Override
    public ModuleVersionResult completeVersions(ModuleVersionQuery query) {
        return new ModuleVersionResult(query.getName());
    }
    
    @Override
    public ModuleSearchResult searchModules(ModuleQuery query) {
        return new ModuleSearchResult();
    }
    
    @Override
    public void refresh(boolean recurse) {
        output.refresh(recurse);
        manager.refresh(recurse);
    }

    @Override
    public ArtifactContext getArtifactOverride(ArtifactContext context) throws RepositoryException {
        return context;
    }

    @Override
    public boolean isValidNamespace(String namespace) {
        return namespace == null 
            || DefaultRepository.NAMESPACE.equals(namespace);
    }
}
