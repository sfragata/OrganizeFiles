package com.github.sfragata.organizefiles.resolver;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

class FolderNameByDateAttributesResolver
        implements FolderNameResolver {

    public FolderNameByDateAttributesResolver() {
        super();

    }

    @Override
    public Function<Path, String> getFunction() {
        return (final Path path) -> {

            final BasicFileAttributes attr;
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
    }
}
