package com.github.sfragata.organizefiles.resolver;

import java.nio.file.Path;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;

class AbstractFolderNameResolver
    implements FolderNameResolver {

    private Function<Path, String> function;

    protected AbstractFolderNameResolver() {
    }

    protected void setFunction(
        final Function<Path, String> pFunction) {

        Validate.notNull(pFunction);

        this.function = pFunction;
    }

    @Override
    public String resolve(
        final Path path) {

        Validate.notNull(path);

        return this.function.apply(path);
    }

}
