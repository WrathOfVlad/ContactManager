#!/bin/zsh

for i in **/*.java; do
    echo "\n\n--------------------------------------------------" >> all.txt
    echo "File Directory: ${i%/*}" >> all.txt
    echo "File Name: ${${i##*/}%.java}\n" >> all.txt
    cat $i >> all.txt
    echo "--------------------------------------------------" >> all.txt
done
