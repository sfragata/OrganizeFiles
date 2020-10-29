package com.github.sfragata.organizefiles;

import com.github.sfragata.organizefiles.resolver.FolderNameResolutionType;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class OrganizeFiles {

    private static final Logger logger = LoggerFactory.getLogger(OrganizeFiles.class);

    public OrganizeFiles() {
        super();
    }

    public void organize(
            final Path source,
            final Path target,
            final FolderNameResolutionType folderNameResolutionType)
            throws IOException {

        Validate.notNull(source, "the source couldn't be null");
        Validate.notNull(target, "the target couldn't be null");
        Validate.isTrue(Files.isDirectory(source, LinkOption.NOFOLLOW_LINKS), "the source must be a directory");

        createOrValidateTargetFolder(target);

        logger.info("The folders will be created {}", folderNameResolutionType.name());

        Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                .filter(path -> !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).parallel().forEach((
                eachPath) -> {
            try {

                final String fileName = folderNameResolutionType.getResolver().resolve(eachPath);

                final Path targetPath = target.getFileSystem().getPath(target.toString(), fileName);

                createOrValidateTargetFolder(targetPath);

                logger.info("Copying file {} to {}", eachPath.toString(), targetPath.toString());

                Files.copy(eachPath,
                        targetPath.getFileSystem().getPath(targetPath.toString(), eachPath.getFileName().toString()),
                        COPY_ATTRIBUTES);

            } catch (final Exception e) {
                logger.error(String.format("Error in file %s", eachPath.toString()), e);
            }
        });
    }

    private void createOrValidateTargetFolder(
            final Path folder)
            throws IOException {

        if (!Files.exists(folder, LinkOption.NOFOLLOW_LINKS)) {
            logger.info("Creating target folder '{}'", folder);
            Files.createDirectory(folder);
        } else {
            Validate.isTrue(Files.isDirectory(folder, LinkOption.NOFOLLOW_LINKS), "target must be a directory");
        }
    }
}
