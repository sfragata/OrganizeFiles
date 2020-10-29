package com.github.sfragata.organizefiles.resolver;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;

import java.nio.file.Path;
import java.util.function.Function;

class FolderNameByNameResolver
        implements FolderNameResolver {

    private static final int MINIMUM_FILE_LENGTH = 8;

    public FolderNameByNameResolver() {
        super();
    }

    @Override
    public Function<Path, String> getFunction() {
        return (final Path path) -> {

            if (path == null) {
                throw new UnresolvedFolderNameException("The parameter could not be null");
            }

            final Path fileName = path.getFileName();

            validateFileName(fileName);

            return String.format("%s-%s-%s", fileName.toString().substring(0, 4), fileName.toString().substring(4, 6),
                    fileName.toString().substring(6, 8));
        };
    }

    private void validateFileName(
            final Path fileName) {

        final String fileNameWithoutExtension = removeExtension(fileName);

        if (fileNameWithoutExtension.length() < MINIMUM_FILE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("File name '%s' does not match to the date format YYYYMMDD as prefix",
                            fileName.toString()));
        }
    }

    private String removeExtension(
            final Path fileName) {

        final String stringFileName = fileName.toString();
        return stringFileName.substring(0, stringFileName.lastIndexOf("."));

    }


}
