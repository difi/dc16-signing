#!/bin/sh

echo "Prepare docs..."

find /signing/docs -name .git | rm -fr

for adoc in $(find /signing/docs | grep ".adoc$"); do
	echo "Converting $adoc."
        asciidoctor-pdf $adoc
        rm $adoc
done
