# base64clj

This is a tiny cli tool that does these transformations
```
raw bytes -> base64 string encoding
base64 string encoding -> raw bytes
```
At the moment it has been tested with text-files, png, jpeg, bmp but not exhaustively. It is also a pet project so consider it at least WIP and not for serious stuff.

## Build from source

    $ lein compile && lein uberjar`

## Usage

Usage:

    $ java -jar base64-0.1.0-standalone.jar encode <input-file> [<output-file>]
    $ java -jar base64-0.1.0-standalone.jar decode <input-file> <output-file>

## Options

`<input-file>` Currently the program only reads from a file in encoding as well as decoding case.

`<output-file>` Is optional in the encoding case at which the result is printed tou standard output.

### Tests

Currently not that many: `lein test`

### Build using GraalVM

Following the steps from [here](https://www.astrecipes.net/blog/2018/07/20/cmd-line-apps-with-clojure-and-graalvm/)


1) Download the latest release of the GraalVM community edition and decompress it
2) Export the uberjar and optionally rename to base64clj.jar
3) From the directory of the GraalVM

       $./bin/native-image -H:+ReportUnsupportedElementsAtRuntime -J-Xmx3G -J-Xms3G --no-server -jar /path/to/base64clj.jar
4) Invoke the created image example:
        
       $./base64clj encode base64_meme.jpeg
       

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
