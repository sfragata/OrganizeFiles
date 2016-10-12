/**
 *
 */
package com.github.sfragata.organizefiles.resolver;

import java.nio.file.Path;

/**
 * @author Silvio Fragata da Silva
 *
 */
@FunctionalInterface
public interface FolderNameResolver {
    String resolve(
        Path path);
}
