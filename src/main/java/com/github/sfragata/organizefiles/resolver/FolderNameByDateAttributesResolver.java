package com.github.sfragata.organizefiles.resolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;

class FolderNameByDateAttributesResolver
    extends AbstractFolderNameResolver {

    public FolderNameByDateAttributesResolver() {
        super();

        final Function<Path, String> function = (
            final Path path) -> {

            BasicFileAttributes attr;
            try {
                attr = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (final IOException ioException) {
                throw new UnresolvedFolderNameException(
                    String.format("Could not read the attributes of the file %s", path.toString()),
                    ioException);
            }

            final Instant instant = attr.creationTime().toInstant();

            final LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            return date.format(DateTimeFormatter.ISO_DATE);
        };

        setFunction(function);
    }

}
