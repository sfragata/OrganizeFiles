package com.github.sfragata.organizefiles.resolver;

import java.nio.file.Path;
import java.util.function.Function;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;

public class FolderNameByNameResolver
    extends AbstractFolderNameResolver {

    public FolderNameByNameResolver() {
        super();

        final Function<Path, String> function = (
            final Path path) -> {

            if (path == null) {
                throw new UnresolvedFolderNameException("The parameter could not be null");
            }

            final Path fileName = path.getFileName();

            return String.format("%s-%s-%s", fileName.toString().substring(0, 4), fileName.toString().substring(4, 6),
                fileName.toString().substring(6, 8));
        };

        setFunction(function);
    }

}
