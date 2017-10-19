package com.github.sfragata.organizefiles.resolver;

import java.nio.file.Path;
import java.util.function.Function;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;

class FolderNameByNameResolver
    extends AbstractFolderNameResolver {

    private static final int MINIMUM_FILE_LENGTH = 8;

    public FolderNameByNameResolver() {
        super();

        final Function<Path, String> function = (
            final Path path) -> {

            if (path == null) {
                throw new UnresolvedFolderNameException("The parameter could not be null");
            }

            final Path fileName = path.getFileName();

            validateFileName(fileName);

            return String.format("%s-%s-%s", fileName.toString().substring(0, 4), fileName.toString().substring(4, 6),
                fileName.toString().substring(6, 8));
        };

        setFunction(function);
    }

    private void validateFileName(
        final Path fileName) {

        final String fileNameWithoutExtention = removeExtension(fileName);

        if (fileNameWithoutExtention.length() < MINIMUM_FILE_LENGTH) {
            throw new IllegalArgumentException(
                String.format("File name '%s' does not match to the date format YYYMMDD as prefix",
                    fileName.toString()));
        }
    }

    private String removeExtension(
        final Path fileName) {

        final String stringFileName = fileName.toString();
        return stringFileName.substring(0, stringFileName.lastIndexOf("."));

    }

}
