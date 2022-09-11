#!/bin/zsh

file="all.txt"
echo "" > $file
for i in **/*.java; do
    echo "/*==================================================" >> $file
    echo "* File Name: ${${i##*/}%.java}\n" >> $file
    echo "* File Directory: ${i%/*}" >> $file
    echo "==================================================*/" >> $file
    cat $i >> $file
    echo "\n" >> $file
done
