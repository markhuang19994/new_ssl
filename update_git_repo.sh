#! /bin/bash

cd /usr/local/docker/dummy_api || exit

git fetch --progress origin master
git clean -f
git checkout -f origin/master
