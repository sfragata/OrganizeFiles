package com.github.sfragata.organizefiles.resolver;

import org.apache.commons.lang3.Validate;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * @author Silvio Fragata da Silva
 */
public interface FolderNameResolver {

    default String resolve(final Path path) {

        Validate.notNull(path);

        return getFunction().apply(path);
    }

    Function<Path, String> getFunction();
}
