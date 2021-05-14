#!/bin/bash

# start gui
currentPath=$(pwd)
cmd="cd ${currentPath}\nsleep 1\njava -cp target/BlockChain_FileSystem.jar ece465.client_gui"
osascript -e "tell app \"Terminal\"
    do script \"$cmd\"
end tell"

# run backend server
java -cp target/BlockChain_FileSystem.jar ece465.server
