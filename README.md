# OrganizeFiles

[![Build Status](https://travis-ci.org/sfragata/OrganizeFiles.svg?branch=master)](https://travis-ci.org/sfragata/OrganizeFiles)

Java application to organize all files from a source folder (organize by file name or file date attributes)

### Usage 
```
/target/bin/organizedfiles (BY_FILE_NAME_WITH_DATE | BY_FILE_ATTRIBUTE_DATE) source-path target-path
```

#### Example

###### BY_FILE_NAME_WITH_DATE
```
/source
    20171017_photo01.jpg
```
it will create a target folder 2017-10-17


###### BY_FILE_ATTRIBUTE_DATE
```
/source
    photo01.jpg (creating time: 2017-10-19...)
```
it will create a target folder based on file creation time (2010-10-19)


