#! /usr/bin/zsh
dot -Tsvg graph.dot > graph.svg
for file in *.dot;
do
  echo ${file:r}.svg
  dot -Tsvg $file > ${file:r}.svg
done
