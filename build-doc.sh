#!/usr/bin/env bash
#asciidoctor -a stylesheet=docs/css.css -o docs/index.html index.adoc
#asciidoctor -o docs/index.html README.adoc
#asciidoctor README.adoc -a stylesheet=docs/css/css.css -o ./docs/index.html
rm -rf docs
mkdir -p docs/images
cp -r css/images/ docs/images
asciidoctor README.adoc -o ./docs/index.html
rm -rf docs.zip
zip -r docs.zip docs