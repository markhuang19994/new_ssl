#! /bin/bash

cd /usr/local/docker/dummy_api || exit

export GIT_SSH_COMMAND="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i /home/root/.ssh/key"
git fetch --progress origin master
git clean -f
git checkout -f origin/master
