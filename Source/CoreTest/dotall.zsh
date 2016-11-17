#! /usr/bin/zsh
for file in *.dot;
do
  echo ${file:r}.png
  dot -Tpng $file > ${file:r}.png
done
