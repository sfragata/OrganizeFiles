package com.github.sfragata.organizefiles;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrganizeFiles {

	private static Logger logger = LoggerFactory.getLogger(OrganizeFiles.class);

	private static Set<PosixFilePermission> PERMS = PosixFilePermissions.fromString("rwxr-x---");
	private static FileAttribute<Set<PosixFilePermission>> ATTR = PosixFilePermissions.asFileAttribute(PERMS);

	private OrganizeFiles() {

	}

	private String formatDate(long millis) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(new Date(millis));

	}

	public void organize(Path source, Path target) throws IOException {
		Validate.notNull(source, "the source couldn't be null");
		Validate.notNull(target, "the target couldn't be null");
		Validate.isTrue(Files.isDirectory(source, LinkOption.NOFOLLOW_LINKS), "the source must be a directory");

		if (!Files.exists(target, LinkOption.NOFOLLOW_LINKS)) {
			logger.info("Crating target folder '{}'", target);
			Files.createDirectory(target, ATTR);
		} else {
			Validate.isTrue(Files.isDirectory(target, LinkOption.NOFOLLOW_LINKS), "the target must be a directory");
		}

		Files.walk(source, FileVisitOption.FOLLOW_LINKS).filter(p -> !Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS))
				.parallel().forEach((eachPath) -> {
					try {
						BasicFileAttributes attr = Files.readAttributes(eachPath, BasicFileAttributes.class);

						String creationTime = formatDate(attr.creationTime().toMillis());

						Path targetPath = Paths.get(target.toString(), creationTime);

						if (!Files.exists(targetPath, LinkOption.NOFOLLOW_LINKS)) {
							logger.info("Crating target folder '{}'", targetPath);
							Files.createDirectory(targetPath, ATTR);
						}

						logger.info("Copying file {} to {}", eachPath.toString(), targetPath.toString());

						Files.copy(eachPath, Paths.get(targetPath.toString(), eachPath.getFileName().toString()),
								COPY_ATTRIBUTES);

						// logger.info("Attributes of {}", eachPath.toString());
						// logger.info("Creation {}",
						// formatDate(attr.creationTime().toMillis()));
						// logger.info("Last modified {}",
						// formatDate(attr.lastModifiedTime().toMillis()));
						// logger.info("Size {} \n\n", attr.size());

					} catch (Exception e) {
						logger.error(String.format("Error in file %s", eachPath.toString()), e);
					}
				});
	}

	public static void main(String[] args) {
		try {

			if (args.length != 2) {
				logger.info("Usage:\n\t organizefiles <source-path> <target-path>");
				System.exit(1);
			}

			new OrganizeFiles().organize(Paths.get(args[0]), Paths.get(args[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
