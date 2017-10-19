package com.github.sfragata.organizefiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sfragata.organizefiles.resolver.FolderNameResolutionType;

public class OrganizeFilesLauncher {

    private static Logger logger = LoggerFactory.getLogger(OrganizeFilesLauncher.class);

    private OrganizeFilesLauncher(final String resolveType, final Path source, final Path target) throws IOException {
        final OrganizeFiles organizeFiles = new OrganizeFiles();
        organizeFiles.organize(source, target, FolderNameResolutionType.findByName(resolveType));
    }

    @SuppressWarnings("unused")
    public static void main(
        final String[] args) {

        try {

            if (args.length != 3) {
                logger.info(
                    "Usage:\n\t organizedfiles (BY_FILE_NAME_WITH_DATE | BY_FILE_ATTRIBUTE_DATE) <source-path> <target-path>");
                System.exit(1);
            }

            new OrganizeFilesLauncher(args[0], Paths.get(args[1]), Paths.get(args[2]));
        } catch (final IOException e) {
            logger.error("Error", e);
        }
    }
}
