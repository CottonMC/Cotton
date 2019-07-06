#!/bin/bash
set -o igncr >/dev/null 2>&1 #
for f in *; do
	if [[ -d "$f" && ! "$f" == *-ext-res ]]; then
		echo 'foreach.sh: changing directory to' $f
		cd "$f"
		echo 'foreach.sh: running' "$@"
		eval "$@"
		cd ..
		echo
		echo
	else
		echo 'foreach.sh: ignoring' $f
	fi
done
