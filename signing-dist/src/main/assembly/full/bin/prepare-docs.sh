#!/bin/sh

for adoc in $(find /signing/docs | grep ".adoc$"); do
        asciidoctor-pdf $adoc
        rm $adoc
done
