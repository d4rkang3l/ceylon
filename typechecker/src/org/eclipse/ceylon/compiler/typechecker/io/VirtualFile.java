package org.eclipse.ceylon.compiler.typechecker.io;

import java.io.InputStream;
import java.util.List;

/**
 * Represents a file on the abstracted file system.
 * A file can be a folder in which case:
 *  - getInputStream() is unavailable
 *  - getChildren() returns the folder files
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public interface VirtualFile extends Comparable<VirtualFile> {
    /**
     * Does the file or folder exist and is it readable?
     */
    boolean exists();

    /**
     * Is the file a folder?
     */
    boolean isFolder();

    /**
     * File simple name
     */
    String getName();

    /**
     * Full file path
     */
    //should it be getURI instead?
    String getPath();

    /**
     * Relative file path
     */
    String getRelativePath(VirtualFile file);

    /**
     * InputStream representing the file.
     * Must be closed by the caller.
     * @throws exception when is a folder
     */
    InputStream getInputStream();

    /**
     * Unmodifiable list of folder children
     */
    List<? extends VirtualFile> getChildren();
}
